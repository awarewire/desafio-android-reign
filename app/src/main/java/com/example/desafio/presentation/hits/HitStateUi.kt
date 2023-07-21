package com.example.desafio.presentation.hits

import android.os.Parcelable
import android.text.format.DateUtils
import com.example.desafio.domain.HitDomain
import kotlinx.parcelize.Parcelize
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Parcelize
data class HitStateUi(
    val id: String,
    val title: String,
    val dateCreated: Long,
    val author: String,
    val url: String
) : Parcelable {
    fun retrieveAuthorAndDate(): String {
        return "$author - ${DateUtils.getRelativeTimeSpanString(dateCreated)}"
    }

    fun retrieveEncodedURL(): String {
        return URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
    }
}

fun List<HitDomain>.toListStateUi() = this.map { entity -> entity.toStateUi() }
fun HitDomain.toStateUi() = HitStateUi(
    id = id,
    title = title,
    dateCreated = dateCreated,
    author = author,
    url = url
)