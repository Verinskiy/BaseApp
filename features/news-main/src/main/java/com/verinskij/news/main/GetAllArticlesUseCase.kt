package com.verinskij.news.main

import Article
import com.verinskij.news.data.ArticlesRepository
import kotlinx.coroutines.flow.Flow

class GetAllArticlesUseCase(private val repository: ArticlesRepository) {

    operator suspend fun invoke(): Flow<Article> {
            return repository.getAll()
    }
}