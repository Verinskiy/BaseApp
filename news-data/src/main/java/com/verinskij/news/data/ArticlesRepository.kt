package com.verinskij.news.data

import ArticleDBO
import com.verinskij.news.data.models.Article
import com.verinskij.news.data.models.toArticle
import com.verinskij.news.data.models.toArticleDbo
import com.verinskij.newsapi.NewsApi
import com.verinskij.newsapi.models.ArticleDTO
import com.verinskij.newsapi.models.ResponseDTO
import com.verinskij.newsdatabase.NewsDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach

class ArticlesRepository(
    private val database: NewsDatabase,
    private val api: NewsApi,
) {

    fun getAll(
        mergeStrategy: MergeStrategy<RequestResult<List<Article>>> = RequestResponseMergeStrategy()
    ): Flow<RequestResult<List<Article>>> {

        val cachedAllArticles = getAllFromDatabase()
            .map { result ->
                result.map { articlesDbos ->
                    articlesDbos.map { articleDbo -> articleDbo.toArticle() }
                }
            }

        val remoteArticles: Flow<RequestResult<List<Article>>> = getAllFromServer()
            .map { result: RequestResult<ResponseDTO<ArticleDTO>> ->
                result.map { response ->
                    response.articles.map { articleDto -> articleDto.toArticle() }
                }
            }

        return cachedAllArticles.combine(remoteArticles) { dbos, dtos->
            mergeStrategy.merge(dbos, dtos)
        }
    }

    private fun getAllFromServer(): Flow<RequestResult<ResponseDTO<ArticleDTO>>> {

        val apiRequest = flow { emit(api.everything()) }
            .onEach { result ->
                if (result.isSuccess) {
                    saveNetResponseToCache(checkNotNull(result.getOrThrow()).articles)
                }
            }
            .map { it.toRequestResult() }
        //.onStart { emit(RequestResult.InProgress()) }
        val start = flowOf<RequestResult<ResponseDTO<ArticleDTO>>>(RequestResult.InProgress())

        return merge(apiRequest, start)
    }

    private suspend fun saveNetResponseToCache(data: List<ArticleDTO>) {
        val dbos = data.map { articleDTO -> articleDTO.toArticleDbo() }
        database.articleDao.insert(dbos)
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