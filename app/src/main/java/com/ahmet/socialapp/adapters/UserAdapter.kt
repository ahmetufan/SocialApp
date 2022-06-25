package com.ahmet.socialapp.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ahmet.socialapp.ChatActivity
import com.ahmet.socialapp.ModelClasses.Users
import com.ahmet.socialapp.R
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.user_search_item_layout.view.*

class UserAdapter(mContext: Context, mUsers: List<Users>, isChatChechk: Boolean) :
    RecyclerView.Adapter<UserAdapter.ViewHolder?>() {

    private val mContext: Context
    private val mUsers: List<Users>
    private val isChatChechk: Boolean

    init {
        this.mUsers = mUsers
        this.mContext = mContext
        this.isChatChechk = isChatChechk
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=LayoutInflater.from(mContext).inflate(R.layout.user_search_item_layout,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val user: Users = mUsers[position]

        holder.userNameText.text=user!!.getUserName()

        Glide.with(mContext).load(user.getProfile()).placeholder(R.drawable.profile).into(holder.profileImageView)

        holder.itemView.layoutUser.setOnClickListener {
            val intent=Intent(mContext,ChatActivity::class.java)
            intent.putExtra("userId",user.getUID())
            intent.putExtra("userName",user.getUserName())
            mContext.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return mUsers.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var userNameText: TextView
        var profileImageView: CircleImageView
        var onlineImageview: CircleImageView
        var offlineImageView: CircleImageView
        var lastMessageTxt: TextView

        init {
            userNameText = itemView.findViewById(R.id.username)
            profileImageView = itemView.findViewById(R.id.profile_image)
            onlineImageview = itemView.findViewById(R.id.image_online)
            offlineImageView = itemView.findViewById(R.id.image_offline)
            lastMessageTxt = itemView.findViewById(R.id.message_last)
        }
    }
}
