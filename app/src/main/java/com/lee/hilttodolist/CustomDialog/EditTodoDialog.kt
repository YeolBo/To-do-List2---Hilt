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
import kotlinx.android.synthetic.main.edit_todo_dialog.*

class EditTodoDialog(
    context: Context,
    iDialog: IDialog
): Dialog(context), View.OnClickListener {

    private var iDialog: IDialog? = null

    init{
        this.iDialog = iDialog
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_todo_dialog)

        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        cancel_btn.setOnClickListener(this)
        input_btn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v){
            input_btn -> {
                Log.d(TAG, "등록 버튼 클릭")
                val userInput = user_input.text.toString()
                this.iDialog?.addATodos(userInput)
            }
            cancel_btn -> {
                Log.d(TAG, "취소 버튼 클릭")
                dismiss()
            }
        }
    }
}