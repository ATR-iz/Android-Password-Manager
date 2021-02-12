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
import com.atriztech.file_manager_api.DirApi
import com.atriztech.image_api.ImageApi
import com.atriztech.passwordmanager.service.di.App
import com.atriztech.passwordmanager.R
import com.atriztech.passwordmanager.databinding.GroupFragmentBinding
import com.atriztech.passwordmanager.model.DirImage
import com.atriztech.passwordmanager.model.entity.GroupEntity
import com.atriztech.passwordmanager.view.FragmentState
import kotlinx.coroutines.*
import javax.inject.Inject

class GroupFragment : Fragment() {
    private lateinit var binding: GroupFragmentBinding
    private var code: Int = 0

    private lateinit var fragmentState: FragmentState

    @Inject lateinit var viewModel: GroupViewModel
    @Inject lateinit var dir: DirImage
    @Inject lateinit var image: ImageApi

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (!this::binding.isInitialized ) {
            fragmentState = FragmentState.Cancel()
            App.component()!!.inject(this)

            binding = DataBindingUtil.inflate(inflater, R.layout.group_fragment, container, false);
            binding.viewModel = viewModel
            binding.fragment = this

            code = requireArguments().getInt("code")
            if(code == 1){
                binding.deleteItem.visibility = View.INVISIBLE
            } else if(code == 2){
                val group = requireArguments().getSerializable("group") as GroupEntity
                viewModel.url = group.url
                viewModel.group.set(group)
                binding.deleteItem.visibility = View.VISIBLE
                binding.groupImage.setImageURI(Uri.parse(dir.pathImage + "/" +  viewModel.group.get()!!.url))
            }
        }

        return binding.root
    }

    fun deleteGroup(){
        fragmentState = FragmentState.Delete()
        var bundle = Bundle()
        bundle.putInt("code", code)
        viewModel.group.get()?.url = viewModel.url
        bundle.putSerializable("group", viewModel.group.get())
        setFragmentResult("Delete", bundle)
        requireActivity().onBackPressed()
        //parentFragmentManager.popBackStack()
        //setResult(ActivityPostCode.DELETE_ITEM, newIntent)
    }

    fun saveGroup(){
        fragmentState = FragmentState.Save()
        var bundle = Bundle()
        bundle.putInt("code", code)
        viewModel.group.get()?.url = viewModel.url
        bundle.putSerializable("group", viewModel.group.get())
        setFragmentResult("Save", bundle)
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
            val shortPath = async { image.createImageCache(bitmap, dir.pathImage) }.await()

            if (viewModel.url != "") {
                if (viewModel.old_url != ""){
                    image.deleteImage(dir.pathImage + "/" + viewModel.url)
                } else{
                    viewModel.old_url = viewModel.url
                }
            }

            viewModel.url = shortPath
            binding.groupImage.setImageURI(Uri.parse(dir.pathImage + "/" + viewModel.url))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        when(fragmentState){
            is FragmentState.Save -> {
                if (viewModel.old_url != "")
                    image.deleteImage("${dir.pathImage}/${viewModel.old_url}")
            }
            is FragmentState.Delete -> {
                if (viewModel.group.get()?.url != "")
                    image.deleteImage("${dir.pathImage}/${viewModel.url}")
                if (viewModel.old_url != "")
                    image.deleteImage("${dir.pathImage}/${viewModel.old_url}")
            }
            is FragmentState.Cancel -> {
                if (viewModel.group.get()?.id == 0.toLong() && viewModel.url != "")
                    image.deleteImage("${dir.pathImage}/${viewModel.url}")
                if (viewModel.group.get()?.id == 0.toLong() && viewModel.old_url != "")
                    image.deleteImage("${dir.pathImage}/${viewModel.old_url}")
                if (viewModel.old_url != "")
                    image.deleteImage("${dir.pathImage}/${viewModel.url}")
            }
        }
    }
}