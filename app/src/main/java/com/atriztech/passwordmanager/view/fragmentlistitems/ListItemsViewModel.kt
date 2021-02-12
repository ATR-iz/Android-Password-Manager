package com.atriztech.passwordmanager.view.fragmentlistitems

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.atriztech.crypto_api.CryptoApi
import com.atriztech.passwordmanager.model.database.GroupWithItemDB
import com.atriztech.passwordmanager.model.entity.GroupEntity
import com.atriztech.passwordmanager.model.entity.ItemEntity
import com.atriztech.passwordmanager.model.entity.ItemGroupEntity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class ListItemsViewModel @Inject constructor(var db: GroupWithItemDB, val crypto: CryptoApi): ViewModel() {
    var listItems = MutableLiveData<List<ItemGroupEntity>>()
    var newItem = MutableLiveData<ItemGroupEntity>()
    var password: String = ""

    fun getDataFromDB(group: GroupEntity){
        GlobalScope.launch {
            var list = db.itemDao().getItemsFromGroup(group.id)

            if (list.isNotEmpty()){
                list[0].group.name = crypto.decode(password, list[0].group.name)
                list[0].group.url = crypto.decode(password, list[0].group.url)
            }

            for(item in list){
                item.item.name = crypto.decode(password, item.item.name)
                item.item.password = crypto.decode(password, item.item.password!!)
            }

            listItems.postValue(list)
        }
    }

    fun addItemToDB(itemGroup: ItemGroupEntity){
        GlobalScope.launch {
            itemGroup.item.name = crypto.encode(password, itemGroup.item.name)
            itemGroup.item.password = crypto.encode(password, itemGroup.item.password!!)
            itemGroup.group.name = crypto.encode(password, itemGroup.group.name)
            itemGroup.group.url = crypto.encode(password, itemGroup.group.url)

            val id = db.itemDao().insert(itemGroup)
            val tmpItemGroup = db.itemDao().getItemGroup(id)

            itemGroup.item.id = tmpItemGroup.item.id
            itemGroup.item.idGroup = tmpItemGroup.item.idGroup
            itemGroup.group.id = tmpItemGroup.group.id

            itemGroup.item.name = crypto.decode(password, itemGroup.item.name)
            itemGroup.item.password = crypto.decode(password, itemGroup.item.password!!)
            itemGroup.group.name = crypto.decode(password, itemGroup.group.name)
            itemGroup.group.url = crypto.decode(password, itemGroup.group.url)

            newItem.postValue(itemGroup)
        }
    }

    fun updateItemFromDB(itemGroup: ItemGroupEntity){
        GlobalScope.launch {
            var tmpItemGroup = ItemGroupEntity(
                ItemEntity(name = itemGroup.item.name, password = itemGroup.item.password, idGroup = itemGroup.item.id),
                GroupEntity(name = itemGroup.group.name, url = itemGroup.group.url)
            )
            tmpItemGroup.item.id = itemGroup.item.id
            tmpItemGroup.group.id = itemGroup.group.id

            tmpItemGroup.item.name = crypto.encode(password, tmpItemGroup.item.name)
            tmpItemGroup.item.password = crypto.encode(password, tmpItemGroup.item.password!!)
            tmpItemGroup.group.name = crypto.encode(password, tmpItemGroup.group.name)
            tmpItemGroup.group.url = crypto.encode(password, tmpItemGroup.group.url)

            db.itemDao().insert(tmpItemGroup)
        }
    }

    fun deleteItemFromDB(itemGroup: ItemGroupEntity) = GlobalScope.launch {
            db.itemDao().deleteItemId(itemGroup.item.id)
        }
}