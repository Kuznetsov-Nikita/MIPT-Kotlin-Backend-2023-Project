package model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Long,
    var login: String,
    var password: String,
    var name: String,
    val createdAt: String,
)
