package com.atriztech.passwordmanager.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.atriztech.crypto_api.CryptoApi
import com.atriztech.passwordmanager.model.database.GroupWithItemDB
import com.atriztech.passwordmanager.model.entity.GroupEntity
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ListGroupsViewModel @Inject constructor(var db: GroupWithItemDB, val crypto: CryptoApi): ViewModel() {
    var listGroup = MutableLiveData<List<GroupEntity>>()
    var newGroup = MutableLiveData<GroupEntity>()
    var password: String = ""

    fun getDataFromDB(){
        Observable.fromCallable {
            var list = db.itemDao().getGroups()
            for(item in list){
                item.name = crypto.decode(password, item.name)
                item.url = crypto.decode(password, item.url)
            }

            listGroup.postValue(list)
        }.subscribeOn(Schedulers.io())
            .subscribe()
    }

    fun updateGroupToDB(group: GroupEntity){
        Observable.fromCallable {
            var tmpGroup = GroupEntity(name = group.name, url = group.url)
            tmpGroup.id = group.id

            tmpGroup.name = crypto.encode(password, group.name)
            tmpGroup.url = crypto.encode(password, group.url)

            db.itemDao().insert(tmpGroup)
        }.subscribeOn(Schedulers.io())
            .subscribe()
    }

    fun deleteGroupFromDB(group: GroupEntity){
        Observable.fromCallable {
            group.name = crypto.encode(password, group.name)
            group.url = crypto.encode(password, group.url)

            var listItems = db.itemDao().getItemsFromGroup(group.id)

            for(item in listItems){
                db.itemDao().delete(item.item)
            }

            db.itemDao().deleteGroupId(group.id)
        }.subscribeOn(Schedulers.io())
            .subscribe()
    }

    fun addGroupToDB(group: GroupEntity){
        Observable.fromCallable {
            group.name = crypto.encode(password, group.name)
            group.url = crypto.encode(password, group.url)

            val id = db.itemDao().insert(group)
            var tmpGroup = db.itemDao().getGroupId(id)

            tmpGroup.name = crypto.decode(password, group.name)
            tmpGroup.url = crypto.decode(password, group.url)

            newGroup.postValue(tmpGroup)
        }.subscribeOn(Schedulers.io())
            .subscribe()
    }
}