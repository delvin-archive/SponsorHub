package com.example.sponsorhub.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Article(

    @SerialName("id")
    val id: String = "",

    @SerialName("title")
    val title: String = "",

    @SerialName("content")
    val content: String = "",

    @SerialName("category")
    val category: String = "",

    @SerialName("created_at")
    val createdAt: String = ""
)