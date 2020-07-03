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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.erickson.retroxchange.R
import com.erickson.retroxchange.adapter.DiscussionAdapter
import com.erickson.retroxchange.databinding.ProfileFragmentBinding
import com.erickson.retroxchange.datamodels.DiscussionData
import com.erickson.retroxchange.manager.CameraManager
import com.erickson.retroxchange.ui.notifications.NotificationsViewModel
import com.google.firebase.auth.FirebaseUser
import com.squareup.picasso.Picasso
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.fragment_profile.*
import java.io.File
import java.io.IOException
import java.nio.file.Files.createFile
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class ProfileFragment : DaggerFragment() {

    private val CHOOSE_IMAGE_REQUEST = 1
    private val REQUEST_IMAGE_CAPTURE = 2
    private var imageBitmap: Bitmap? = null
    private var mCurrentPhotoPath: String? = null

    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    private var picasso: Picasso = Picasso.get()

    private lateinit var binding: ProfileFragmentBinding

    lateinit var profileViewModel: ProfileViewModel

    private val cameraManager: CameraManager = CameraManager()

    private var visible: Boolean = false

    private var userId: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)

        profileViewModel = ViewModelProviders.of(this, viewModelFactory).get(ProfileViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val lifecycleOwner: LifecycleOwner = this

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)

        binding.profileImage.setOnClickListener {
            if(visible){
                binding.photoCamera.visibility = View.GONE
                binding.photoGallery.visibility = View.GONE
                visible = false
            }
            else{
                binding.photoCamera.visibility = View.VISIBLE
                binding.photoGallery.visibility = View.VISIBLE
                visible = true
            }
        }
        profileViewModel.userID().observe(this, androidx.lifecycle.Observer {
            userId = it

            profileViewModel.getUserData(userId!!.uid).observe(this, androidx.lifecycle.Observer {

                binding.profileName.text = it.username

                if(it != null && !it.profile_image.isEmpty()) {
                    picasso
                        .load(it.profile_image)
                        .into(binding.profileImage)
                }
            })
            profileViewModel.getUserDiscussionFeed(userId!!.uid).observe(this, androidx.lifecycle.Observer {
                with(profile_discussion_list){
                    setHasFixedSize(true)
                    layoutManager = LinearLayoutManager(inflater.context)
                    adapter = DiscussionAdapter(lifecycleOwner, activity as AppCompatActivity,  it)
                }
            })
        })



        binding.photoGallery.setOnClickListener{
            startActivityForResult(cameraManager.chooseImage(it), CHOOSE_IMAGE_REQUEST)
        }

        binding.photoCamera.setOnClickListener {
            startActivityForResult(cameraManager.takePicture(context!!), REQUEST_IMAGE_CAPTURE)

        }

        return binding.root
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == CHOOSE_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null){

            profileViewModel.userProfileImageUpload(data.data!!,
                userId!!
            )
            val bitmap = cameraManager.tryReadBitmap(data.data!!, context!!)
            bitmap?.let {
                imageBitmap = bitmap
                profile_image.setImageBitmap(bitmap)
            }

            binding.photoCamera.visibility = View.GONE
            binding.photoGallery.visibility = View.GONE
            visible = false
        }
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK){

            cameraManager.getFilePath().observe(this, androidx.lifecycle.Observer {
                profileViewModel.userProfileImageUpload(Uri.fromFile(File(it!!)),
                    userId!!)
            })

            var bitmap :Bitmap = BitmapFactory.decodeFile(cameraManager.getFilePath().value)
                profile_image.setImageBitmap(bitmap)

            binding.photoCamera.visibility = View.GONE
            binding.photoGallery.visibility = View.GONE
            visible = false
        }
    }
}
