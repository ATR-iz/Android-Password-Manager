package com.atriztech.passwordmanager.crypto

import android.util.Base64
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class Decoding {
    companion object{
        fun decode(password: String, passKey: String): String{

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
    }
}