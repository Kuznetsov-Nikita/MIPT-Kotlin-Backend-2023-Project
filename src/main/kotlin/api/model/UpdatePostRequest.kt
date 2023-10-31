package api.model

import kotlinx.serialization.Serializable

@Serializable
data class UpdatePostRequest(
    val postText: String,
)
