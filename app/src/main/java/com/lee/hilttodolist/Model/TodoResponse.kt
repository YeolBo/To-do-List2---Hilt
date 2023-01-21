package com.lee.mytodolist.Model

import com.google.gson.annotations.SerializedName

data class TodoResponse<T>(
    @SerializedName("data")
    val `data`: T?,
    @SerializedName("message")
    val message: String?
)

data class TodosResponse<T>(
    @SerializedName("data")
    val `data`: List<T>?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("meta")
    val meta: Meta?
)
