package com.atriztech.passwordmanager.viewmodels

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.atriztech.crypto_api.CryptoApi
import com.atriztech.passwordmanager.model.database.GroupWithItemDB
import com.atriztech.passwordmanager.model.entity.ItemEntity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class NewPasswordViewModel @Inject constructor(val db: GroupWithItemDB, val crypto: CryptoApi): ViewModel() {
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

    fun addNewKeyToDB(){
        GlobalScope.launch {
            var passKey = crypto.encode(password.get()!!, key)

            var item = ItemEntity(name = "primary_key", password = passKey, idGroup = -1)
            item.id = 1

            db.itemDao().insert(item)
        }
    }
}