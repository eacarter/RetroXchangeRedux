package com.erickson.retroxchange.ui.dashboard

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

import com.erickson.retroxchange.R
import com.erickson.retroxchange.adapter.DiscussionAdapter
import com.erickson.retroxchange.adapter.DiscussionCommentAdapter
import com.erickson.retroxchange.databinding.DashboardFragmentBinding
import com.erickson.retroxchange.databinding.DashboardItemFragmentBinding
import com.erickson.retroxchange.datamodels.DiscussionCommentData
import com.squareup.picasso.Picasso
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_dashboard.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.text.StringBuilder

class DashboardItemFragment : DaggerFragment() {

    var postID = ""

    companion object {
        fun newInstance() = DashboardItemFragment()
    }

    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: DashboardItemFragmentBinding

    private lateinit var dashboardItemViewModel: DashboardItemViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val bundle = arguments

        if(bundle != null){
            postID = bundle.getString("POST_ID", "")
        }

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard_item, container, false)

        val lifecycleOwner: LifecycleOwner = this

        dashboardItemViewModel = ViewModelProviders.of(this, viewModelFactory).get(DashboardItemViewModel::class.java)

        with(dashboardItemViewModel){
            getDiscussionSpecific(postID).observe(lifecycleOwner, Observer {
                binding.mainDiscussionPostUsername.text = it.userName
                binding.mainDiscussionPostTitle.text = it.title
                binding.mainDiscussionPostDate.text = it.timeStamp
                binding.mainDiscussionPostText.text = it.message
            })
            getUserData().observe(lifecycleOwner, Observer {
                getUserInfo(it.uid).observe(lifecycleOwner, Observer {
                    if(!it.profile_image.isEmpty()){
                        Picasso.get().load(it.profile_image).into(binding.mainProfileImage)
                    }
                })
            })
        }

        with(dashboardItemViewModel){
            getUserData().observe(lifecycleOwner, Observer {
                getDiscussionComments(postID).observe(lifecycleOwner, Observer {
                    with(binding.mainDiscussionList){
                        setHasFixedSize(true)
                        layoutManager = LinearLayoutManager(inflater.context)
                        adapter = DiscussionCommentAdapter(lifecycleOwner, it)
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
                        SimpleDateFormat("MM/dd/yyyy").format(Date())
                    )
                    uploadDiscussionComments(postID, comment, randomize())
                })
            }
        })

        return binding.root
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
