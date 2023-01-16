package com.lee.hilttodolist.RecyclerViewHolder

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.lee.hilttodolist.Interface.RVInterface
import com.lee.hilttodolist.Utils.Constants.TAG
import com.lee.mytodolist.Model.Todo
import kotlinx.android.synthetic.main.main_todo_list_item.view.*

class RVTodoHolder(
    itemView: View,
    rvInterface: RVInterface,
    var onItemClicked: (Int) -> Unit
): RecyclerView.ViewHolder(itemView), View.OnClickListener {

    private var todoList = itemView.todo_list

    private var todo: Todo? = null
    
    var  rvInterface: RVInterface? = null

    init {
        this.rvInterface = rvInterface
        this.itemView.deleted_btn.setOnClickListener(this)
    }

    // 뷰가 데이터에 묶이기 전에 보낼 데이터
    fun bindWithView(list: Todo){
        todoList.text = list.title
        this.todo = list
    }

    override fun onClick(v: View?) {
        when(v){
            this.itemView.deleted_btn -> {
                Log.d(TAG, "삭제 클릭")
                
                todo?.id?.let {
                    onItemClicked(it)
                }?: Log.d(TAG, "id 없음")
            }
        }
    }
}