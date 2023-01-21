package com.lee.hilttodolist

import com.lee.hilttodolist.Interface.MainApiService
import com.lee.hilttodolist.Model.ApiResponse
import com.lee.mytodolist.Model.Todo
import javax.inject.Inject


class MainRepository @Inject constructor(
    private val mainApiService: MainApiService
) {
    // 목록
    suspend fun fetchTodos(page: Int): List<Todo> {
        return mainApiService.fetchTodos(page).data ?: emptyList()
    }

    // 추가
    suspend fun addATodo(title: String): Todo? {
        return mainApiService.addATodo(title).data
    }

    // 삭제
    suspend fun deletedTodos(id: Int): Todo? {
        return mainApiService.deletedTodos(id).data
    }
}
