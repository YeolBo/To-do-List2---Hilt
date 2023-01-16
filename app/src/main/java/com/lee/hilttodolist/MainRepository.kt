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
    suspend fun editTodos(title: String): List<Todo> {
        return mainApiService.editTodos(title).data ?: return emptyList()
    }

    // 삭제
    suspend fun deletedTodos(id: Int): List<Todo> {
        return mainApiService.deletedTodos(id).data ?: return emptyList()
    }
}
