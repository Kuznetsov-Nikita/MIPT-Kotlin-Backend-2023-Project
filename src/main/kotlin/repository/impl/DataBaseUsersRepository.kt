package repository.impl

import model.User
import org.jetbrains.exposed.sql.transactions.transaction
import repository.UsersRepository
import repository.model.UserEntity
import repository.model.UsersTable
import kotlin.random.Random

class DataBaseUsersRepository: UsersRepository {
    override fun addUser(login: String, password: String, name: String): Boolean {
        return transaction {
            if (getByLogin(login) != null) {
                false
            } else {
                val userId = Random.nextLong()
                UserEntity.new(userId) {
                    this.login = login
                    this.password = password
                    this.name = name
                }

                true
            }
        }
    }

    override fun getByLogin(login: String): User? {
        return transaction {
            val user = UserEntity.find { UsersTable.login eq login }
            if (user.empty()) {
                null
            } else {
                user.first().toUser()
            }
        }
    }

    private fun UserEntity.toUser(): User {
        return User(
            id = id.value,
            login = login,
            password = password,
            name = name,
            createdAt = createdAt.toString(),
        )
    }
}
