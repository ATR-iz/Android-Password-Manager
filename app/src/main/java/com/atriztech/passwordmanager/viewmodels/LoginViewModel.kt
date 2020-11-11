package com.atriztech.passwordmanager.viewmodels

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.atriztech.passwordmanager.crypto.Decoding
import javax.inject.Inject

class LoginViewModel @Inject constructor(): ViewModel() {
    var password = ObservableField<String>("")
    var key = "jksfhjhkjHFKJDSALH234DSKJFH234RKSDF"

    fun checkPassword(passKey: String): Boolean{
        var key2 = Decoding.decode(password.get()!!, passKey)

        return key2 == key
    }

}