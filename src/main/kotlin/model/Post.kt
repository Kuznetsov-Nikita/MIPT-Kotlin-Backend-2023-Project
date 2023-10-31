package model

import kotlinx.serialization.Serializable

@Serializable
data class Post(
    val id: Long,
    var text: String,
    val createdAt: String,
    var updatedAt: String,
)
