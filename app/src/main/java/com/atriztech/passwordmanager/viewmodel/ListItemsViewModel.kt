package com.atriztech.passwordmanager.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.atriztech.passwordmanager.crypto.Decoding
import com.atriztech.passwordmanager.crypto.Encoding
import com.atriztech.passwordmanager.model.database.GroupWithItemDB
import com.atriztech.passwordmanager.model.entity.GroupEntity
import com.atriztech.passwordmanager.model.entity.ItemEntity
import com.atriztech.passwordmanager.model.entity.ItemGroupEntity
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class ListItemsViewModel(var db: GroupWithItemDB, val password: String): ViewModel() {
    var listItems = MutableLiveData<List<ItemGroupEntity>>()
    var newItem = MutableLiveData<ItemGroupEntity>()

    fun getDataFromDB(group: GroupEntity){
        Observable.fromCallable {
            var list = db.itemDao().getItemsFromGroup(group.id)

            if (list.isNotEmpty()){
                list[0].group.name = Decoding.decode(password, list[0].group.name)
                list[0].group.url = Decoding.decode(password, list[0].group.url)
            }

            for(item in list){
                item.item.name = Decoding.decode(password, item.item.name)
                item.item.password = Decoding.decode(password, item.item.password!!)
            }

            listItems.postValue(list)
        }.subscribeOn(Schedulers.io())
            .subscribe()
    }

    fun addItemToDB(itemGroup: ItemGroupEntity){
        Observable.fromCallable {
            itemGroup.item.name = Encoding.encode(password, itemGroup.item.name)
            itemGroup.item.password = Encoding.encode(password, itemGroup.item.password!!)
            itemGroup.group.name = Encoding.encode(password, itemGroup.group.name)
            itemGroup.group.url = Encoding.encode(password, itemGroup.group.url)

            var id = db.itemDao().insert(itemGroup)
            var tmpItemGroup = db.itemDao().getItemGroup(id)

            tmpItemGroup.item.name = Decoding.decode(password, tmpItemGroup.item.name)
            tmpItemGroup.item.password = Decoding.decode(password, tmpItemGroup.item.password!!)
            tmpItemGroup.group.name = Decoding.decode(password, tmpItemGroup.group.name)
            tmpItemGroup.group.url = Decoding.decode(password, tmpItemGroup.group.url)

            newItem.postValue(tmpItemGroup)
        }.subscribeOn(Schedulers.io())
            .subscribe()
    }

    fun updateItemFromDB(itemGroup: ItemGroupEntity){
        Observable.fromCallable {
            var tmpItemGroup = ItemGroupEntity(
                ItemEntity(name = itemGroup.item.name, password = itemGroup.item.password, idGroup = itemGroup.item.id),
                GroupEntity(name = itemGroup.group.name, url = itemGroup.group.url)
            )
            tmpItemGroup.item.id = itemGroup.item.id
            tmpItemGroup.group.id = itemGroup.group.id

            tmpItemGroup.item.name = Encoding.encode(password, tmpItemGroup.item.name)
            tmpItemGroup.item.password = Encoding.encode(password, tmpItemGroup.item.password!!)
            tmpItemGroup.group.name = Encoding.encode(password, tmpItemGroup.group.name)
            tmpItemGroup.group.url = Encoding.encode(password, tmpItemGroup.group.url)

            db.itemDao().insert(tmpItemGroup)
        }.subscribeOn(Schedulers.io())
            .subscribe()
    }

    fun deleteItemFromDB(itemGroup: ItemGroupEntity){
        Observable.fromCallable {
            db.itemDao().deleteItemId(itemGroup.item.id)
        }.subscribeOn(Schedulers.io())
            .subscribe()
    }
}