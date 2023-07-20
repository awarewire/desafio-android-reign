package com.example.desafio.domain.commons

import com.example.desafio.domain.commons.errors.GenericUseCaseError
import kotlinx.coroutines.*

interface UseCaseExecutor {
    operator fun <T, P> UseCase<T, P>.invoke(
        scope: CoroutineScope,
        params: P,
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        onResult: (Result<T>) -> Unit = {}
    )

    operator fun <T, P> ObservableUseCase<T, P>.invoke(
        scope: CoroutineScope,
        params: P,
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        onResult: (Result<T>) -> Unit = {}
    )
}

class DefaultUseCaseExecutor : UseCaseExecutor {

    override operator fun <T, P> UseCase<T, P>.invoke(
        scope: CoroutineScope,
        params: P,
        dispatcher: CoroutineDispatcher,
        onResult: (Result<T>) -> Unit
    ) {
        val backgroundJob = scope.async(dispatcher) { run(params) }
        scope.launch {
            try {
                onResult(backgroundJob.await())
            } catch (e: Throwable) {
                onResult(Result.failure(GenericUseCaseError(e)))
            }
        }
    }

    override operator fun <T, P> ObservableUseCase<T, P>.invoke(
        scope: CoroutineScope,
        params: P,
        dispatcher: CoroutineDispatcher,
        onResult: (Result<T>) -> Unit
    ) {
        val backgroundJob = scope.async(dispatcher) { run(params) }
        scope.launch {
            try {
                backgroundJob.await().collect { onResult(it) }
            } catch (e: Throwable) {
                onResult(Result.failure(GenericUseCaseError(e)))
            }
        }
    }
}
