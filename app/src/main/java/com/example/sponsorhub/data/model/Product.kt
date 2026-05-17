package com.example.sponsorhub.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Product(

    @SerialName("id")
    val id: String = "",

    @SerialName("product_name")
    val productName: String = "",

    @SerialName("description")
    val description: String = "",

    @SerialName("product_url")
    val productUrl: String? = null,

    @SerialName("user_id")
    val userId: String = ""
)