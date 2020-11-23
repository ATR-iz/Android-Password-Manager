package com.atriztech.crypto_api

interface CryptoApi {
    fun decode(password: String, passKey: String): String
    fun encode(password: String, passKey: String): String
}