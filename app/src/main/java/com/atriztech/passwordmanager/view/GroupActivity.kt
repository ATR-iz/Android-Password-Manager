package com.atriztech.passwordmanager.view

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.atriztech.passwordmanager.R
import com.atriztech.passwordmanager.databinding.ActivityGroupBinding
import com.atriztech.passwordmanager.model.Dir
import com.atriztech.passwordmanager.model.entity.GroupEntity
import com.atriztech.passwordmanager.viewmodel.GroupViewModel
import java.io.File

class GroupActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGroupBinding
    private lateinit var viewModel: GroupViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = GroupViewModel()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_group)
        binding.viewModel = viewModel
        binding.executePendingBindings()

        var code = intent?.extras?.get("code") as Int

        if(code == 1){
            binding.deleteItem.visibility = View.INVISIBLE
        } else if(code == 2){
            var group = intent?.extras?.get("group") as GroupEntity
            viewModel.group.set(group)
            binding.deleteItem.visibility = View.VISIBLE
            binding.groupImage.setImageURI(Uri.parse(Dir.homeDirOnMemory + "/" +  viewModel.group.get()!!.url))
        }
    }

    fun deleteGroup(view: View){
        val newIntent = Intent()
        newIntent.putExtra("group", viewModel.group.get())
        setResult(ActivityPostCode.DELETE_ITEM, newIntent)
        this.finish()
    }

    fun saveGroup(view: View){
        val newIntent = Intent()
        newIntent.putExtra("group", viewModel.group.get())
        newIntent.putExtra("old_url", viewModel.old_url)
        setResult(ActivityPostCode.SAVE_ITEM, newIntent)
        this.finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val newIntent = Intent()
        setResult(ActivityPostCode.CANCEL, newIntent)
        finish()
    }

    fun openPicture(view: View){
        val intent = Intent(applicationContext, ImageActivity::class.java)
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1){
            if (resultCode == ActivityPostCode.SAVE_ITEM){
                val group = viewModel.group.get()!!
                viewModel.old_url = group.url
                group.url = data?.extras?.getString("imagePath") as String

                viewModel.group.set(group)
                binding.groupImage.setImageURI(Uri.parse(Dir.homeDirOnMemory + "/" + group.url))
            }
        }
    }
}