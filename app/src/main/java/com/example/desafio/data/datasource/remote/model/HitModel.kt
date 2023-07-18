package com.example.desafio.data.datasource.remote.model

import com.example.desafio.domain.HitDomain
import com.google.gson.annotations.SerializedName
import kotlinx.datetime.toInstant

data class HitModel(
    @SerializedName("objectID")
    val id: String,
    @SerializedName("story_title")
    val title: String?,
    @SerializedName("title")
    val titleFallback: String?,
    @SerializedName("created_at")
    val dateCreated: String,
    @SerializedName("author")
    val author: String?,
    @SerializedName("story_url")
    val url: String?
)

fun List<HitModel>.toListDomain() = this.map { model -> model.toDomain() }
fun HitModel.toDomain() = HitDomain(
    id = id,
    title = title ?: titleFallback.orEmpty(),
    dateCreated = dateCreated.toInstant().toEpochMilliseconds(),
    author = author.orEmpty(),
    url = url.orEmpty()
)