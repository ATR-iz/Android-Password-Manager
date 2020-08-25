package com.atriztech.passwordmanager.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.atriztech.passwordmanager.R
import com.atriztech.passwordmanager.databinding.ActivityLoginBinding
import com.atriztech.passwordmanager.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity() {
    private lateinit var mainBinding : ActivityLoginBinding
    var viewModel = LoginViewModel()
    var passKey: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        passKey = intent?.extras?.getString("pass_key").toString()

        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        mainBinding.viewModel = viewModel
        mainBinding.executePendingBindings()

    }

    fun confirmPassword(view: View){
        if (viewModel.checkPassword(passKey)){
            startListItemsActivity()
        } else {
            Toast.makeText(this, "Пароль введен неверно", Toast.LENGTH_SHORT).show()
        }
    }

    fun startListItemsActivity(){
        val intent = Intent(applicationContext, ListGroupsActivity::class.java)
        intent.putExtra("password", viewModel.password.get())
        startActivity(intent)
        this.finish()
    }
}