package com.erickson.retroxchange.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.erickson.retroxchange.MainActivity

import com.erickson.retroxchange.R
import com.erickson.retroxchange.datamodels.DiscussionData
import com.erickson.retroxchange.manager.DatabaseManager
import com.erickson.retroxchange.ui.dashboard.DashboardItemActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_dashboard_item.view.*
import kotlinx.android.synthetic.main.discussion_comment_singlecard.view.*
import kotlinx.android.synthetic.main.discussion_singlecard.view.*
import kotlinx.android.synthetic.main.discussion_singlecard.view.discussion_count
import kotlinx.android.synthetic.main.discussion_singlecard.view.discussion_post_date
import kotlinx.android.synthetic.main.discussion_singlecard.view.discussion_post_title
import kotlinx.android.synthetic.main.discussion_singlecard.view.discussion_post_username
import kotlinx.android.synthetic.main.discussion_singlecard.view.discussion_single
import kotlinx.android.synthetic.main.discussion_singlecard.view.profile_image
import javax.inject.Inject

class DiscussionAdapter (val lifecycleOwner:LifecycleOwner,
                                            val activity: AppCompatActivity,
                                            val discussions: List<DiscussionData>,
                         var databaseManager: DatabaseManager ):
    RecyclerView.Adapter<DiscussionAdapter.DiscussionViewHolder>() {


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
            holder.v1.discussion_count.text = discussions[position].starredUsers.size.toString()
            holder.v1.discussion_single_star_image.isSelected = discussions[position].starredUsers.contains(discussions[position].userId)

            holder.v1.discussion_single_star_image.setOnClickListener(View.OnClickListener {
                if(discussions[position].starredUsers.contains(discussions[position].userId)){
                    databaseManager.removeDiscussionCount(discussions[position].id, discussions[position].userId)
                    holder.v1.discussion_single_star_image.isSelected = false
                }
                else{
                    databaseManager.updateDiscussionCount(discussions[position].id, discussions[position].userId)
                    holder.v1.discussion_single_star_image.isSelected = true
                }
            })

            holder.v1.discussion_single.setOnClickListener(View.OnClickListener {

                val intent = Intent(activity.applicationContext, DashboardItemActivity::class.java)
                intent.putExtra("postID", discussions[position].id)

                activity.startActivity(intent)
            })
        })
    }


}