package repository.model

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column

object TokensTable: IntIdTable("tokens") {
    val userId: Column<EntityID<Long>> = reference("userId", UsersTable)
    var refreshToken: Column<String> = varchar("refreshToken", length = 500)
    var expiresAt: Column<Long> = long("expiresAt")
}
