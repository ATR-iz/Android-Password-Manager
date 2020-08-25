package com.atriztech.passwordmanager.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.atriztech.passwordmanager.R
import com.atriztech.passwordmanager.databinding.ActivityListItemsBinding
import com.atriztech.passwordmanager.model.database.GroupWithItemDB
import com.atriztech.passwordmanager.model.entity.GroupEntity
import com.atriztech.passwordmanager.model.entity.ItemGroupEntity
import com.atriztech.passwordmanager.view.recyclerviewadapters.ListItemsDelegate
import com.atriztech.passwordmanager.view.recyclerviewadapters.ListItemsRecyclerViewAdapter
import com.atriztech.passwordmanager.viewmodel.ListItemsViewModel

class ListItemsActivity : AppCompatActivity() {
    private lateinit var binding : ActivityListItemsBinding
    private val recyclerView = ListItemsRecyclerViewAdapter()
    lateinit var viewModel: ListItemsViewModel
    private lateinit var group: GroupEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val password = intent?.extras?.get("password") as String
        group = intent?.extras?.get("group") as GroupEntity

        val db = Room.databaseBuilder(this, GroupWithItemDB::class.java, "db").build()

        viewModel = ListItemsViewModel(db, password)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_list_items)
        binding.viewModel = viewModel
        binding.executePendingBindings()

        recyclerView.attachDelegate(object: ListItemsDelegate {
            override fun OpenItem(itemGroup: ItemGroupEntity) {
                openItem(itemGroup = itemGroup)
            }

            override fun EditItem(itemGroup: ItemGroupEntity) {
                editItem(itemGroup = itemGroup)
            }
        })

        binding.listItems.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        binding.listItems.adapter = recyclerView

        viewModel.getDataFromDB(group)

        viewModel.listItems.observe(this, Observer {
                items -> recyclerView.setData(items)
        })

        viewModel.newItem.observe(this, Observer {
                items -> recyclerView.InsertItem(items)
        })

    }

    fun addNewItem(view: View){
        val intent = Intent(applicationContext, ItemActivity::class.java)
        intent.putExtra("code", 1)
        intent.putExtra("group", group)
        startActivityForResult(intent, 1)
    }

    private fun editItem(itemGroup: ItemGroupEntity){
        val intent = Intent(applicationContext, ItemActivity::class.java)
        intent.putExtra("code", 2)
        intent.putExtra("item", itemGroup)
        startActivityForResult(intent, 2)
    }

    private fun openItem(itemGroup: ItemGroupEntity){
        val intent = Intent(applicationContext, ItemActivity::class.java)
        intent.putExtra("code", 3)
        intent.putExtra("item", itemGroup)
        startActivityForResult(intent, 3)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1){
            when(resultCode){
                ActivityPostCode.SAVE_ITEM -> {
                    val item = data?.extras?.get("item") as ItemGroupEntity
                    viewModel.addItemToDB(item)
                }
            }
        } else if(requestCode == 2){
            if (resultCode == ActivityPostCode.SAVE_ITEM){
                val item =  data?.extras?.get("item") as ItemGroupEntity
                viewModel.updateItemFromDB(item)
                recyclerView.UpdateItem(item)
            } else if (resultCode == ActivityPostCode.DELETE_ITEM){
                val item =  data?.extras?.get("item") as ItemGroupEntity
                viewModel.deleteItemFromDB(item)
                recyclerView.DeleteItem(item)
            }
        }
    }
}