package com.atriztech.crypto_impl

import android.util.Base64
import com.atriztech.crypto_api.CryptoApi
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class CryptoImpl: CryptoApi {
    override fun decode(password: String, passKey: String): String {
        var newPassword = password
        if (password.length < 16){
            for(i in password.length until 16){
                newPassword += "1"
            }
        }

        val secretKeySpec = SecretKeySpec(newPassword.toByteArray(), "AES")
        val iv = newPassword.toByteArray()
        val ivParameterSpec = IvParameterSpec(iv)

        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec)

        val decodedValue = Base64.decode(passKey, Base64.DEFAULT)
        var decryptedValue = ""
        try {
            decryptedValue = String(cipher.doFinal(decodedValue))
        } catch (e: BadPaddingException){
            decryptedValue = "error"
        }

        return decryptedValue
    }

    override fun encode(password: String, passKey: String): String {
        var newPassword = password
        if (password.length < 16){
            for(i in password.length until 16){
                newPassword += "1"
            }
        }

        val secretKeySpec = SecretKeySpec(newPassword.toByteArray(), "AES")
        val iv = newPassword.toByteArray()
        val ivParameterSpec = IvParameterSpec(iv)

        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec)

        val encryptedValue = cipher.doFinal(passKey.toByteArray())
        return Base64.encodeToString(encryptedValue, Base64.DEFAULT)
    }
}