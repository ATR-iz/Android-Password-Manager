package com.atriztech.passwordmanager.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.setFragmentResult
import com.atriztech.passwordmanager.R
import com.atriztech.passwordmanager.databinding.ItemFragmentBinding
import com.atriztech.passwordmanager.model.entity.GroupEntity
import com.atriztech.passwordmanager.model.entity.ItemEntity
import com.atriztech.passwordmanager.model.entity.ItemGroupEntity
import com.atriztech.passwordmanager.viewmodels.ItemViewModel

class ItemFragment : Fragment() {
    private lateinit var binding: ItemFragmentBinding
    private lateinit var viewModel: ItemViewModel
    private var code: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (!retainInstance ) {
            retainInstance = true

            viewModel = ItemViewModel()
            binding = DataBindingUtil.inflate(inflater, R.layout.item_fragment, container, false);
            binding.viewModel = viewModel
            binding.fragment = this

            code = requireArguments().getInt("code")

            when(code){
                1 -> {
                    //title = "Create item"
                    binding.deleteItem.visibility = View.INVISIBLE
                    binding.itemGroupName.isEnabled = false
                    val group = requireArguments().getSerializable("group") as GroupEntity
                    viewModel.item.set(ItemGroupEntity(ItemEntity(name = "", password = "", idGroup = group.id), group))
                }
                2 -> {
                    //title = "Edit item"
                    binding.deleteItem.visibility = View.VISIBLE
                    val item = requireArguments().getSerializable("item") as ItemGroupEntity
                    viewModel.item.set(item)
                }
                3-> {
                    //title = "View item"
                    val item = requireArguments().getSerializable("item") as ItemGroupEntity
                    viewModel.item.set(item)

                    binding.deleteItem.visibility = View.INVISIBLE
                    binding.saveItem.visibility = View.INVISIBLE

                    binding.itemGroupName.isEnabled = false
                    binding.itemName.isEnabled = false
                    binding.itemPassword.isEnabled = false
                }
            }

            return binding.root
        } else {
            return binding.root
        }
    }

    fun saveItem(view: View){
        var bundle = Bundle()
        bundle.putInt("code", code)
        bundle.putSerializable("item", viewModel.item.get())
        setFragmentResult("Save", bundle)
        requireActivity().onBackPressed()
    }

    fun deleteItem(view: View){
        var bundle = Bundle()
        bundle.putInt("code", code)
        bundle.putSerializable("item", viewModel.item.get())
        setFragmentResult("Delete", bundle)
        requireActivity().onBackPressed()
    }
}