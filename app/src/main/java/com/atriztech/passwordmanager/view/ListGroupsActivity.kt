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
import com.atriztech.passwordmanager.databinding.ActivityListGroupsBinding
import com.atriztech.passwordmanager.model.Dir
import com.atriztech.passwordmanager.model.database.GroupWithItemDB
import com.atriztech.passwordmanager.model.entity.GroupEntity
import com.atriztech.passwordmanager.view.recyclerviewadapters.ListGroupsDelegate
import com.atriztech.passwordmanager.view.recyclerviewadapters.ListGroupsRecyclerViewAdapter
import com.atriztech.passwordmanager.viewmodel.ListGroupsViewModel
import java.io.File

class ListGroupsActivity : AppCompatActivity() {
    private lateinit var binding : ActivityListGroupsBinding
    private val recyclerView = ListGroupsRecyclerViewAdapter()
    lateinit var viewModel: ListGroupsViewModel
    private var password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        password = intent?.extras?.get("password") as String

        var db = Room.databaseBuilder(this, GroupWithItemDB::class.java, "db").build()

        viewModel = ListGroupsViewModel(db, password)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_list_groups)
        binding.viewModel = viewModel
        binding.executePendingBindings()

        recyclerView.attachDelegate(object: ListGroupsDelegate {
            override fun OpenGroup(itemGroup: GroupEntity) {
                openGroup(itemGroup = itemGroup)
            }

            override fun EditGroup(itemGroup: GroupEntity) {
                editGroup(itemGroup = itemGroup)
            }
        })

        binding.listGroups.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        binding.listGroups.adapter = recyclerView

        viewModel.listGroup.observe(this, Observer {
                items -> recyclerView.setData(items)
        })

        viewModel.newGroup.observe(this, Observer {
                item -> recyclerView.InsertItem(item)
        })

        viewModel.getDataFromDB()
    }

    private fun openGroup(itemGroup: GroupEntity){
        val intent = Intent(applicationContext, ListItemsActivity::class.java)
        intent.putExtra("password", password)
        intent.putExtra("group", itemGroup)
        startActivity(intent)
    }

    fun addNewGroup(view: View){
        val intent = Intent(applicationContext, GroupActivity::class.java)
        intent.putExtra("code", 1)
        startActivityForResult(intent, 1)
    }

    private fun editGroup(itemGroup: GroupEntity){
        val intent = Intent(applicationContext, GroupActivity::class.java)
        intent.putExtra("code", 2)
        intent.putExtra("group", itemGroup)
        startActivityForResult(intent, 2)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1){
            if (resultCode == ActivityPostCode.SAVE_ITEM){
                val group =  data?.extras?.get("group") as GroupEntity
                group.url = moveImageToOriginalDir(group.url)

                viewModel.addGroupToDB(group)
            }
        } else if(requestCode == 2){
            if (resultCode == ActivityPostCode.SAVE_ITEM){
                val group =  data?.extras?.get("group") as GroupEntity
                val oldPicturePath =  data?.extras?.get("old_url") as String

                if(oldPicturePath != "") File(Dir.homeDirOnMemory + "/" + oldPicturePath).delete()

                group.url = moveImageToOriginalDir(group.url)

                viewModel.updateGroupToDB(group)
                recyclerView.UpdateItem(group)
            } else if (resultCode == ActivityPostCode.DELETE_ITEM){
                val group =  data?.extras?.get("group") as GroupEntity

                if(group.url != ""){
                    File(Dir.homeDirOnMemory + "/" + group.url).delete()
                }

                viewModel.deleteGroupFromDB(group)
                recyclerView.DeleteItem(group)
            }
        }
    }

    private fun moveImageToOriginalDir(tmpPath: String): String{
        val originalPath = tmpPath.replace("tmp_image", "image")

        File(Dir.homeDirOnMemory + "/" + tmpPath).let { sourceFile ->
            sourceFile.copyTo(File(Dir.homeDirOnMemory + "/" + originalPath))
            sourceFile.delete()
        }

        return originalPath
    }
}