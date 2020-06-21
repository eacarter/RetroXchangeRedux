package com.erickson.retroxchange.manager

import android.app.ProgressDialog.show
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.FileProvider
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class CameraManager  constructor(){
    private val filePath = MediatorLiveData<String>()
    private var imageBitmap: Bitmap? = null
    private var mCurrentPhotoPath: String? = null

    fun getFilePath() : LiveData<String>{
        filePath.value = mCurrentPhotoPath
        return filePath
    }

    fun chooseImage(view: View) : Intent{
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        var chooser = Intent.createChooser(intent, "Choose image for item")
        return chooser
    }

    fun takePicture(context: Context): Intent {

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val file: File = createFile(context)

        val uri: Uri = FileProvider.getUriForFile(
            context!!,
            "com.retroXchange.android.fileprovider",
            file
        )
        intent.putExtra(MediaStore.EXTRA_OUTPUT,uri)

        return intent
    }

    private fun createFile(context: Context): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = context!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            mCurrentPhotoPath = absolutePath
        }
    }

//    private fun tryReadBitmap(data: Uri, context: Context): Bitmap? {
//        return try {
//            MediaStore.Images.Media.getBitmap(context.contentResolver, data)
//        }
//        catch (e: IOException){
//            e.printStackTrace()
//            null
//        }
//    }


}