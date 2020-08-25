package com.atriztech.passwordmanager.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.atriztech.passwordmanager.R
import com.atriztech.passwordmanager.databinding.ActivityItemBinding
import com.atriztech.passwordmanager.model.entity.GroupEntity
import com.atriztech.passwordmanager.model.entity.ItemEntity
import com.atriztech.passwordmanager.model.entity.ItemGroupEntity
import com.atriztech.passwordmanager.viewmodel.ItemViewModel

class ItemActivity : AppCompatActivity() {
    private lateinit var binding: ActivityItemBinding
    private lateinit var viewModel: ItemViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ItemViewModel()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_item)
        binding.viewModel = viewModel
        binding.executePendingBindings()

        var code = intent?.extras?.get("code") as Int

        when(code){
            1 -> {
                binding.deleteItem.visibility = View.INVISIBLE
                binding.itemGroupName.isEnabled = false
                val group = intent?.extras?.get("group") as GroupEntity
                viewModel.item.set(ItemGroupEntity(ItemEntity(name = "", password = "", idGroup = group.id), group))
            }
            2 -> {
                binding.deleteItem.visibility = View.VISIBLE
                val item = intent?.extras?.get("item") as ItemGroupEntity
                viewModel.item.set(item)
            }
            3-> {
                val item = intent?.extras?.get("item") as ItemGroupEntity
                viewModel.item.set(item)

                binding.deleteItem.visibility = View.INVISIBLE
                binding.saveItem.visibility = View.INVISIBLE

                binding.itemGroupName.isEnabled = false
                binding.itemName.isEnabled = false
                binding.itemPassword.isEnabled = false
            }
        }
    }

    fun saveItem(view: View){
        val newIntent = Intent()
        newIntent.putExtra("item", viewModel.item.get())
        setResult(ActivityPostCode.SAVE_ITEM, newIntent)
        this.finish()
    }

    fun deleteItem(view: View){
        val newIntent = Intent()
        newIntent.putExtra("item", viewModel.item.get())
        setResult(ActivityPostCode.DELETE_ITEM, newIntent)
        this.finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val newIntent = Intent()
        setResult(ActivityPostCode.CANCEL, newIntent)
        finish()
    }
}