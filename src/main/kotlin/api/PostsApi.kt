package api

import api.model.CreatePostRequest
import api.model.UpdatePostRequest
import api.utils.isPostTextValid
import repository.PostsRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.postsApi() {
    routing {
        val postsRepository by inject<PostsRepository>()

        get("/posts/all") {
            val posts = postsRepository.getAll()
            call.respond(posts)
        }

        get("/posts/getPage/{page}") {
            val pageNum = call.parameters["page"]?.toInt() ?: 0
            val posts = postsRepository.getPage(pageNum)

            if (posts == null) {
                call.respond(HttpStatusCode.NotFound)
            } else {
                call.respond(posts)
            }
        }

        get("/post/{id}/get") {
            val postId = call.parameters["id"]?.toLong() ?: 0
            val post = postsRepository.getById(postId)

            if (post == null) {
                call.respond(HttpStatusCode.NotFound)
            } else {
                call.respond(post)
            }
        }

        put("/post/create") {
            val request = call.receive<CreatePostRequest>()

            if (!isPostTextValid(request.postText)) {
                call.respond(HttpStatusCode.PayloadTooLarge)
            } else {
                val createdPost = postsRepository.createPost(request.postText)
                call.respond(createdPost)
            }
        }

        patch("/post/{id}/update") {
            val postId = call.parameters["id"]?.toLong() ?: 0
            val request = call.receive<UpdatePostRequest>()

            if (!isPostTextValid(request.postText)) {
                call.respond(HttpStatusCode.PayloadTooLarge)
            } else {
                val post = postsRepository.updatePost(postId, request.postText)

                if (post == null) {
                    call.respond(HttpStatusCode.NotFound)
                } else {
                    call.respond(post)
                }
            }
        }

        delete("/post/{id}/delete") {
            val postId = call.parameters["id"]?.toLong() ?: 0
            val post = postsRepository.deletePost(postId)

            if (post == null) {
                call.respond(HttpStatusCode.NotFound)
            } else {
                call.respond(post)
            }
        }
    }
}
