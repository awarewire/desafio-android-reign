package com.example.desafio.presentation.fake

import com.example.desafio.presentation.hits.HitStateUi

object FakeData {

    fun getFakeHitStateUi(): HitStateUi {
        return HitStateUi(
            id = "",
            title = "Title",
            dateCreated = 1222L,
            author = "Author",
            url = "g"
        )
    }
}