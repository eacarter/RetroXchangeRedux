package com.erickson.retroxchange.adapter

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView

import com.erickson.retroxchange.R
import com.erickson.retroxchange.datamodels.DiscussionData
import com.erickson.retroxchange.manager.DatabaseManager
import com.erickson.retroxchange.ui.dashboard.DashboardItemFragment
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.discussion_singlecard.view.*
import javax.inject.Inject

class DiscussionAdapter(val lifecycleOwner:LifecycleOwner, val activity: AppCompatActivity, val discussions: List<DiscussionData>):
    RecyclerView.Adapter<DiscussionAdapter.DiscussionViewHolder>() {

    var databaseManager: DatabaseManager = DatabaseManager()

    class DiscussionViewHolder(val v1: View) : RecyclerView.ViewHolder(v1)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiscussionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.discussion_singlecard, parent,false)
        return DiscussionViewHolder(
            view
        )
    }

    override fun getItemCount(): Int {
        return discussions.size
    }

    override fun onBindViewHolder(holder: DiscussionViewHolder, position: Int) {
        databaseManager.getUser(discussions[position].userId).observe(lifecycleOwner, Observer {
            if(!it.profile_image.isEmpty()) {
                Picasso.get().load(it.profile_image).into(holder.v1.profile_image)
            }
            holder.v1.discussion_post_title.text = discussions[position].title
            holder.v1.discussion_post_username.text = discussions[position].userName
            holder.v1.discussion_post_date.text = discussions[position].timeStamp

            holder.v1.discussion_single.setOnClickListener(View.OnClickListener {

                var bundle = Bundle()

                bundle.putString("POST_ID", discussions[position].id)

                var fragment = DashboardItemFragment()

                fragment.arguments = bundle

                activity.supportFragmentManager
                    .beginTransaction()

                    .add(R.id.nav_host_fragment, fragment, "DasdboardItem")
                    .addToBackStack("Dashboard")
                    .commit()
            })
        })
    }


}