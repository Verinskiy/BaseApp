package com.verinskij.newsapi.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Source(
    @SerialName("id") val id: Int? = 0,
    @SerialName("name") val name: String
)