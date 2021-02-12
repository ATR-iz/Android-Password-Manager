package com.atriztech.passwordmanager.view.fragmentlistgroups

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.atriztech.crypto_api.CryptoApi
import com.atriztech.passwordmanager.model.database.GroupWithItemDB
import com.atriztech.passwordmanager.model.entity.GroupEntity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class ListGroupsViewModel @Inject constructor(var db: GroupWithItemDB, val crypto: CryptoApi): ViewModel() {
    var listGroup = MutableLiveData<List<GroupEntity>>()
    var newGroup = MutableLiveData<GroupEntity>()
    var password: String = ""

    fun getDataFromDB(){
        GlobalScope.launch {
            var list = db.itemDao().getGroups()
            for(item in list){
                item.name = crypto.decode(password, item.name)
                item.url = crypto.decode(password, item.url)
            }

            listGroup.postValue(list)
        }
    }

    fun updateGroupToDB(group: GroupEntity){
        GlobalScope.launch {
            var tmpGroup = GroupEntity(name = group.name, url = group.url)
            tmpGroup.id = group.id

            tmpGroup.name = crypto.encode(password, group.name)
            tmpGroup.url = crypto.encode(password, group.url)

            db.itemDao().insert(tmpGroup)
        }
    }

    fun deleteGroupFromDB(group: GroupEntity){
        GlobalScope.launch {
            group.name = crypto.encode(password, group.name)
            group.url = crypto.encode(password, group.url)

            var listItems = db.itemDao().getItemsFromGroup(group.id)

            for(item in listItems){
                db.itemDao().delete(item.item)
            }

            db.itemDao().deleteGroupId(group.id)
        }
    }

    fun addGroupToDB(group: GroupEntity){
        GlobalScope.launch {
            group.name = crypto.encode(password, group.name)
            group.url = crypto.encode(password, group.url)

            val id = db.itemDao().insert(group)
            var tmpGroup = db.itemDao().getGroupId(id)

            tmpGroup.name = crypto.decode(password, group.name)
            tmpGroup.url = crypto.decode(password, group.url)

            newGroup.postValue(tmpGroup)
        }
    }
}