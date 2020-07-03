package com.erickson.retroxchange

import android.Manifest
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.erickson.retroxchange.databinding.HomeFragmentBinding
import com.erickson.retroxchange.databinding.MainActivityBinding
import com.erickson.retroxchange.datamodels.DiscussionData
import com.erickson.retroxchange.login.LoginActivity
import com.erickson.retroxchange.login.LoginViewModel
import com.erickson.retroxchange.manager.DatabaseManager
import com.erickson.retroxchange.manager.UserManager
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import dagger.android.AndroidInjection
//import dagger.databinding.MainActivityBinding
import dagger.android.DaggerActivity
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.DaggerAppCompatActivity
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

class MainActivity : DaggerAppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var mainViewModel: MainViewModel

    private lateinit var binding: MainActivityBinding

    @Inject
    lateinit var userManager: UserManager

    @Inject
    lateinit var databaseManager: DatabaseManager

    lateinit var mFusedLocationClient: FusedLocationProviderClient

    private var isOpen : Boolean = false


    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector(): DispatchingAndroidInjector<Fragment> {
        return dispatchingAndroidInjector
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        AndroidInjection.inject(this)

        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)


        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications, R.id.navigation_notifications ))

//        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val permissions = arrayOf(android.Manifest.permission.CAMERA,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION)

        fun hasNoPermissions(): Boolean{
            return ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        }

        fun requestPermission(){
            ActivityCompat.requestPermissions(this, permissions,0)
        }

        MobileAds.initialize(this)

//        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
//
//        mFusedLocationClient.lastLocation.addOnCompleteListener { task ->
//            var location: Location? = null
//            if(location == null){
//                var mLocationRequest = LocationRequest()
//                mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
//                mLocationRequest.interval = 0
//                mLocationRequest.fastestInterval = 0
//                mLocationRequest.numUpdates = 1
//
//                mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
//                mFusedLocationClient!!.requestLocationUpdates(
//                    mLocationRequest, null,
//                    Looper.myLooper()
//                )
//            }
//            else{
//                location.longitude.toString()
//                location.latitude.toString()
//            }
//        }

        binding.fabDiscussion.setOnClickListener ( View.OnClickListener {

            val dialogBuilder = AlertDialog.Builder(this)

            val layout = layoutInflater.inflate(R.layout.dialog_discussion, null)

            dialogBuilder.setView(layout)

            dialogBuilder.setTitle("Create new discussion post")

            val title : EditText = layout.findViewById<EditText>(R.id.discussion_title)
            val text : EditText = layout.findViewById<EditText>(R.id.discussion_text)

            dialogBuilder.setPositiveButton("Create", DialogInterface.OnClickListener { dialog, which ->

                userManager.getUserData().observe(
                    this,
                    Observer { it1 ->
                        var id = it1.uid + randomize()

                        var discussionData = DiscussionData(
                            id,
                            it1.uid,
                            it1.email!!.substringBefore('@', ""),
                            title.text.toString(),
                            text.text.toString(),
                            "",
                            SimpleDateFormat("MM/dd/yyyy").format(Date()),
                            arrayListOf(),
                            arrayListOf()
                        )
                        databaseManager.uploadDiscussionFeed(id, discussionData)
                    })
                }
            )

            dialogBuilder.setNegativeButton("Cancel", DialogInterface.OnClickListener {dialog, which ->
               dialog.cancel()
            })

            dialogBuilder.create().show()
            binding.fabDiscussion.visibility = View.GONE
            binding.fabItem.visibility = View.GONE
            isOpen = false
        })

        binding.addFab.setOnClickListener( View.OnClickListener {
            if(isOpen){
                binding.fabDiscussion.visibility = View.GONE
                binding.fabItem.visibility = View.GONE
                isOpen = false
            }
            else{
                binding.fabDiscussion.visibility = View.VISIBLE
                binding.fabItem.visibility = View.VISIBLE
                isOpen = true
            }
        })

        mainViewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)

        mainViewModel.getUserInfo()?.observe(this, Observer {
            if(it == null){
                startActivity(Intent(this, LoginActivity::class.java))
            }
        });


    }

    fun randomize() : String{

        var idAppend = StringBuilder()

        for(i in 1..15){
           var pos: Char = ('a'..'z' - 1).random()
            idAppend.append(pos)
        }

        return idAppend.toString()
    }

}
