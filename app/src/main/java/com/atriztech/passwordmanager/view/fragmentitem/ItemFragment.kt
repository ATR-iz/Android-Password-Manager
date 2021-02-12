package com.atriztech.passwordmanager.view.fragmentitem

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.setFragmentResult
import com.atriztech.passwordmanager.service.di.App
import com.atriztech.passwordmanager.R
import com.atriztech.passwordmanager.databinding.ItemFragmentBinding
import com.atriztech.passwordmanager.model.entity.GroupEntity
import com.atriztech.passwordmanager.model.entity.ItemEntity
import com.atriztech.passwordmanager.model.entity.ItemGroupEntity
import javax.inject.Inject

class ItemFragment : Fragment() {

    companion object{
        const val CODE = "code"
        const val ITEM = "item"
        const val GROUP = "group"
        const val SAVE = "save"
        const val DELETE = "delete"

        fun bundleFor(code: Int, group: GroupEntity): Bundle {
            return Bundle().apply {
                putInt(CODE, code)
                putSerializable(GROUP, group)
            }
        }

        fun bundleFor(code: Int, item: ItemGroupEntity): Bundle {
            return Bundle().apply {
                putInt(CODE, code)
                putSerializable(ITEM, item)
            }
        }
    }

    private lateinit var binding: ItemFragmentBinding
    @Inject lateinit var viewModel: ItemViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (!this::binding.isInitialized ) {
            App.component()!!.inject(this)

            binding = DataBindingUtil.inflate(inflater, R.layout.item_fragment, container, false);
            binding.viewModel = viewModel
            binding.fragment = this

            viewModel.code = requireArguments().getInt(CODE)

            when(viewModel.code){
                1 -> {
                    //title = "Create item"
                    binding.deleteItem.visibility = View.INVISIBLE
                    binding.itemGroupName.isEnabled = false
                    val group = requireArguments().getSerializable(GROUP) as GroupEntity
                    viewModel.item.set(ItemGroupEntity(ItemEntity(name = "", password = "", idGroup = group.id), group))
                }
                2 -> {
                    //title = "Edit item"
                    binding.deleteItem.visibility = View.VISIBLE
                    val item = requireArguments().getSerializable(ITEM) as ItemGroupEntity
                    viewModel.item.set(item)
                }
                3-> {
                    //title = "View item"
                    val item = requireArguments().getSerializable(ITEM) as ItemGroupEntity
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

    fun saveItem(){
        setFragmentResult(SAVE, viewModel.createBundleForSave())
        requireActivity().onBackPressed()
    }

    fun deleteItem(){
        setFragmentResult(DELETE, viewModel.createBundleForDelete())
        requireActivity().onBackPressed()
    }
}