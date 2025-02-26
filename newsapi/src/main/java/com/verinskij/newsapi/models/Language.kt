package com.verinskij.newsapi.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class Language {
    @SerialName("ar")
    AR,
    @SerialName("de")
    DE,
    @SerialName("en")
    EN,
    @SerialName("es")
    ES,
}