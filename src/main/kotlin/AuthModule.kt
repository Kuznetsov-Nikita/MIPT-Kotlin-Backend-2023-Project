import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import repository.TokensRepository
import repository.UsersRepository
import repository.impl.DefaultTokensRepository
import repository.impl.DefaultUsersRepository

val authModule = module {
    singleOf(::DefaultUsersRepository) bind UsersRepository::class
    singleOf(::DefaultTokensRepository) bind TokensRepository::class
}
