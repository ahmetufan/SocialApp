package com.ahmet.socialapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ahmet.socialapp.ModelClasses.Post
import com.ahmet.socialapp.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.row_home.view.*

class RecyclerAdapter(val list:ArrayList<Post>): RecyclerView.Adapter<RecyclerAdapter.Holder>() {

    class Holder(view: View): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view1= LayoutInflater.from(parent.context).inflate(R.layout.row_home,parent,false)
        return Holder(view1)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

        holder.itemView.recyclerEmailText.text=list[position].email
        holder.itemView.recyclerCommentText.text=list[position].comment

        Picasso.get().load(list.get(position).downloadurl).into(holder.itemView.recyclerImageview)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}