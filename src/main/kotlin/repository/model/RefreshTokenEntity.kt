package repository.model

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class RefreshTokenEntity(id: EntityID<Int>): IntEntity(id) {
    companion object: IntEntityClass<RefreshTokenEntity>(TokensTable)

    var userId by TokensTable.userId
    var refreshToken by TokensTable.refreshToken
    var expiresAt by TokensTable.expiresAt
}
