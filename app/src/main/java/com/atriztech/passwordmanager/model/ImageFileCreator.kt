package com.atriztech.passwordmanager.model

import android.R.id.mask
import android.graphics.Bitmap
import android.graphics.Canvas
import java.io.File
import java.io.FileOutputStream


class ImageFileCreator {
    companion object{
        fun createImageCache(bitmap: Bitmap): String {
            Dir.createHomeDir(false)
            val imageDir = Dir.createDir(Dir.imageDir, false)

            val image = File.createTempFile("cache_", ".jpg", File(imageDir))
            val stream = FileOutputStream(image)

            var bmp = bitmap
            bmp = cutImage(bmp)
            bmp.compress(Bitmap.CompressFormat.JPEG, 50, stream)

            stream.flush()
            stream.close()

            return File(imageDir).name + "/" + image.name
        }

        private fun cutImage(bmp: Bitmap): Bitmap{
            val imageNewSize = 320

            var newBmp = if (bmp.width >= bmp.height) {
                Bitmap.createBitmap(bmp, bmp.width / 2 - bmp.height / 2, 0, bmp.height, bmp.height)
            } else {
                Bitmap.createBitmap(bmp, 0, bmp.height / 2 - bmp.width / 2, bmp.width, bmp.width)
            }

            if (imageNewSize < bmp.width) {
                val newMinSize = (bmp.width / (bmp.width.toDouble() / imageNewSize)).toInt() //Расчет коэффициента уменьшения изображения
                newBmp = Bitmap.createScaledBitmap(
                    bmp,
                    newMinSize,
                    newMinSize,
                    false
                )
            }

            return newBmp
        }
    }
}