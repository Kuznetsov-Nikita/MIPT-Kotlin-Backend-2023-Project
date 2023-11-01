package repository

import model.User

interface UsersRepository {
    fun addUser(login: String, password: String, name: String): Boolean

    fun getByLogin(login: String): User?
}
