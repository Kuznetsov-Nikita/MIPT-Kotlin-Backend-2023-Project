package repository

import model.Post

interface PostsRepository {
    fun getAll(): Collection<Post>

    fun getPage(pageNum: Int): Collection<Post>?

    fun getById(id: Long): Post?

    fun createPost(postText: String): Post

    fun updatePost(id: Long, postText: String): Post?

    fun deletePost(id: Long): Post?
}
