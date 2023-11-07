package model

import kotlinx.serialization.Serializable

@Serializable
data class RefreshToken(
    val userId: Long,
    var refreshToken: String,
    var expiresAt: Long,
)
