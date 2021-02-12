package com.atriztech.passwordmanager.model

import com.atriztech.file_manager_api.DirApi
import com.atriztech.file_manager_impl.DirImpl

class DirImage(path: String): DirApi, DirImpl(){
    val pathImage = "$path/image"
}