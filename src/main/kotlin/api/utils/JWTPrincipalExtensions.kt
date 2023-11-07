package api.utils

import io.ktor.server.auth.jwt.*

fun JWTPrincipal.getPrincipalParameter(name: String): String {
    return payload.getClaim("id").asString()
}
