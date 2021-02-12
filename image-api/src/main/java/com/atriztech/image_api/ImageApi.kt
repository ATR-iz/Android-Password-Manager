package com.atriztech.image_api

import android.graphics.Bitmap

interface ImageApi {
    fun createImageCache(bitmap: Bitmap, applicationPath: String): String
    fun deleteImage(path: String)
}