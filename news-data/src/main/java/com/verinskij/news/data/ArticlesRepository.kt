package com.verinskij.news.data

import Article
import ArticleDBO
import com.verinskij.news.data.models.toArticle
import com.verinskij.news.data.models.toArticleDbo
import com.verinskij.newsapi.NewsApi
import com.verinskij.newsapi.models.ArticleDTO
import com.verinskij.newsapi.models.Response
import com.verinskij.newsdatabase.NewsDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import java.io.IOException

class ArticlesRepository(
    private val database: NewsDatabase,
    private val api: NewsApi,
) {

    fun getAll(): Flow<RequestResult<List<Article>>> {

        val cachedAllArticles: Flow<RequestResult.Success<List<ArticleDBO>>> = getAllFromDatabase()

        val remoteArticles: Flow<RequestResult<*>> = getAllFromServer()

        TODO()
    }

    private fun getAllFromServer(): Flow<RequestResult<List<ArticleDBO>>> {
        return flow { emit(api.everything()) }
            .map { result ->
                if (result.isSuccess) {
                    val response: Response<ArticleDTO> = result.getOrThrow()
                    RequestResult.Success(response.articles)
                } else {
                    RequestResult.Error(null)
                }
            }.filterIsInstance<RequestResult.Success<List<ArticleDTO>>>()
            .map { requestResult ->
                requestResult.map { dtos -> dtos.map { articleDTO -> articleDTO.toArticleDbo() } }
            }.onEach { reqestResult ->
                database.articleDao.insert(reqestResult.data)
            }
    }

    private fun getAllFromDatabase(): Flow<RequestResult.Success<List<ArticleDBO>>> {
        return database.articleDao
            .getAll()
            .map { RequestResult.Success(it) }
    }

    suspend fun search(query: String): Flow<Article> {
        api.everything()
        TODO("Not impl")
    }
}

sealed class RequestResult<E>(internal val data: E) {

    class InProgress<E>(data: E) : RequestResult<E>(data)
    class Success<E>(data: E) : RequestResult<E>(data)
    class Error<E>(data: E) : RequestResult<E>(data)
}

internal fun <T : Any> RequestResult<T?>.requireData(): T = checkNotNull(data)

internal fun <I, O> RequestResult<I>.map(mapper: (I) -> O): RequestResult<O> {
    val out = mapper(data)
    return when (this) {
        is RequestResult.Error -> RequestResult.Error(out)
        is RequestResult.InProgress -> RequestResult.InProgress(out)
        is RequestResult.Success -> RequestResult.Success(out)
    }
}