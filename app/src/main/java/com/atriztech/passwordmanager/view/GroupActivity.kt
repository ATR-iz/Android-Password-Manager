package com.atriztech.passwordmanager.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.atriztech.passwordmanager.R
import com.atriztech.passwordmanager.databinding.ActivityGroupBinding
import com.atriztech.passwordmanager.model.entity.GroupEntity
import com.atriztech.passwordmanager.viewmodel.GroupViewModel

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
            binding.deleteItem.visibility = View.VISIBLE
            var group = intent?.extras?.get("group") as GroupEntity
            viewModel.group.set(group)
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
        setResult(ActivityPostCode.SAVE_ITEM, newIntent)
        this.finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val newIntent = Intent()
        setResult(ActivityPostCode.CANCEL, newIntent)
        finish()
    }
}