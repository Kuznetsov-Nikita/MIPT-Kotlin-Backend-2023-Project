import repository.PostsRepository
import repository.impl.DefaultPostsRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val postsModule = module {
    singleOf(::DefaultPostsRepository) bind PostsRepository::class
}
