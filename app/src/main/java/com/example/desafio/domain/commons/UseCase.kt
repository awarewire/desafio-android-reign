package com.example.desafio.domain.commons

interface UseCase<out Type, in Params> {

    suspend fun run(params: Params): Result<Type>
}