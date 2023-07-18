package com.example.desafio.domain.commons

import kotlinx.coroutines.flow.Flow

interface ObservableUseCase<out Type, in Params> {

    fun run(params: Params): Flow<Result<Type>>
}
