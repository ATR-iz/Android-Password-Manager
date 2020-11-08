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
import androidx.room.Room
import com.atriztech.passwordmanager.R
import com.atriztech.passwordmanager.databinding.ListItemsFragmentBinding
import com.atriztech.passwordmanager.model.database.GroupWithItemDB
import com.atriztech.passwordmanager.model.entity.GroupEntity
import com.atriztech.passwordmanager.model.entity.ItemGroupEntity
import com.atriztech.passwordmanager.view.recyclerviewadapters.ListItemsDelegate
import com.atriztech.passwordmanager.view.recyclerviewadapters.ListItemsRecyclerViewAdapter
import com.atriztech.passwordmanager.viewmodels.ListItemsViewModel

class ListItemsFragment : Fragment() {
    private lateinit var binding: ListItemsFragmentBinding
    private lateinit var viewModel: ListItemsViewModel
    private val recyclerView = ListItemsRecyclerViewAdapter()
    private lateinit var group: GroupEntity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (!retainInstance ) {
            retainInstance = true

            setFragmentResultListener("Save") { key, bundle ->
                saveItem(bundle)
            }

            setFragmentResultListener("Delete") { key, bundle ->
                deleteItem(bundle)
            }

            val password = requireArguments().getString("password")
            group = requireArguments().getSerializable("group") as GroupEntity

            val db = Room.databaseBuilder(this.requireContext(), GroupWithItemDB::class.java, "db").build()

            viewModel = ListItemsViewModel(db, password!!)
            binding = DataBindingUtil.inflate(inflater,
                R.layout.list_items_fragment, container, false);
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

        if (code == 1) {
            val item = bundle.getSerializable("item") as ItemGroupEntity
            viewModel.addItemToDB(item)
        } else if (code == 2) {
            val item =  bundle.getSerializable("item") as ItemGroupEntity
            viewModel.updateItemFromDB(item)
            recyclerView.UpdateItem(item)
        }
    }

    fun deleteItem(bundle: Bundle){
        val item = bundle.getSerializable("item") as ItemGroupEntity
        viewModel.deleteItemFromDB(item)
        recyclerView.DeleteItem(item)
    }

    fun addNewItem(view: View){
        var bundle = Bundle()
        bundle.putInt("code", 1)
        bundle.putSerializable("group", group)
        this.findNavController().navigate(R.id.action_list_items_fragment_to_item_fragment, bundle)
    }

    private fun editItem(itemGroup: ItemGroupEntity){
        var bundle = Bundle()
        bundle.putInt("code", 2)
        bundle.putSerializable("item", itemGroup)
        this.findNavController().navigate(R.id.action_list_items_fragment_to_item_fragment, bundle)
    }

    private fun openItem(itemGroup: ItemGroupEntity){
        var bundle = Bundle()
        bundle.putInt("code", 3)
        bundle.putSerializable("item", itemGroup)
        this.findNavController().navigate(R.id.action_list_items_fragment_to_item_fragment, bundle)
    }
}