import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import repository.TokensRepository
import repository.UsersRepository
import repository.impl.DataBaseTokensRepository
import repository.impl.DataBaseUsersRepository

val authModule = module {
    singleOf(::DataBaseUsersRepository) bind UsersRepository::class
    singleOf(::DataBaseTokensRepository) bind TokensRepository::class
}
