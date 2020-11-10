package com.atriztech.passwordmanager.view

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.setFragmentResult
import com.atriztech.passwordmanager.R
import com.atriztech.passwordmanager.databinding.GroupFragmentBinding
import com.atriztech.passwordmanager.model.Dir
import com.atriztech.passwordmanager.model.ImageFileCreator
import com.atriztech.passwordmanager.model.entity.GroupEntity
import com.atriztech.passwordmanager.viewmodels.GroupViewModel
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

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
        }

        return binding.root
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
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        this.requireActivity().startActivityFromFragment(this, Intent.createChooser(intent, "Select Picture"), 1)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1){
            if (resultCode == Activity.RESULT_OK){
                Observable.fromCallable {

                    val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(this.requireActivity().contentResolver, data?.data)
                    val shortPath = ImageFileCreator.createImageCache(bitmap)

                    val group = viewModel.group.get()!!
                    viewModel.old_url = group.url
                    group.url = shortPath

                    viewModel.group.set(group)
                    binding.groupImage.setImageURI(Uri.parse(Dir.homeDirOnMemory + "/" + group.url))

                }.observeOn(Schedulers.computation())
                    .subscribe()
            }
        }
    }
}