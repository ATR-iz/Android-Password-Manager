package com.atriztech.passwordmanager.view.fragmentlistitems

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
import com.atriztech.passwordmanager.service.di.App
import com.atriztech.passwordmanager.R
import com.atriztech.passwordmanager.databinding.ListItemsFragmentBinding
import com.atriztech.passwordmanager.model.entity.GroupEntity
import com.atriztech.passwordmanager.model.entity.ItemGroupEntity
import com.atriztech.passwordmanager.view.fragmentlistitems.ListItemsDelegate
import com.atriztech.passwordmanager.view.fragmentlistitems.ListItemsRecyclerViewAdapter
import javax.inject.Inject

class ListItemsFragment : Fragment() {
    private lateinit var binding: ListItemsFragmentBinding
    private lateinit var group: GroupEntity

    @Inject lateinit var viewModel: ListItemsViewModel
    @Inject lateinit var recyclerView: ListItemsRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (!this::binding.isInitialized ) {
            App.component()!!.inject(this)

            setFragmentResultListener("Save") { _, bundle ->
                saveItem(bundle)
            }

            setFragmentResultListener("Delete") { _, bundle ->
                deleteItem(bundle)
            }

            group = requireArguments().getSerializable("group") as GroupEntity
            viewModel.password = requireArguments().getString("password")!!

            binding = DataBindingUtil.inflate(inflater, R.layout.list_items_fragment, container, false);
            binding.viewModel = viewModel
            binding.fragment = this

            recyclerView.attachDelegate(object: ListItemsDelegate {
                override fun OpenItem(itemGroup: ItemGroupEntity) {
                    openItem(itemGroup = itemGroup)
                }

                override fun EditItem(itemGroup: ItemGroupEntity) {
                    editItem(itemGroup = itemGroup)
                }
            })

            binding.listItems.layoutManager = LinearLayoutManager(this.requireContext(), LinearLayoutManager.VERTICAL, false)
            binding.listItems.adapter = recyclerView

            viewModel.getDataFromDB(group)

            viewModel.listItems.observe(this.requireActivity(), Observer {
                    items -> recyclerView.setData(items)
            })

            viewModel.newItem.observe(this.requireActivity(), Observer {
                    items -> recyclerView.InsertItem(items)
            })

            return binding.root
        } else {
            return binding.root
        }
    }

    fun saveItem(bundle: Bundle){
        val code =  bundle.getInt("code")
        val item = bundle.getSerializable("item") as ItemGroupEntity

        when (code){
            1 -> viewModel.addItemToDB(item)
            2 -> {
                viewModel.updateItemFromDB(item)
                recyclerView.UpdateItem(item)
            }
        }
    }

    fun deleteItem(bundle: Bundle){
        val item = bundle.getSerializable("item") as ItemGroupEntity
        viewModel.deleteItemFromDB(item)
        recyclerView.DeleteItem(item)
    }

    fun addNewItem(){
        val bundle = Bundle().apply {
            putInt("code", 1)
            putSerializable("group", group)
        }

        this.findNavController().navigate(R.id.action_list_items_fragment_to_item_fragment, bundle)
    }

    private fun editItem(itemGroup: ItemGroupEntity){
        val bundle = Bundle().apply {
            putInt("code", 2)
            putSerializable("item", itemGroup)
        }

        this.findNavController().navigate(R.id.action_list_items_fragment_to_item_fragment, bundle)
    }

    private fun openItem(itemGroup: ItemGroupEntity){
        val bundle = Bundle().apply {
            putInt("code", 3)
            putSerializable("item", itemGroup)
        }

        this.findNavController().navigate(R.id.action_list_items_fragment_to_item_fragment, bundle)
    }
}