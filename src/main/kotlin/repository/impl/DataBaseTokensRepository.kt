package repository.impl

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import model.RefreshToken
import model.TokenPair
import org.jetbrains.exposed.sql.transactions.transaction
import repository.TokensRepository
import repository.model.RefreshTokenEntity
import repository.model.TokensTable
import repository.model.UserEntity
import java.time.Duration
import java.util.*

class DataBaseTokensRepository: TokensRepository {
    companion object {
        private const val accessLifeTime: Long = 30
        private const val refreshLifeTime: Long = 60
        private const val secret: String = "secret"
        private const val issuer: String = "issuer"
        private val algorithm: Algorithm = Algorithm.HMAC256(secret)
    }

    override fun generateTokenPair(userId: Long, isUpdate: Boolean): TokenPair {
        val currentTime = System.currentTimeMillis()

        val accessToken = JWT.create()
            .withSubject(userId.toString())
            .withExpiresAt(Date(currentTime + Duration.ofMinutes(accessLifeTime).toMillis()))
            .withIssuer(issuer)
            .sign(algorithm)

        val refreshToken = UUID.randomUUID().toString()

        if (!isUpdate) {
            transaction {
                val user = UserEntity.findById(userId)!!.id
                RefreshTokenEntity.new {
                    this.userId = user
                    this.refreshToken = refreshToken
                    expiresAt = currentTime + Duration.ofDays(refreshLifeTime).toMillis()
                }
            }
        }

        return TokenPair(accessToken, refreshToken)
    }

    override fun getRefreshTokenInfo(refreshToken: String): RefreshToken? {
        return transaction {
            val refreshTokenInfo = RefreshTokenEntity.find { TokensTable.refreshToken eq refreshToken }
            if (refreshTokenInfo.empty()) {
                null
            } else {
                refreshTokenInfo.first().toRefreshToken()
            }
        }
    }

    override fun updateRefreshToken(oldRefreshToken: String, newRefreshToken: String): Boolean {
        return transaction {
            val refreshTokenInfo = RefreshTokenEntity.find { TokensTable.refreshToken eq oldRefreshToken }

            if (refreshTokenInfo.empty()) {
                val currentTime = System.currentTimeMillis()
                val refreshToken = refreshTokenInfo.first()

                refreshToken.refreshToken = newRefreshToken
                refreshToken.expiresAt =  currentTime + Duration.ofDays(refreshLifeTime).toMillis()

                true
            } else {
                false
            }
        }
    }

    private fun RefreshTokenEntity.toRefreshToken(): RefreshToken {
        return RefreshToken(
            userId = userId.value,
            refreshToken = refreshToken,
            expiresAt = expiresAt,
        )
    }
}
