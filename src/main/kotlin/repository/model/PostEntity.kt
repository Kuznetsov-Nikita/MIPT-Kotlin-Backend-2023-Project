package repository.model

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class PostEntity(id: EntityID<Long>): LongEntity(id) {
    companion object: LongEntityClass<PostEntity>(PostsTable)

    var num by PostsTable.num
    var text by PostsTable.text
    var createdAt by PostsTable.createdAt
    var updatedAt by PostsTable.updatedAt
    var authorId by PostsTable.authorId
}
