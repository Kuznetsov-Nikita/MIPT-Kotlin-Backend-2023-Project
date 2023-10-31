package repository.impl

import model.Post
import repository.PostsRepository
import java.time.Instant
import java.util.Collections.synchronizedSet
import kotlin.math.min
import kotlin.random.Random

class DefaultPostsRepository: PostsRepository {
    companion object {
        private const val postsPerPage: Int = 10
    }

    private val posts = synchronizedSet<Post>(mutableSetOf())
        
    override fun getAll(): Collection<Post> {
        return posts.toList()
    }

    override fun getPage(pageNum: Int): Collection<Post>? {
        val start: Int = pageNum * postsPerPage
        if (start >= posts.size) {
            return null
        }
        val end: Int = min(start + postsPerPage, posts.size)
        return posts.toList().subList(start, end)
    }

    override fun getById(id: Long): Post? {
        return posts.find { it.id == id }
    }

    override fun createPost(postText: String): Post {
        val creationTime = Instant.now().toString()
        val createdPost = Post(
            id = Random.nextLong(),
            text = postText,
            createdAt = creationTime,
            updatedAt = creationTime,
        )
        posts.add(createdPost)
        return createdPost
    }

    override fun updatePost(id: Long, postText: String): Post? {
        val post = posts.find { it.id == id }

        if (post != null) {
            post.text = postText
            post.updatedAt = Instant.now().toString()
        }

        return post
    }

    override fun deletePost(id: Long): Post? {
        val post = posts.find { it.id == id }

        if (post != null) {
            posts.removeIf { it.id == post.id }
        }

        return post
    }
}
