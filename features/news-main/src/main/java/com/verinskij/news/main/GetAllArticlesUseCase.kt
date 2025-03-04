package com.verinskij.news.main

import com.verinskij.news.data.ArticlesRepository
import com.verinskij.news.data.RequestResult
import com.verinskij.news.data.map
import com.verinskij.news.main.models.Article
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.verinskij.news.data.models.Article as DataArticle

class GetAllArticlesUseCase(private val repository: ArticlesRepository) {

    operator fun invoke(): Flow<RequestResult<List<Article>>> {
            return repository.getAll()
                .map { requestResult ->
                    requestResult.map { articles ->
                        articles.map { it.toUiArticle() }
                    }
                }
    }
}

private fun DataArticle.toUiArticle(): Article {
    TODO()
}