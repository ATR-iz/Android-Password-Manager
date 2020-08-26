package com.atriztech.passwordmanager.model

import android.os.Environment
import java.io.File

class Dir {
    companion object {

        val homeDir = Environment.getExternalStorageDirectory().toString() + "/PasswordManager"
        val imageDir = "$homeDir/Image"

        fun createHomeDir(ifDirExistsDelete: Boolean): String{
            createDir(homeDir, ifDirExistsDelete)
            return homeDir
        }

        fun createDir(path: String, ifDirExistsDelete: Boolean): String{
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

        fun deleteDir(path: String){
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
}