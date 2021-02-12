package com.atriztech.passwordmanager.view.fragmentgroup

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
import com.atriztech.image_api.ImageApi
import com.atriztech.passwordmanager.service.di.App
import com.atriztech.passwordmanager.R
import com.atriztech.passwordmanager.databinding.GroupFragmentBinding
import com.atriztech.passwordmanager.model.entity.GroupEntity
import kotlinx.coroutines.*
import javax.inject.Inject

class GroupFragment : Fragment() {

    companion object{
        const val CODE = "code"
        const val GROUP = "group"
        const val SAVE = "save"
        const val DELETE = "delete"

        fun bundleFor(code: Int): Bundle {
            return Bundle().apply {
                putInt(CODE, code)
            }
        }

        fun bundleFor(code: Int, group: GroupEntity): Bundle {
            return Bundle().apply {
                putInt(CODE, code)
                putSerializable(GROUP, group)
            }
        }
    }

    private lateinit var binding: GroupFragmentBinding
    @Inject lateinit var viewModel: GroupViewModel
    @Inject lateinit var image: ImageApi

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (!this::binding.isInitialized ) {
            App.component()!!.inject(this)

            binding = DataBindingUtil.inflate(inflater, R.layout.group_fragment, container, false);
            binding.viewModel = viewModel
            binding.fragment = this

            viewModel.code = requireArguments().getInt(CODE)
            if(viewModel.code == 1){
                binding.deleteItem.visibility = View.INVISIBLE
            } else if(viewModel.code == 2){
                val group = requireArguments().getSerializable(GROUP) as GroupEntity
                viewModel.url = group.url
                viewModel.group.set(group)
                binding.deleteItem.visibility = View.VISIBLE
                binding.groupImage.setImageURI(Uri.parse(viewModel.dir.pathImage + "/" +  viewModel.group.get()!!.url))
            }
        }

        return binding.root
    }

    fun deleteGroup(){
        setFragmentResult(DELETE, viewModel.createBundleForDelete())
        requireActivity().onBackPressed()
        //parentFragmentManager.popBackStack()
        //setResult(ActivityPostCode.DELETE_ITEM, newIntent)
    }

    fun saveGroup(){
        setFragmentResult(SAVE, viewModel.createBundleForSave())
        requireActivity().onBackPressed()
        //parentFragmentManager.popBackStack()
        //this.findNavController().navigate(R.id.action_group_fragment_to_list_groups_fragment, bundle)
        //setResult(ActivityPostCode.SAVE_ITEM, newIntent)
    }

    fun openPicture(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        this.requireActivity().startActivityFromFragment(this, Intent.createChooser(intent, "Select Picture"), 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1){
            if (resultCode == Activity.RESULT_OK){
                createImageCacheAndReplaceImagePath(data)
            }
        }
    }

    private fun createImageCacheAndReplaceImagePath(data: Intent?){
        GlobalScope.launch(Dispatchers.Main) {
            val bitmap: Bitmap = async { MediaStore.Images.Media.getBitmap(activity?.contentResolver, data?.data) }.await()
            val path = viewModel.convertImageAndSave(bitmap)
            binding.groupImage.setImageURI(Uri.parse(path))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.deleteImage()
    }
}