package com.erickson.retroxchange.ui.dashboard

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

import com.erickson.retroxchange.R
import com.erickson.retroxchange.adapter.DiscussionCommentAdapter
import com.erickson.retroxchange.databinding.DashboardItemFragmentBinding
import com.erickson.retroxchange.datamodels.DiscussionCommentData
import com.erickson.retroxchange.manager.DatabaseManager
import com.squareup.picasso.Picasso
import dagger.android.AndroidInjection
import dagger.android.support.DaggerAppCompatActivity
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.text.StringBuilder

class DashboardItemActivity : DaggerAppCompatActivity() {

    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var databaseManager: DatabaseManager

    private lateinit var binding: DashboardItemFragmentBinding

    private lateinit var dashboardItemViewModel: DashboardItemViewModel

    private var id = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_item)
        AndroidInjection.inject(this)

        id = intent.getStringExtra("postID")

        val lifecycleOwner: LifecycleOwner = this

        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard_item)

        dashboardItemViewModel = ViewModelProviders.of(this, viewModelFactory).get(DashboardItemViewModel::class.java)

        with(dashboardItemViewModel){
                getDiscussionSpecific(id).observe(lifecycleOwner, Observer {
                    binding.mainDiscussionPostUsername.text = it.userName
                    binding.mainDiscussionPostTitle.text = it.title
                    binding.mainDiscussionPostDate.text = it.timeStamp
                    binding.mainDiscussionPostText.text = it.message
                    binding.discussionCount.text = it.starredUsers.size.toString()
                    binding.discussionStarImage.isSelected = it.starredUsers.contains(it.userId)

                    binding.discussionStarImage.setOnClickListener(View.OnClickListener {it1 ->
                        if(it.starredUsers.contains(it.userId)){
                            removeCount(id, it.userId)
                            binding.discussionStarImage.isSelected = false
                        }
                        else{
                            updateCount(id, it.userId)
                            binding.discussionStarImage.isSelected = true
                        }
                    })
                })
                getUserData().observe(lifecycleOwner, Observer {
                    getUserInfo(it.uid).observe(lifecycleOwner, Observer {
                        if (!it.profile_image.isEmpty()) {
                            Picasso.get().load(it.profile_image).into(binding.mainProfileImage)
                        }
                    })
                })
        }

        with(dashboardItemViewModel){
            getUserData().observe(lifecycleOwner, Observer {
                    getDiscussionComments(id).observe(lifecycleOwner, Observer {
                        with(binding.mainDiscussionList) {
                            setHasFixedSize(true)
                            layoutManager = LinearLayoutManager(context)
                            adapter = DiscussionCommentAdapter(lifecycleOwner, it, databaseManager)
                        }
                    })
            })
        }

        binding.mainDiscussionSend.setOnClickListener(View.OnClickListener {
            with(dashboardItemViewModel){
                getUserData().observe(lifecycleOwner, Observer {

                    val comment = DiscussionCommentData(
                        it.uid + randomize(),
                        it.uid,
                        it.email!!.substringBefore("@"),
                        binding.mainNewPost.text.toString(),
                        SimpleDateFormat("MM/dd/yyyy").format(Date()),
                        arrayListOf(it.uid)
                    )
                    uploadDiscussionComments(id, comment, randomize())
                    binding.mainNewPost.text.clear()
                })
            }
        })
    }


    fun randomize() : String{
        val idAppend:StringBuilder = StringBuilder()
        for(i in 1..15){
            var pos: Char = ('a'..'z' - 1).random()
            idAppend.append(pos)
        }
        return idAppend.toString()
    }
}
