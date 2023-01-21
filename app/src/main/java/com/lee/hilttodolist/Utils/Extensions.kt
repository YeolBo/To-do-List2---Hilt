package com.lee.hilttodolist.Utils

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

// 문자열이 json 형태인지
fun String?.isJsonObject(): Boolean {
    return this?.startsWith("{") == true && this.endsWith("}")
}

// 문자열이 json 배열 형태인지
fun String?.isJsonArray(): Boolean {
    return this?.startsWith("[") == true && this.endsWith("]")
}

// 에딧 텍스트에 대한 익스텐션
fun TextInputEditText.onTextChanged(changedText: (Editable?) -> Unit){
    this.addTextChangedListener(object: TextWatcher {

        override fun afterTextChanged(editable: Editable?) {
            changedText(editable)
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

    })
}

// 에딧텍스트 텍스트 변경을 flow로 받기
fun EditText.textChangesToFlow(): Flow<CharSequence?> {

    // flow 콜백 받기
    return callbackFlow<CharSequence?> {

        // 람다 콜백을 flow 로 보내는 리스너
        val listener = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

            override fun afterTextChanged(p0: Editable?) {
                Unit
            }
            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                Log.d(Constants.TAG, "onTextChanged() / textChangesToFlow() 에 달려있는 텍스트 와쳐 / text : $text")
                // 값 내보내기
                trySend(text)
            }
        }

        // 위에서 설정한 리스너 달아주기 - 에딧텍스트
        addTextChangedListener(listener)

        // 콜백이 사라질때 실행됨
        // 플로우 이벤트 발송 끝
        awaitClose {
            Log.d(Constants.TAG, "textChangesToFlow() awaitClose 실행")
            removeTextChangedListener(listener) // 리스너 지우기
        }
    }
}