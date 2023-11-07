package repository.model

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class UserEntity(id: EntityID<Long>): LongEntity(id) {
    companion object: LongEntityClass<UserEntity>(UsersTable)

    var login by UsersTable.login
    var password by UsersTable.password
    var name by UsersTable.name
    var createdAt by UsersTable.createdAt
}
