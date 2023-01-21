package com.lee.hilttodolist.Interface

import com.lee.hilttodolist.Utils.API
import com.lee.mytodolist.Model.DeletedTodoResponse
import com.lee.mytodolist.Model.Todo
import com.lee.mytodolist.Model.TodoResponse
import com.lee.mytodolist.Model.TodosResponse
import retrofit2.Response
import retrofit2.http.*

interface MainApiService {

    // 모든 할 일 목록을 가져온다.
    @GET(API.BASE_URL + "todos")
    suspend fun fetchTodos(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int = 20
    ): TodosResponse<Todo>

    // 할일을 추가하고 추가된 할일을 반환한다.
    @Multipart
    @POST(API.BASE_URL + "todos")
    suspend fun addATodo(
        @Part("title") title: String,
        @Part("is_done") isDone: Boolean = false
    ): TodoResponse<Todo>

    // 기존 할일을 수정하고 수정된 포스팅을 반환한다.
    @FormUrlEncoded
    @PUT(API.BASE_URL + "todos/{id}")
    suspend fun reviseList(
        @Path("id") id: Int,
        @Field("title") title: String
    ): DeletedTodoResponse<Todo>

    // 기존 할일을 삭제하고 수정된 포스팅을 반환한다.
    @DELETE(API.BASE_URL + "todos/{todoId}")
    suspend fun deletedTodos(@Path("todoId") id: Int): DeletedTodoResponse<Todo>
}
