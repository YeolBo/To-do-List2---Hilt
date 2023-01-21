package com.lee.hilttodolist

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.lee.hilttodolist.CustomDialog.DeletedTodoDialog
import com.lee.hilttodolist.CustomDialog.EditTodoDialog
import com.lee.hilttodolist.Interface.IDialog
import com.lee.hilttodolist.Interface.RVInterface
import com.lee.hilttodolist.RecyclerViewAdapter.RVTodoAdapter
import com.lee.hilttodolist.Utils.Constants.TAG
import com.lee.hilttodolist.Utils.textChangesToFlow
import com.lee.hilttodolist.ViewModel.TodoViewModel
import com.lee.mytodolist.Model.Todo
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.edit_todo_dialog.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), RVInterface, IDialog {

    // 뷰모델 가져오기
    val todosViewModel: TodoViewModel by viewModels()

    // 커스텀 다이얼로그 생성
    private var editDialog: EditTodoDialog? = null
    private var deletedDialog: DeletedTodoDialog? = null

    // 어답터로 넘겨줄 데이터를 담을 리스트
    var todoList = ArrayList<Todo>()

    private lateinit var searchInput: TextInputEditText

    // 어답터 생성
    private lateinit var rvTodoAdapter: RVTodoAdapter

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.searchInput = findViewById(R.id.search_todo)

        lifecycleScope
            .launch(context = Dispatchers.IO) {
                todosViewModel.searchTermTest = searchInput
                    .textChangesToFlow() // Flow<CharSequence?>
                    .debounce(2000) // Flow<CharSequence?>
                    .map { it.toString() } // Flow<String>
            }

        // 리사이클러뷰 설정
        this.rvTodoAdapter = RVTodoAdapter(this,
            onItemClicked = {
                deletedDialog = DeletedTodoDialog(this, this, it)
                deletedDialog?.apply {
                    setCancelable(false)
                    show()
                }
            })

        // 리사이클러뷰 레이아웃 설정
        val layoutManager = GridLayoutManager(
            this,
            1,
            GridLayoutManager.VERTICAL,
            false
        )
        main_todo_recycler_view.layoutManager = layoutManager

        // 위에서 생성한 어답터를 설정한 레이아웃에 장착
        main_todo_recycler_view.adapter = this.rvTodoAdapter

        // 스크롤을 감지해서 페이지 추가
        main_todo_recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                Log.d(TAG, "onScrolled / dy: $dy")

                // progressbar 확인을 위한 딜레이 추가
                if (layoutManager.findLastCompletelyVisibleItemPosition() == todoList.size - 1) {
                    Handler(Looper.getMainLooper()).postDelayed({
                        todosViewModel.pageCountUp()
                    }, 1000)
                }
            }
        })

        lifecycleScope.launch {
            // started 라이프사이클을 타면 자동으로 반복해서 구독처리 설정
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                // 각각 launch 스코프를 동해 각각 flow 구독 처리
                launch {
                    todosViewModel.currentPage.collect {

                    }
                }

                launch {
                    todosViewModel.todos.collect { todoList ->
                        // 할일 추가 버튼
                        edit_todo_btn.setOnClickListener {
                            Log.d(TAG, "추가 버튼 클릭")
                            editDialog = EditTodoDialog(this@MainActivity, this@MainActivity)
                            editDialog?.apply {
                                setCancelable(false)
                                show()
                            }
                        }

                        this@MainActivity.todoList = ArrayList(todoList)
                        // 어답터에 리스트 보냄
                        this@MainActivity.rvTodoAdapter.submitList(ArrayList(todoList))
                        // 어답터에게 데이터가 변경되었다고 알림
                        this@MainActivity.rvTodoAdapter.notifyDataSetChanged()
                    }
                }

                launch {
                    todosViewModel.isLoading.collect {
                        if (it) {
                            todo_loading_progress_bar.visibility = View.VISIBLE
                        } else {
                            todo_loading_progress_bar.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    // 삭제 버튼 클릭
    override fun deletedBtn(id: Int) {
        todosViewModel.deletedTodos(id)
        deletedDialog?.dismiss()
    }

    // 할일 추가
    override fun addATodos(title: String) {
        if (title.length >= 6) {
            todosViewModel.addATodos(title)
            Toast.makeText(
                this@MainActivity,
                "할일이 성공적으로 추가되었습니다.",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(
                this@MainActivity,
                "6글자 이상 입력해주세요!",
                Toast.LENGTH_SHORT
            ).show()
        }
        editDialog?.dismiss()
    }
}