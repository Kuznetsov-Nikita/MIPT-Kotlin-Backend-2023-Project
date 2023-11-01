package repository.impl

import model.User
import repository.UsersRepository
import java.time.Instant
import java.util.*
import kotlin.random.Random

class DefaultUsersRepository: UsersRepository {
    private val users = Collections.synchronizedSet<User>(mutableSetOf())

    override fun addUser(login: String, password: String, name: String): Boolean {
        val creationTime = Instant.now().toString()
        if (users.find { it.login == login } != null) {
            return false
        }

        val createdUser = User(
            id = Random.nextLong(),
            login = login,
            password = password,
            name = name,
            createdAt = creationTime,
        )

        users.add(createdUser)
        return true
    }

    override fun getByLogin(login: String): User? {
        return users.find { it.login == login }
    }
}
