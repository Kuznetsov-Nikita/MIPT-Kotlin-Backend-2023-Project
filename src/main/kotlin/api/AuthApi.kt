package api

import api.model.LoginRequest
import api.model.RegisterRequest
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import model.RefreshToken
import org.koin.ktor.ext.inject
import repository.TokensRepository
import repository.UsersRepository

fun Application.authApi() {
    routing {
        val usersRepository by inject<UsersRepository>()
        val tokensRepository by inject<TokensRepository>()

        post("/register") {
            val request = call.receive<RegisterRequest>()

            if (usersRepository.addUser(request.login, request.password, request.name)) {
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.Conflict)
            }
        }

        post("/login") {
            val request = call.receive<LoginRequest>()
            val user = usersRepository.getByLogin(request.login)

            if (user == null || user.password != request.password) {
                call.respond(HttpStatusCode.Unauthorized)
            } else {
                val tokenPair = tokensRepository.generateTokenPair(user.id)
                call.respond(tokenPair)
            }
        }

        post("/refresh") {
            val oldRefreshToken = call.receive<RefreshToken>().refreshToken
            val refreshTokenInfo = tokensRepository.getRefreshTokenInfo(oldRefreshToken)
            val currentTime = System.currentTimeMillis()

            if (refreshTokenInfo == null || refreshTokenInfo.expiresAt < currentTime) {
                call.respond(HttpStatusCode.Forbidden)
            } else {
                val tokenPair = tokensRepository.generateTokenPair(refreshTokenInfo.userId, true)
                tokensRepository.updateRefreshToken(oldRefreshToken, tokenPair.refreshToken)
                call.respond(tokenPair)
            }
        }
    }
}
