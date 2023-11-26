package repository.model

import kotlinx.datetime.Clock
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Column
import kotlinx.datetime.Instant
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object PostsTable: LongIdTable("posts") {
    val num: Column<Int> = integer("num").autoIncrement()
    var text: Column<String> = varchar("text", length = 500)
    val createdAt: Column<Instant> = timestamp("createdAt").clientDefault { Clock.System.now() }
    var updatedAt: Column<Instant> = timestamp("updatedAt").clientDefault { Clock.System.now() }
    val authorId: Column<EntityID<Long>> = reference("authorId", UsersTable)
}
