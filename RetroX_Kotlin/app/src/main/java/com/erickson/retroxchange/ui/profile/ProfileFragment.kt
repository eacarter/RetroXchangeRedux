package com.erickson.retroxchange.ui.profile

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.erickson.retroxchange.R
import com.erickson.retroxchange.databinding.NotificationsFragmentBinding
import com.erickson.retroxchange.databinding.ProfileFragmentBinding
import com.erickson.retroxchange.ui.notifications.NotificationsViewModel
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_profile.*
import java.io.IOException
import javax.inject.Inject

class ProfileFragment : DaggerFragment() {

    private val CHOOSE_IMAGE_REQUEST = 1
    private var imageBitmap: Bitmap? = null

    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: ProfileFragmentBinding

    lateinit var profileViewModel: ProfileViewModel

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
            chooseImage(it)
        }

        return binding.root
    }

    fun chooseImage(view: View) {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        var chooser = Intent.createChooser(intent, "Choose image for item")
        startActivityForResult(chooser, CHOOSE_IMAGE_REQUEST)
//        Log.d(this.context.packageName, "Intent to choose image sent...")
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == CHOOSE_IMAGE_REQUEST && resultCode == Activity.RESULT_OK
            && data != null && data.data != null){
//            Log.d(this.localClassName, "Image Chosen")

            val bitmap = tryReadBitmap(data.data!!)

            bitmap?.let {
                imageBitmap = bitmap
//                card2.visibility = View.VISIBLE
                profile_image.setImageBitmap(bitmap)
//                Log.d(this.localClassName, "read image bitmap, updated image view")
            }
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
