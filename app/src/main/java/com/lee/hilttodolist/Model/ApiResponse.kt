package com.lee.hilttodolist.Model

sealed class ApiResponse<out T> {
    object Loading : ApiResponse<Nothing>()
    data class Success<out T>(val data: T) : ApiResponse<T>()
    data class Failure(var errorMessage: String, val code: Int) : ApiResponse<Nothing>()
}

//// api 에러 처리
//sealed class ApiError {
//    data class AuthError(var msg: String = "인증되지 않은 사용자 입니다", var tokenRetryCount: Int = 2) :
//        ApiError()
//    data class NoContentError(var msg: String = "컨텐츠가 없습니다") : ApiError()
//    data class UnknownError(var msg: String = "알 수 없는 에러 입니다", var code: Int? = null) : ApiError()
//}
