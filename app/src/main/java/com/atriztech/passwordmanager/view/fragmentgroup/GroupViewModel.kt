package com.atriztech.passwordmanager.view.fragmentgroup

import android.graphics.Bitmap
import android.os.Bundle
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.atriztech.image_api.ImageApi
import com.atriztech.passwordmanager.model.DirImage
import com.atriztech.passwordmanager.model.entity.GroupEntity
import com.atriztech.passwordmanager.view.FragmentState
import kotlinx.coroutines.*
import javax.inject.Inject

class GroupViewModel @Inject constructor(var dir: DirImage, var image: ImageApi): ViewModel() {
    var group = ObservableField<GroupEntity>(GroupEntity(name = "", url = ""))
    var url: String = ""
    var old_url = ""
    var code = 0
    private var fragmentState: FragmentState

    init {
        fragmentState = FragmentState.Cancel()
    }

    fun convertImageAndSave(bitmap: Bitmap): String{
        val shortPath = image.createImageCache(bitmap, dir.pathImage)

        if (url != "") {
            if (old_url != ""){
                image.deleteImage(dir.pathImage + "/" + url)
            } else{
                old_url = url
            }
        }

        url = shortPath

        return dir.pathImage + "/" + url
    }

    fun createBundleForDelete(): Bundle{
        fragmentState = FragmentState.Delete()
        val bundle = Bundle()
        bundle.putInt("code", code)
        group.get()?.url = url
        bundle.putSerializable("group", group.get())

        return bundle
    }

    fun createBundleForSave(): Bundle{
        fragmentState = FragmentState.Save()
        var bundle = Bundle()
        bundle.putInt("code", code)
        group.get()?.url = url
        bundle.putSerializable("group", group.get())

        return bundle
    }

    fun deleteImage(){
        when(fragmentState){
            is FragmentState.Save -> {
                if (old_url != "")
                    image.deleteImage("${dir.pathImage}/${old_url}")
            }
            is FragmentState.Delete -> {
                if (group.get()?.url != "")
                    image.deleteImage("${dir.pathImage}/${url}")
                if (old_url != "")
                    image.deleteImage("${dir.pathImage}/${old_url}")
            }
            is FragmentState.Cancel -> {
                if (group.get()?.id == 0.toLong() && url != "")
                    image.deleteImage("${dir.pathImage}/${url}")
                if (group.get()?.id == 0.toLong() && old_url != "")
                    image.deleteImage("${dir.pathImage}/${old_url}")
                if (old_url != "")
                    image.deleteImage("${dir.pathImage}/${url}")
            }
        }
    }
}