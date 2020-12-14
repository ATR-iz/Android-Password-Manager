package com.atriztech.passwordmanager.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.atriztech.passwordmanager.model.database.GroupWithItemDB
import com.atriztech.passwordmanager.model.entity.ItemEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(val db: GroupWithItemDB): ViewModel() {
    var status = MutableLiveData<String>()

    fun getTestDataFromDB(){
        GlobalScope.launch(Dispatchers.IO) {
            val test: ItemEntity? = db.itemDao().getItem(1)

            if (test == null)
                status.postValue("First")
            else
                status.postValue(test.password)

        }
    }
}