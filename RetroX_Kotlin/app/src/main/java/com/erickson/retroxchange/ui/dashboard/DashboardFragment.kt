package com.erickson.retroxchange.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.erickson.retroxchange.R
import com.erickson.retroxchange.adapter.DiscussionAdapter
import com.erickson.retroxchange.databinding.DashboardFragmentBinding
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_dashboard.*
import javax.inject.Inject

class DashboardFragment : DaggerFragment() {

    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: DashboardFragmentBinding

    lateinit var dashboardViewModel: DashboardViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val lifecycleOwner: LifecycleOwner = this

        dashboardViewModel = ViewModelProviders.of(this, viewModelFactory).get(DashboardViewModel::class.java)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false)

        getListData(inflater, lifecycleOwner)

        binding.refreshSwipe.setOnRefreshListener{
                getListData(inflater, lifecycleOwner)
                binding.refreshSwipe.isRefreshing = false
        }

        return binding.root
    }

    fun getListData(inflater: LayoutInflater, lifecycleOwner: LifecycleOwner){
        dashboardViewModel.getDiscussionFeed().observe(this, Observer {
            with(discussion_list){
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(inflater.context)
                adapter = DiscussionAdapter(lifecycleOwner,activity as AppCompatActivity, it)
            }
        })
    }
}
