package com.atriztech.passwordmanager.view

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.setFragmentResult
import com.atriztech.passwordmanager.R
import com.atriztech.passwordmanager.databinding.GroupFragmentBinding
import com.atriztech.passwordmanager.model.Dir
import com.atriztech.passwordmanager.model.entity.GroupEntity
import com.atriztech.passwordmanager.viewmodels.GroupViewModel

class GroupFragment : Fragment() {
    private lateinit var binding: GroupFragmentBinding
    private lateinit var viewModel: GroupViewModel
    private var code: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (!retainInstance ) {
            retainInstance = true

            viewModel = GroupViewModel()
            binding = DataBindingUtil.inflate(inflater, R.layout.group_fragment, container, false);
            binding.viewModel = viewModel
            binding.fragment = this

            code = requireArguments().getInt("code")!!
            if(code == 1){
                binding.deleteItem.visibility = View.INVISIBLE
            } else if(code == 2){
                var group = requireArguments().getSerializable("group") as GroupEntity
                viewModel.group.set(group)
                binding.deleteItem.visibility = View.VISIBLE
                binding.groupImage.setImageURI(Uri.parse(Dir.homeDirOnMemory + "/" +  viewModel.group.get()!!.url))
            }

            return binding.root
        } else {
            return binding.root
        }
    }

    fun deleteGroup(view: View){
        var bundle = Bundle()
        bundle.putInt("code", code)
        bundle.putSerializable("group", viewModel.group.get())
        setFragmentResult("Delete", bundle)
        requireActivity().onBackPressed()
        //parentFragmentManager.popBackStack()
        //setResult(ActivityPostCode.DELETE_ITEM, newIntent)
    }

    fun saveGroup(view: View){
        var bundle = Bundle()
        bundle.putInt("code", code)
        bundle.putSerializable("group", viewModel.group.get())
        bundle.putString("old_url", viewModel.old_url)
        setFragmentResult("Save", bundle)
        requireActivity().onBackPressed()
        //parentFragmentManager.popBackStack()
        //this.findNavController().navigate(R.id.action_group_fragment_to_list_groups_fragment, bundle)
        //setResult(ActivityPostCode.SAVE_ITEM, newIntent)
    }

    fun openPicture(view: View){
        //val intent = Intent(this.context, ImageActivity::class.java)
        //startActivityForResult(intent, 1)
    }
}