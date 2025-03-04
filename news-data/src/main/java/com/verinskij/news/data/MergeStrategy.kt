package com.verinskij.news.data

interface MergeStrategy<E> {

    fun merge(cache: E, server: E): E
}

internal class RequestResponseMergeStrategy<T: Any> : MergeStrategy<RequestResult<T>> {

    override fun merge(cache: RequestResult<T>, server: RequestResult<T>): RequestResult<T> {
        return when {
            cache is RequestResult.InProgress && server is RequestResult.InProgress ->
                merge(cache, server)

            else -> error("UnImplemented branch")
        }
    }

    private fun merge(
        cache: RequestResult.InProgress<T>,
        server: RequestResult.InProgress<T>
    ): RequestResult<T> {
        return when {
            server.data!= null -> RequestResult.InProgress(server.data)
            else -> RequestResult.InProgress(cache.data)
        }
    }

    private fun merge(
        cache: RequestResult.InProgress<T>,
        server: RequestResult.Success<T>
    ): RequestResult<T> {
        return RequestResult.Success(checkNotNull( server.data))
    }

    private fun merge(
        cache: RequestResult.Success<T>,
        server: RequestResult.InProgress<T>
    ): RequestResult<T> {
        return RequestResult.InProgress(cache.data)
    }

    private fun merge(
        cache: RequestResult.Success<T>,
        server: RequestResult.Error<T>
    ): RequestResult<T> {
        return RequestResult.Error(cache.data, server.error)
    }

}