package com.lee.hilttodolist.RecyclerViewAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lee.hilttodolist.Interface.RVInterface
import com.lee.hilttodolist.R
import com.lee.hilttodolist.RecyclerViewHolder.RVTodoHolder
import com.lee.mytodolist.Model.Todo

class RVTodoAdapter(
    private val rvInterface: RVInterface,
    var onItemClicked: (Int) -> Unit
): RecyclerView.Adapter<RVTodoHolder>() {

    // 리사이클러뷰에서 보여줄 아이템을 담을 리스트
    private var todoList = ArrayList<Todo>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVTodoHolder {
        return RVTodoHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.main_todo_list_item, parent, false),
            rvInterface,
            onItemClicked
        )
    }

    override fun onBindViewHolder(holder: RVTodoHolder, position: Int) {
        holder.bindWithView(this.todoList[position])
    }

    override fun getItemCount(): Int {
        return this.todoList.size
    }

    fun submitList(todoList: ArrayList<Todo>){
        this.todoList = todoList
    }

}