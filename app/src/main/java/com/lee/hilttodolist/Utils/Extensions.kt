package com.lee.hilttodolist.Utils

// 문자열이 json 형태인지
fun String?.isJsonObject(): Boolean {
    return this?.startsWith("{") == true && this.endsWith("}")
}

// 문자열이 json 배열 형태인지
fun String?.isJsonArray(): Boolean {
    return this?.startsWith("[") == true && this.endsWith("]")
}