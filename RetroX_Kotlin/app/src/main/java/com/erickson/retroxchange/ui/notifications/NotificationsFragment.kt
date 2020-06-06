package com.erickson.retroxchange.ui.notifications

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
import com.erickson.retroxchange.databinding.NotificationsFragmentBinding
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.DaggerFragment

class NotificationsFragment : DaggerFragment() {

    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: NotificationsFragmentBinding

    lateinit var notificationsViewModel: NotificationsViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        AndroidSupportInjection.inject(this)

        notificationsViewModel = ViewModelProviders.of(this).get(NotificationsViewModel::class.java)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_notifications, container, false)
        val textView: TextView = binding.textNotifications

        notificationsViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return binding.root
    }
}
