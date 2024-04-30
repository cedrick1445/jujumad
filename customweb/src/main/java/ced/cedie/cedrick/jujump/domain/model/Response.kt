package ced.cedie.cedrick.jujump.domain.model

import java.lang.Exception

sealed class Response<out T> {
    data class Success<out T>(
        val data: T
    ): Response<T>()

    data class Error(
        val exception: Exception
    ): Response<Nothing>()
}
