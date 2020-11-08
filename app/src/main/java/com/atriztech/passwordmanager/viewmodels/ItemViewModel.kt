package com.atriztech.passwordmanager.viewmodels

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.atriztech.passwordmanager.model.entity.GroupEntity
import com.atriztech.passwordmanager.model.entity.ItemEntity
import com.atriztech.passwordmanager.model.entity.ItemGroupEntity

class ItemViewModel: ViewModel() {
    var item = ObservableField<ItemGroupEntity>(ItemGroupEntity(ItemEntity(name = "", password = "", idGroup = 0), GroupEntity(name = "", url = "")))
//
//    fun setItem(newItem: ItemGroupEntity, password: String){
//        newItem.item.password = Decoding.decode(password, newItem.item.password!!)
//        item.set(newItem)
//    }
//
//    fun getItem(password: String): ItemGroupEntity{
//        var newItem = item.get()!!
//        newItem.item.password = Encoding.encode(password, newItem.item.password!!)
//        return newItem
//    }
//

}