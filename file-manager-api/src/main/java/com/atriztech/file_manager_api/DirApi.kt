package com.atriztech.file_manager_api

interface DirApi {
    var applicationPath: String?
    fun createDir(path: String, ifDirExistsDelete: Boolean): String
    fun deleteDir(path: String)
}