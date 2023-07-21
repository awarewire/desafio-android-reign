package com.example.desafio.domain.commons.errors

class NetworkConnection : Exception() {

    override fun hashCode(): Int {
        return super.hashCode() + 32
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return true
    }
}