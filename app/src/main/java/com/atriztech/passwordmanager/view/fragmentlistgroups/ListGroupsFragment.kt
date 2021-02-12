package com.atriztech.passwordmanager.view.fragmentlistgroups

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
import com.atriztech.passwordmanager.view.fragmentgroup.GroupFragment
import com.atriztech.passwordmanager.view.fragmentlistgroups.ListGroupsDelegate
import com.atriztech.passwordmanager.view.fragmentlistgroups.ListGroupsRecyclerViewAdapter
import com.atriztech.passwordmanager.view.fragmentlistitems.ListItemsFragment
import java.io.File
import javax.inject.Inject

class ListGroupsFragment: Fragment() {

    companion object{
        const val PASSWORD = "password"

        fun bundleFor(password: String): Bundle {
            val bundle = Bundle()
            bundle.putString(PASSWORD, password)
            return bundle
        }
    }

    private lateinit var binding: ListGroupsFragmentBinding
    private var password = ""

    @Inject lateinit var viewModel: ListGroupsViewModel
    @Inject lateinit var recyclerView: ListGroupsRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (!this::binding.isInitialized ) {
            App.component()!!.inject(this)

            setFragmentResultListener(GroupFragment.SAVE) { _, bundle ->
                saveGroup(bundle)
            }

            setFragmentResultListener(GroupFragment.DELETE) { _, bundle ->
                deleteGroup(bundle)
            }

            password = requireArguments().getString(PASSWORD)!!
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
        val code =  bundle.getInt(GroupFragment.CODE)
        val group =  bundle.getSerializable(GroupFragment.GROUP) as GroupEntity

        when(code) {
            1 -> viewModel.addGroupToDB(group)
            2 -> {
                viewModel.updateGroupToDB(group)
                recyclerView.UpdateItem(group)
            }
        }
    }

    private fun deleteGroup(bundle: Bundle){
        val group =  bundle.getSerializable(GroupFragment.GROUP) as GroupEntity
        viewModel.deleteGroupFromDB(group)
        recyclerView.DeleteItem(group)
    }

    private fun openGroup(itemGroup: GroupEntity) =
        findNavController().navigate(R.id.action_list_groups_fragment_to_list_items_fragment, ListItemsFragment.bundleFor(itemGroup, password))

    fun addNewGroup() =
        findNavController().navigate(R.id.action_list_groups_fragment_to_group_fragment, GroupFragment.bundleFor(1))

    private fun editGroup(itemGroup: GroupEntity) =
        this.findNavController().navigate(R.id.action_list_groups_fragment_to_group_fragment, GroupFragment.bundleFor(2, itemGroup))
}