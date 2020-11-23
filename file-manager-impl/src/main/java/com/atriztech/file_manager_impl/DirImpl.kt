package com.atriztech.file_manager_impl

import com.atriztech.file_manager_api.DirApi
import java.io.File

class DirImpl() : DirApi {
    override var applicationPath: String? = null

    override fun createDir(path: String, ifDirExistsDelete: Boolean): String {
        val file = File(path)

        if (file.exists()) {
            if (ifDirExistsDelete) {
                file.deleteRecursively()
                createNomediaDir(file)
            }
        } else {
            createNomediaDir(file)
        }

        return path
    }

    override fun deleteDir(path: String) {
        val file = File(path)
        if (file.exists()) {
            file.deleteRecursively()
        }
    }

    private fun createNomediaDir(file: File){
        file.mkdir()
        File(file.path + "/.nomedia").createNewFile()
    }
}