package com.ahmet.messenger21.adaptor

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.ahmet.messenger21.R
import com.ahmet.messenger21.model.User
import com.ahmet.messenger21.view.UsersFragmentDirections
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView


class UserAdaptor(private val context: Context, private val userList: ArrayList<User>) :
    RecyclerView.Adapter<UserAdaptor.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.item_user,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user=userList[position]
        holder.tstUserName.text=user.userName
        Glide.with(context).load(user.profilImage).into(holder.imgUser)

        holder.layoutUser.setOnClickListener {
            val action= UsersFragmentDirections.actionUsersFragmentToChatFragment2(user.userId,user.userName)
            Navigation.findNavController(it).navigate(action)
/*
            val intent= Intent(context, ChatFragment::class.java)
            intent.putExtra("userId",user.userId)
            intent.putExtra("userName",user.userName)
            context.startActivity(intent)
            */

        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val tstUserName: TextView =view.findViewById(R.id.userName)
        val imgUser: CircleImageView =view.findViewById(R.id.userImage3)
        val layoutUser: LinearLayout =view.findViewById(R.id.layoutUser2)
    }
}