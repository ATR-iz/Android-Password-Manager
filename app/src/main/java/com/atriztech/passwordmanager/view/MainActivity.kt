package com.atriztech.passwordmanager.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.room.Room
import com.atriztech.passwordmanager.R
import com.atriztech.passwordmanager.model.Dir
import com.atriztech.passwordmanager.model.database.GroupWithItemDB
import com.atriztech.passwordmanager.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createDirs()

        var viewModel = MainViewModel()
        viewModel.status.observe(this, Observer {
            passKey -> if(passKey == "First") startNewPasswordScreen()
                    else startPasswordScreen(passKey)
        })

        var db = Room.databaseBuilder(this, GroupWithItemDB::class.java, "db").build()

        viewModel.getTestDataFromDB(db)
    }

    private fun startNewPasswordScreen(){
        val intent = Intent(applicationContext, NewPasswordActivity::class.java)
        startActivity(intent)
        this.finish()
    }

    private fun startPasswordScreen(passKey: String){
        val intent = Intent(applicationContext, LoginActivity::class.java)
        intent.putExtra("pass_key", passKey)
        startActivity(intent)
        this.finish()
    }

    private fun createDirs(){
        Dir.homeDirOnMemory = this.application.filesDir.absoluteFile.toString()
        Dir.createDir(Dir.homeDirOnMemory + "/image", false)
        Dir.createDir(Dir.homeDirOnMemory + "/tmp_image", true)
    }
}