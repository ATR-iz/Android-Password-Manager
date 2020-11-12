package com.atriztech.passwordmanager.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.atriztech.file_manager_api.DirApi
import com.atriztech.passwordmanager.service.di.App
import com.atriztech.passwordmanager.R
import com.atriztech.passwordmanager.databinding.ListGroupsFragmentBinding
import com.atriztech.passwordmanager.model.entity.GroupEntity
import com.atriztech.passwordmanager.view.recyclerviewadapters.ListGroupsDelegate
import com.atriztech.passwordmanager.view.recyclerviewadapters.ListGroupsRecyclerViewAdapter
import com.atriztech.passwordmanager.viewmodels.ListGroupsViewModel
import java.io.File
import javax.inject.Inject

class ListGroupsFragment: Fragment() {
    private lateinit var binding: ListGroupsFragmentBinding
    private var password = ""

    @Inject
    lateinit var viewModel: ListGroupsViewModel

    @Inject
    lateinit var recyclerView: ListGroupsRecyclerViewAdapter

    @Inject
    lateinit var dir: DirApi

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (!retainInstance ) {
            retainInstance = true

            App.component()!!.inject(this)

            setFragmentResultListener("Save") { key, bundle ->
                saveGroup(bundle)
            }

            setFragmentResultListener("Delete") { key, bundle ->
                deleteGroup(bundle)
            }

            password = requireArguments().getString("password")!!
            viewModel.password = password

            binding = DataBindingUtil.inflate(inflater, R.layout.list_groups_fragment, container, false);
            binding.viewModel = viewModel
            binding.fragment = this

            recyclerView.attachDelegate(object: ListGroupsDelegate {
                override fun OpenGroup(itemGroup: GroupEntity) {
                    openGroup(itemGroup = itemGroup)
                }

                override fun EditGroup(itemGroup: GroupEntity) {
                    editGroup(itemGroup = itemGroup)
                }
            })

            binding.listGroups.layoutManager = LinearLayoutManager(this.requireContext(), LinearLayoutManager.VERTICAL, false)
            binding.listGroups.adapter = recyclerView

            viewModel.listGroup.observe(this.requireActivity(), Observer {
                    items -> recyclerView.setData(items)
            })

            viewModel.newGroup.observe(this.requireActivity(), Observer {
                    item -> recyclerView.InsertItem(item)
            })

            viewModel.getDataFromDB()

            return binding.root
        } else {
            return binding.root
        }
    }

    private fun saveGroup(bundle: Bundle){
        val code =  bundle.getInt("code")
        if (code == 1){
            val group =  bundle.getSerializable("group") as GroupEntity
            group.url = moveImageToOriginalDir(group.url)

            viewModel.addGroupToDB(group)
        } else if(code == 2){
            val group =  bundle.getSerializable("group") as GroupEntity
            val oldPicturePath =  bundle.getString("old_url") as String

            if(oldPicturePath != "") File(dir.applicationPath + "/" + oldPicturePath).delete()

            group.url = moveImageToOriginalDir(group.url)

            viewModel.updateGroupToDB(group)
            recyclerView.UpdateItem(group)
        }
    }

    private fun deleteGroup(bundle: Bundle){
        val group =  bundle.getSerializable("group") as GroupEntity

        if(group.url != ""){
            File(dir.applicationPath + "/" + group.url).delete()
        }

        viewModel.deleteGroupFromDB(group)
        recyclerView.DeleteItem(group)
    }

    private fun moveImageToOriginalDir(tmpPath: String): String{
        var originalPath = tmpPath

        if(tmpPath.indexOf("tmp_image") >= 0){
            originalPath = tmpPath.replace("tmp_image", "image")

            File(dir.applicationPath + "/" + tmpPath).let { sourceFile ->
                sourceFile.copyTo(File(dir.applicationPath + "/" + originalPath))
                sourceFile.delete()
            }
        }

        return originalPath
    }

    private fun openGroup(itemGroup: GroupEntity){
        var bundle = Bundle()
        bundle.putString("password", password)
        bundle.putSerializable("group", itemGroup)
        this.findNavController().navigate(R.id.action_list_groups_fragment_to_list_items_fragment, bundle)
    }

    fun addNewGroup(view: View){
        var bundle = Bundle()
        bundle.putInt("code", 1)
        this.findNavController().navigate(R.id.action_list_groups_fragment_to_group_fragment, bundle)
    }

    private fun editGroup(itemGroup: GroupEntity){
        var bundle = Bundle()
        bundle.putInt("code", 2)
        bundle.putSerializable("group", itemGroup)
        this.findNavController().navigate(R.id.action_list_groups_fragment_to_group_fragment, bundle)
    }
}