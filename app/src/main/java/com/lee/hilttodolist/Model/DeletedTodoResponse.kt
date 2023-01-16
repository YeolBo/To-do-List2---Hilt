package com.lee.mytodolist.Model


import com.google.gson.annotations.SerializedName

data class DeletedTodoResponse<T>(
    @SerializedName("data")
    val `data`: List<T>?,
    @SerializedName("message")
    val message: String?
)
