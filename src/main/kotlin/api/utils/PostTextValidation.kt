package api.utils

const val maxPostTextLength = 500

fun isPostTextValid(text: String): Boolean {
    return text.length <= maxPostTextLength
}
