package com.atriztech.passwordmanager.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.atriztech.passwordmanager.crypto.Encoding
import com.atriztech.passwordmanager.model.database.GroupWithItemDB
import com.atriztech.passwordmanager.model.entity.ItemEntity
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class NewPasswordViewModel: ViewModel() {
    var password = ObservableField<String>("")
    var password_confirm = ObservableField<String>("")
    var key: String = "jksfhjhkjHFKJDSALH234DSKJFH234RKSDF"

    fun comparePassword(): String{
        if (password.get()?.length!! < 4){
            return "short"
        } else if (password.get() != password_confirm.get()){
            return "not equal"
        } else {
            return "ok"
        }
    }

    fun addNewKeyToDB(db: GroupWithItemDB){
        Observable.fromCallable {
            var passKey = Encoding.encode(password.get()!!, key)

            var item = ItemEntity(name = "primary_key", password = passKey, idGroup = -1)
            item.id = 1

            db.itemDao().insert(item)
        }.subscribeOn(Schedulers.io())
            .subscribe()
    }
}