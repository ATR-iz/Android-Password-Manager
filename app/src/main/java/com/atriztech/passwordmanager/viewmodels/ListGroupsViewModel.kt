package com.atriztech.passwordmanager.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.atriztech.passwordmanager.crypto.Decoding
import com.atriztech.passwordmanager.crypto.Encoding
import com.atriztech.passwordmanager.model.database.GroupWithItemDB
import com.atriztech.passwordmanager.model.entity.GroupEntity
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class ListGroupsViewModel(var db: GroupWithItemDB, var password: String): ViewModel() {
    var listGroup = MutableLiveData<List<GroupEntity>>()
    var newGroup = MutableLiveData<GroupEntity>()

    fun getDataFromDB(){
        Observable.fromCallable {
            var list = db.itemDao().getGroups()
            for(item in list){
                item.name = Decoding.decode(password, item.name)
                item.url = Decoding.decode(password, item.url)
            }

            listGroup.postValue(list)
        }.subscribeOn(Schedulers.io())
            .subscribe()
    }

    fun updateGroupToDB(group: GroupEntity){
        Observable.fromCallable {
            var tmpGroup = GroupEntity(name = group.name, url = group.url)
            tmpGroup.id = group.id

            tmpGroup.name = Encoding.encode(password, group.name)
            tmpGroup.url = Encoding.encode(password, group.url)

            db.itemDao().insert(tmpGroup)
        }.subscribeOn(Schedulers.io())
            .subscribe()
    }

    fun deleteGroupFromDB(group: GroupEntity){
        Observable.fromCallable {
            group.name = Encoding.encode(password, group.name)
            group.url = Encoding.encode(password, group.url)

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
            group.name = Encoding.encode(password, group.name)
            group.url = Encoding.encode(password, group.url)

            val id = db.itemDao().insert(group)
            var tmpGroup = db.itemDao().getGroupId(id)

            tmpGroup.name = Decoding.decode(password, group.name)
            tmpGroup.url = Decoding.decode(password, group.url)

            newGroup.postValue(tmpGroup)
        }.subscribeOn(Schedulers.io())
            .subscribe()
    }
}