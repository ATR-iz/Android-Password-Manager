package com.atriztech.passwordmanager.view

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import com.atriztech.passwordmanager.R
import com.atriztech.passwordmanager.model.ImageFileCreator
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class ImageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)
        OpenPictureFromGallery()
    }

    private fun OpenPictureFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1){
            if (resultCode == Activity.RESULT_OK){
                Observable.fromCallable {

                    val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, data?.data)
                    val shortPath = ImageFileCreator.createImageCache(bitmap)

                    val intent = Intent()
                    intent.putExtra("imagePath", shortPath)
                    setResult(ActivityPostCode.SAVE_ITEM, intent)
                    finish()

                }.observeOn(Schedulers.computation())
                    .subscribe()
            } else if (resultCode == Activity.RESULT_CANCELED){
                finish()
            }

        }
    }
}