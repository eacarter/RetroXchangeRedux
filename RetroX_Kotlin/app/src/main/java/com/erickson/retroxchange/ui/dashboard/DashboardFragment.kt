package com.erickson.retroxchange.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.erickson.retroxchange.R
import com.erickson.retroxchange.databinding.DashboardFragmentBinding
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.DaggerFragment

class DashboardFragment : DaggerFragment() {

    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: DashboardFragmentBinding

    lateinit var dashboardViewModel: DashboardViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        AndroidSupportInjection.inject(this)

        dashboardViewModel = ViewModelProviders.of(this).get(DashboardViewModel::class.java)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false)
        val textView: TextView = binding.textDashboard

        dashboardViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return binding.root
    }
}
