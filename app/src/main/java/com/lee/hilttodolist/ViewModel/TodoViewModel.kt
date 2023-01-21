package com.lee.hilttodolist.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lee.hilttodolist.MainRepository
import com.lee.hilttodolist.Utils.Constants.TAG
import com.lee.mytodolist.Model.Todo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    // 데이터 리스트
    private val _todos = MutableStateFlow<List<Todo>>(emptyList())

    // UI가 상태 업데이트를 가져옴
    val todos: StateFlow<List<Todo>> = _todos

    // 현재 페이지
    private val _currentPage = MutableStateFlow(1)
    val currentPage: StateFlow<Int> = _currentPage

    // 리로드
    val reloadAction = MutableSharedFlow<Unit>()

    // 삭제된 것
    private val _deletedTodo = MutableSharedFlow<Todo>()
    val deletedTodo: SharedFlow<Todo> = _deletedTodo


    // 로딩 여부
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading


    private val _searchTerm = MutableSharedFlow<String>()
    var searchTermTest: Flow<String> = emptyFlow()

    init {
        viewModelScope.launch {
            launch {
                _currentPage.collect { changedPage ->
                    Log.d(TAG, "TodoListViewModel - changedPage : $changedPage")
                    // api 호출
                    fetchTodos()
                }
            }
            launch {
                reloadAction.collect {
                    Log.d(TAG, "ReviseTextViewModel - reloadAction")
                    refreshDataApiCall()
                }
            }
            // 쓰레드 관련
            launch(Dispatchers.IO) {
                searchTermTest.collectLatest {
                    Log.d(TAG, "TodoViewModel - searchTerm: $it")
                    //TODO: 1. 검색 API 호출하기
                    //TODO: 2. 응답에 따라 리스트 데이터 변경
                }
            }
        }
    }

    // 페이지수를 1씩 올려준다
    fun pageCountUp() {
        _currentPage.value = _currentPage.value + 1
    }

    // 목록
    private fun fetchTodos() {
        viewModelScope.launch {
            _isLoading.emit(true)
        }
        viewModelScope.launch {
            mainRepository.fetchTodos(page = _currentPage.value).map {
                _todos.value = _todos.value + it
            }
        }
        viewModelScope.launch {
            _isLoading.emit(false)
        }
    }

    // 삭제
    fun deletedTodos(id: Int) {
        viewModelScope.launch {
            mainRepository.deletedTodos(id)?.let { deletedTodo ->
                // 삭제된거 필터링해서 데이터 변경
                _todos.value = _todos.value.filter { it.id != deletedTodo.id }
                // 삭제된 녀석 보내기
                _deletedTodo.emit(deletedTodo)
            }
        }
    }

    private fun refreshDataApiCall() {
        _currentPage.value = 1
        viewModelScope.launch {
            mainRepository.fetchTodos(page = _currentPage.value).map {
                _todos.value = _todos.value + it
            }
        }
    }
    // 할일 추가
    fun addATodos(title: String){
        viewModelScope.launch {
            mainRepository.addATodo(title)?.let {
                val updatedTodos : MutableList<Todo> = _todos.value.toMutableList()
                updatedTodos.add(0, it)
                _todos.emit(updatedTodos)
                updateATodo(it)
            }
        }
    }

    fun updateATodo(deletedTodo: Todo) {
        val todoIndex = _todos.value.indexOfFirst {
            it.id == deletedTodo.id
        }

        // 수정을 위해 담을 그릇
        val existingTodos = _todos.value.toMutableList()

        // 갈아끼움
        existingTodos[todoIndex] = deletedTodo

        // 전체적으로 수정
        _todos.value = existingTodos
    }
}