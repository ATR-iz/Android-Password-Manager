package com.atriztech.passwordmanager.view.fragmentlogin

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.atriztech.crypto_api.CryptoApi
import javax.inject.Inject

class LoginViewModel @Inject constructor(val crypto: CryptoApi): ViewModel() {
    var password = ObservableField<String>("")
    var key = "jksfhjhkjHFKJDSALH234DSKJFH234RKSDF"

    fun checkPassword(passKey: String): Boolean{
        var key2 = crypto.decode(password.get()!!, passKey)

        return key2 == key
    }

}