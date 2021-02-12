package com.atriztech.file_manager_impl

import com.atriztech.file_manager_api.FileApi
import java.io.File
import java.lang.Exception

open class FileImpl(val path: String, val fileName: String): FileApi {
    override fun read(): String {
        return File(path, fileName).readText()
    }

    override fun create(jsonTxt: String): Boolean {
        delete()
        return try {
            File(path, fileName).printWriter().use {
                it.print(jsonTxt)
            }
            true
        } catch (e: Exception){
            false
        }
    }

    override fun delete(): Boolean {
        val status = File(path, fileName).delete()
        return status
    }
}