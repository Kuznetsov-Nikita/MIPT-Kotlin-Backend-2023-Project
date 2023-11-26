import repository.PostsRepository
import repository.impl.DataBasePostsRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val postsModule = module {
    singleOf(::DataBasePostsRepository) bind PostsRepository::class
}
