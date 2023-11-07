package api

import api.model.CreatePostRequest
import api.model.UpdatePostRequest
import api.utils.getPathParameter
import api.utils.getPrincipalParameter
import api.utils.isPostTextValid
import repository.PostsRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
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
            val pageNum = getPathParameter("page")?.toInt() ?: 0
            val posts = postsRepository.getPage(pageNum)

            if (posts == null) {
                call.respond(HttpStatusCode.NotFound)
            } else {
                call.respond(posts)
            }
        }

        get("/post/{id}/get") {
            val postId = getPathParameter("id")?.toLong() ?: 0
            val post = postsRepository.getById(postId)

            if (post == null) {
                call.respond(HttpStatusCode.NotFound)
            } else {
                call.respond(post)
            }
        }

        authenticate("access") {
            put("/post/create") {
                val request = call.receive<CreatePostRequest>()
                val principal = call.principal<JWTPrincipal>()

                if (!isPostTextValid(request.postText)) {
                    call.respond(HttpStatusCode.PayloadTooLarge)
                } else {
                    val userId = principal?.getPrincipalParameter("id")?.toLong()
                    if (userId == null) {
                        call.respond(HttpStatusCode.BadRequest)
                    } else {
                        val createdPost = postsRepository.createPost(request.postText, userId)
                        call.respond(createdPost)
                    }
                }
            }

            patch("/post/{id}/update") {
                val postId = getPathParameter("id")?.toLong() ?: 0
                val request = call.receive<UpdatePostRequest>()
                val principal = call.principal<JWTPrincipal>()

                if (!isPostTextValid(request.postText)) {
                    call.respond(HttpStatusCode.PayloadTooLarge)
                } else {
                    val userId = principal?.getPrincipalParameter("id")?.toLong()
                    var post = postsRepository.getById(postId)

                    if (post == null) {
                        call.respond(HttpStatusCode.NotFound)
                    } else {
                        if (userId != post.authorId) {
                            call.respond(HttpStatusCode.Forbidden)
                        }

                        post = postsRepository.updatePost(postId, request.postText)
                        call.respond(post!!)
                    }
                }
            }

            delete("/post/{id}/delete") {
                val postId = getPathParameter("id")?.toLong() ?: 0
                var post = postsRepository.getById(postId)
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.getPrincipalParameter("id")?.toLong()

                if (post == null) {
                    call.respond(HttpStatusCode.NotFound)
                } else {
                    if (userId != post!!.authorId) {
                        call.respond(HttpStatusCode.Forbidden)
                    }

                    post = postsRepository.deletePost(postId)
                    call.respond(post!!)
                }
            }
        }
    }
}
