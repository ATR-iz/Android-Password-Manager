package com.atriztech.passwordmanager.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.atriztech.passwordmanager.model.database.GroupWithItemDB
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class MainViewModel(): ViewModel() {
    var status = MutableLiveData<String>()

    fun getTestDataFromDB(db: GroupWithItemDB){
        status.postValue("Loading")
        Observable.fromCallable {

            var test = db.itemDao().getItem(1)

            if (test == null){
                status.postValue("First")
            } else {
                status.postValue(test.password)
            }

        }.subscribeOn(Schedulers.io())
            .subscribe()
    }
}