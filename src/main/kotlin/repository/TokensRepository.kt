package repository

import model.RefreshToken
import model.TokenPair

interface TokensRepository {
    fun generateTokenPair(userId: Long, isUpdate: Boolean = false): TokenPair

    fun getRefreshTokenInfo(refreshToken: String): RefreshToken?

    fun updateRefreshToken(oldRefreshToken: String, newRefreshToken: String): Boolean
}
