package com.example.desafio.data.datasource.remote

import com.example.desafio.data.datasource.device.NetworkHandler
import com.example.desafio.domain.commons.Cancelled
import com.example.desafio.domain.commons.EmptyResponseBody
import com.example.desafio.domain.commons.NetworkConnection
import com.example.desafio.domain.commons.ServerError
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

abstract class ApiService {
    abstract val networkHandler: NetworkHandler

    suspend fun <T> request(
        default: T? = null,
        call: suspend () -> Response<T>
    ): Result<T> =
        withContext(Dispatchers.IO) {
            return@withContext when (networkHandler.isConnected()) {
                true -> performRequest(call, default)
                false -> Result.failure<T>(NetworkConnection())
            }
        }

    private suspend fun <T> performRequest(
        call: suspend () -> Response<T>,
        default: T? = null
    ): Result<T> {
        return try {
            val response = call()
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) }
                    ?: (default?.let { Result.success(it) } ?: Result.failure(EmptyResponseBody()))
            } else {
                Result.failure(ServerError())
            }
        } catch (exception: Throwable) {
            when (exception) {
                is CancellationException -> Result.failure(Cancelled())
                else -> Result.failure(ServerError())
            }
        }
    }
}