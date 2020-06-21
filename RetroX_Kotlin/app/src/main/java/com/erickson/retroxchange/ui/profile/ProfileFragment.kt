package com.erickson.retroxchange.ui.profile

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.erickson.retroxchange.R
import com.erickson.retroxchange.databinding.ProfileFragmentBinding
import com.erickson.retroxchange.manager.CameraManager
import com.erickson.retroxchange.ui.notifications.NotificationsViewModel
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_profile.*
import java.io.File
import java.io.IOException
import java.nio.file.Files.createFile
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class ProfileFragment : DaggerFragment() {

    private val CHOOSE_IMAGE_REQUEST = 1
    private val REQUEST_IMAGE_CAPTURE = 2
    private var imageBitmap: Bitmap? = null
    private var mCurrentPhotoPath: String? = null

    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: ProfileFragmentBinding

    lateinit var profileViewModel: ProfileViewModel

    private val cameraManager: CameraManager = CameraManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        profileViewModel = ViewModelProviders.of(this, viewModelFactory).get(ProfileViewModel::class.java)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)

        binding.profileImage.setOnClickListener {
           // chooseImage(it)
//            startActivityForResult(cameraManager.chooseImage(it), CHOOSE_IMAGE_REQUEST)
            startActivityForResult(cameraManager.takePicture(context!!), REQUEST_IMAGE_CAPTURE)
        }

        return binding.root
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == CHOOSE_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null){
            val bitmap = tryReadBitmap(data.data!!)
            bitmap?.let {
                imageBitmap = bitmap
                profile_image.setImageBitmap(bitmap)

                //TODO add database code
            }
        }
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK){
//            val auxFile = File(mCurrentPhotoPath)
            var bitmap :Bitmap = BitmapFactory.decodeFile(cameraManager.getFilePath().value)
                profile_image.setImageBitmap(bitmap)

            //TODO add database code

        }
    }

    private fun tryReadBitmap(data: Uri): Bitmap? {
        return try {
            MediaStore.Images.Media.getBitmap(context?.contentResolver, data)
        }
        catch (e: IOException){
            e.printStackTrace()
            null
        }
    }
}
