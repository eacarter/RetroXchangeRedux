package com.erickson.retroxchange.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView

import com.erickson.retroxchange.R
import com.erickson.retroxchange.datamodels.DiscussionCommentData
import com.erickson.retroxchange.manager.DatabaseManager
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.discussion_comment_singlecard.view.*
import kotlinx.android.synthetic.main.discussion_singlecard.view.*
import kotlinx.android.synthetic.main.discussion_singlecard.view.discussion_count
import kotlinx.android.synthetic.main.discussion_singlecard.view.discussion_post_date
import kotlinx.android.synthetic.main.discussion_singlecard.view.discussion_post_title
import kotlinx.android.synthetic.main.discussion_singlecard.view.discussion_post_username
import kotlinx.android.synthetic.main.discussion_singlecard.view.profile_image

class DiscussionCommentAdapter(val lifecycleOwner:LifecycleOwner, val discussions: List<DiscussionCommentData>,
                               var databaseManager: DatabaseManager): RecyclerView.Adapter<DiscussionCommentAdapter.DiscussionViewHolder>() {

    class DiscussionViewHolder(val v1: View) : RecyclerView.ViewHolder(v1)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiscussionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.discussion_comment_singlecard, parent,false)
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
            holder.v1.discussion_post_title.text = discussions[position].text
            holder.v1.discussion_post_username.text = discussions[position].userName
            holder.v1.discussion_post_date.text = discussions[position].timeStamp
            holder.v1.discussion_count.text = discussions[position].starredUsers.size.toString()
            holder.v1.discussion_comment_star_image.isSelected = discussions[position].starredUsers.contains(discussions[position].userId)

            holder.v1.discussion_comment_star_image.setOnClickListener(View.OnClickListener {
                if(discussions[position].starredUsers.contains(discussions[position].userId)){
                    databaseManager.removeDiscussionCount(discussions[position].id, discussions[position].userId)
                    holder.v1.discussion_comment_star_image.isSelected = false
                }
                else{
                    databaseManager.updateDiscussionCount(discussions[position].id, discussions[position].userId)
                    holder.v1.discussion_comment_star_image.isSelected = true
                }
            })

        })
    }


}