package com.atriztech.file_manager_api

interface DirApi {
    fun createDir(path: String, ifDirExistsDelete: Boolean): String
    fun deleteDir(path: String)
}