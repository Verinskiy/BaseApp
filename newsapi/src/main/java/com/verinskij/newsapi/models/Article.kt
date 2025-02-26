package com.verinskij.newsapi.models

import com.verinskij.newsapi.utils.DateSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.Date

/**
 * {
 * -"source": {
 * "id": null,
 * "name": "Ambcrypto.com"
 * },
 * "author": "Denis Mwirigi",
 * "title": "Cardano’s recovery strengthens – ADA eyes breakout above THIS level - AMBCrypto News",
 * "description": "ADA’s outlook suggests long-term strength as market participation and sentiment drive Cardano’s bullish momentum.",
 * "url": "https://ambcrypto.com/cardanos-recovery-strengthens-ada-eyes-breakout-above-this-level/",
 * "urlToImage": "https://ambcrypto.com/wp-content/uploads/2025/02/Editors-34-1000x600.webp",
 * "publishedAt": "2025-02-24T08:01:48Z",
 * "content": "<ul><li>Cardanos ascending triangle formation signals investor accumulation and higher chances of a bullish breakout.</li><li>Rising Open Interest, network activity and bullish market sentiment allud… [+2224 chars]"
 * },
 */

@Serializable
data class Article(
    @SerialName("source") val source: Source,
    @SerialName("author") val author: String,
    @SerialName("title") val title: String,
    @SerialName("description") val description: String,
    @SerialName("url") val url: String,
    @SerialName("urlToImage") val urlToImage: String,
    @SerialName("publishedAt")
    @Serializable(with = DateSerializer::class)
    val publishedAt: Date,
    @SerialName("content") val content: String,
)

