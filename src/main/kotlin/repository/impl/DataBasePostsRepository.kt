package repository.impl

import kotlinx.datetime.Clock
import model.Post
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import repository.PostsRepository
import repository.model.PostEntity
import repository.model.PostsTable
import repository.model.UserEntity
import kotlin.random.Random

class DataBasePostsRepository: PostsRepository {
    companion object {
        private const val postsPerPage: Int = 10
    }

    override fun getAll(): Collection<Post> {
        return transaction {
            val query = PostsTable.selectAll()
            val entities = PostEntity.wrapRows(query)
            entities.map { it.toPost() }
        }
    }

    override fun getPage(pageNum: Int): Collection<Post>? {
        return transaction {
            val start: Int = pageNum * postsPerPage
            val query = PostsTable.select { PostsTable.num greaterEq start and (PostsTable.num less start + postsPerPage) }
            val entities = PostEntity.wrapRows(query)
            entities.map { it.toPost() }
        }
    }

    override fun getById(id: Long): Post? {
        return transaction {
            PostEntity.findById(id)?.toPost()
        }
    }

    override fun createPost(postText: String, userId: Long): Post {
        return transaction {
            val postId = Random.nextLong()
            val authorUser = UserEntity.findById(userId)

            val createdEntity = PostEntity.new(postId) {
                text = postText
                authorId = authorUser!!.id
            }

            createdEntity.toPost()
        }
    }

    override fun updatePost(id: Long, postText: String): Post? {
        return transaction {
            val post = PostEntity.findById(id)

            post?.text = postText
            post?.updatedAt = Clock.System.now()

            post?.toPost()
        }
    }

    override fun deletePost(id: Long): Post? {
        return transaction {
            val post = PostEntity.findById(id)
            val postInfo = post?.toPost()

            post?.delete()

            postInfo
        }
    }

    private fun PostEntity.toPost(): Post {
        return Post(
            id = id.value,
            text = text,
            createdAt = createdAt.toString(),
            updatedAt = updatedAt.toString(),
            authorId = authorId.value
        )
    }
}
