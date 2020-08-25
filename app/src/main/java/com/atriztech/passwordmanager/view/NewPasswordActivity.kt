package com.atriztech.passwordmanager.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.room.Room
import com.atriztech.passwordmanager.R
import com.atriztech.passwordmanager.databinding.ActivityNewPasswordBinding
import com.atriztech.passwordmanager.model.database.GroupWithItemDB
import com.atriztech.passwordmanager.viewmodel.NewPasswordViewModel

class NewPasswordActivity : AppCompatActivity() {
    private lateinit var mainBinding : ActivityNewPasswordBinding
    var viewModel = NewPasswordViewModel()
    lateinit var db: GroupWithItemDB


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = Room.databaseBuilder(this, GroupWithItemDB::class.java, "db").build()

        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_new_password)
        mainBinding.viewModel = viewModel
        mainBinding.executePendingBindings()

    }

    fun confirmPassword(view: View){
        var status = viewModel.comparePassword()
        if(status== "ok"){
            viewModel.addNewKeyToDB(db)
            startListGroupsActivity(viewModel.password.get()!!)
        } else if (status == "short") {
            Toast.makeText(this, "Пароль слишком короткий. Минимальная длинна 4 символа.", Toast.LENGTH_SHORT).show()
        } else if (status == "not equal"){
            Toast.makeText(this, "Пароли не совпадают", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startListGroupsActivity(password: String){
        val intent = Intent(applicationContext, ListGroupsActivity::class.java)
        intent.putExtra("password", password)
        startActivity(intent)
        this.finish()
    }
}