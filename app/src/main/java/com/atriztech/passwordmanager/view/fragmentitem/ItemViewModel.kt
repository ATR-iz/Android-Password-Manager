package com.atriztech.passwordmanager.view.fragmentitem

import android.os.Bundle
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.atriztech.passwordmanager.model.entity.GroupEntity
import com.atriztech.passwordmanager.model.entity.ItemEntity
import com.atriztech.passwordmanager.model.entity.ItemGroupEntity
import javax.inject.Inject

class ItemViewModel @Inject constructor(): ViewModel() {
    var code = 0
    var item = ObservableField<ItemGroupEntity>(ItemGroupEntity(ItemEntity(name = "", password = "", idGroup = 0), GroupEntity(name = "", url = "")))

    fun createBundleForSave(): Bundle{
       return Bundle().apply {
           putInt("code", code)
           putSerializable("item", item.get())
       }
    }

    fun createBundleForDelete(): Bundle{
        return Bundle().apply {
            putInt("code", code)
            putSerializable("item", item.get())
        }
    }

}