package com.atriztech.passwordmanager.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.room.Room
import com.atriztech.passwordmanager.R
import com.atriztech.passwordmanager.model.Dir
import com.atriztech.passwordmanager.model.database.GroupWithItemDB
import com.atriztech.passwordmanager.viewmodels.MainViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createDirs()

        var viewModel = MainViewModel()
        viewModel.status.observe(this, Observer {
            passKey -> if(passKey == "First") startNewPasswordFragment()
                    else startLoginFragment(passKey)
        })

        var db = Room.databaseBuilder(this, GroupWithItemDB::class.java, "db").build()

        viewModel.getTestDataFromDB(db)
    }

    private fun startNewPasswordFragment(){
        findNavController(R.id.container).navigate(R.id.action_main_fragment_to_new_password_fragment, Bundle())
    }

    private fun startLoginFragment(passKey: String){
        var bundle = Bundle()
        bundle.putString("pass_key", passKey)
        findNavController(R.id.container).navigate(R.id.action_main_fragment_to_login_fragment, bundle)
    }

    private fun createDirs(){
        Dir.homeDirOnMemory = this.application.filesDir.absoluteFile.toString()
        Dir.createDir(Dir.homeDirOnMemory + "/image", false)
        Dir.createDir(Dir.homeDirOnMemory + "/tmp_image", true)
    }
}