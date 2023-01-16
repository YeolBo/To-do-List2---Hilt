package com.lee.hilttodolist.CustomDialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import com.lee.hilttodolist.Interface.IDialog
import com.lee.hilttodolist.R
import com.lee.hilttodolist.Utils.Constants.TAG
import kotlinx.android.synthetic.main.deleted_todo_dialog.*
import kotlinx.android.synthetic.main.main_todo_list_item.*

class DeletedTodoDialog(
    context: Context,
    iDialog: IDialog,
    var id: Int
): Dialog(context), View.OnClickListener {

    private var iDialog: IDialog? = null

    init{
        this.iDialog = iDialog
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.deleted_todo_dialog)

        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        deleted_cancel_btn.setOnClickListener(this)
        dialog_deleted_btn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v){
            deleted_cancel_btn -> {
                Log.d(TAG, "취소 버튼 클릭")
                dismiss()
            }
            dialog_deleted_btn -> {
                Log.d(TAG, "삭제 버튼 클릭")
                this.iDialog?.deletedBtn(id)
            }
        }
    }
}