package repository.model

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object UsersTable: LongIdTable("users") {
    var login: Column<String> = varchar("login", length = 100)
    var password: Column<String> = varchar("password", length = 100)
    var name: Column<String> = varchar("name", length = 100)
    val createdAt: Column<Instant> = timestamp("createdAt").clientDefault { Clock.System.now() }
}
