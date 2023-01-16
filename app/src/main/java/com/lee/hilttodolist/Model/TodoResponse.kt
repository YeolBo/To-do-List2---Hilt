package com.lee.mytodolist.Model

import com.google.gson.annotations.SerializedName

//data class TodosResponse(
//    @SerializedName("data")
//    val `data`: List<Todo>?,
//    @SerializedName("message")
//    val message: String?,
//    @SerializedName("meta")
//    val meta: Meta?
//)
data class TodosResponse<T>(
    @SerializedName("data")
    val `data`: List<T>?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("meta")
    val meta: Meta?
)
