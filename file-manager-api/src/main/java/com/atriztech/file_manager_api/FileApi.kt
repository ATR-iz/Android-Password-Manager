package com.atriztech.file_manager_api

interface FileApi {
    fun read(): String
    fun create(jsonTxt: String): Boolean
    fun delete(): Boolean
}