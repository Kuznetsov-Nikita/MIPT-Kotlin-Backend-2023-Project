package repository.impl

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import model.RefreshToken
import model.TokenPair
import repository.TokensRepository
import java.util.*
import java.time.Duration

class DefaultTokensRepository: TokensRepository {
    companion object {
        private const val accessLifeTime: Long = 30
        private const val refreshLifeTime: Long = 60
        private const val secret: String = "secret"
        private const val issuer: String = "issuer"
        private val algorithm: Algorithm = Algorithm.HMAC256(secret)
    }

    private val tokens = Collections.synchronizedSet<RefreshToken>(mutableSetOf())

    override fun generateTokenPair(userId: Long, isUpdate: Boolean): TokenPair {
        val currentTime = System.currentTimeMillis()

        val accessToken = JWT.create()
            .withSubject(userId.toString())
            .withExpiresAt(Date(currentTime + Duration.ofMinutes(accessLifeTime).toMillis()))
            .withIssuer(issuer)
            .sign(algorithm)

        val refreshToken = UUID.randomUUID().toString()

        if (!isUpdate) {
            val refreshTokenInfo = RefreshToken(
                userId = userId,
                refreshToken = refreshToken,
                expiresAt = currentTime + Duration.ofDays(refreshLifeTime).toMillis()
            )
            tokens.add(refreshTokenInfo)
        }

        return TokenPair(accessToken, refreshToken)
    }

    override fun getRefreshTokenInfo(refreshToken: String): RefreshToken? {
        return tokens.find { it.refreshToken == refreshToken }
    }

    override fun updateRefreshToken(oldRefreshToken: String, newRefreshToken: String): Boolean {
        var refreshTokenInfo = tokens.find { it.refreshToken == oldRefreshToken }

        if (refreshTokenInfo != null) {
            val currentTime = System.currentTimeMillis()

            refreshTokenInfo.refreshToken = newRefreshToken
            refreshTokenInfo.expiresAt =  currentTime + Duration.ofDays(refreshLifeTime).toMillis()

            return true
        } else {
            return false
        }
    }
}
