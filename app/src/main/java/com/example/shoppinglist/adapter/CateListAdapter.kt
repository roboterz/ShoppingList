package com.example.shoppinglist.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.CheckBox
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R
import com.example.shoppinglist.data.entities.CateList
import com.example.shoppinglist.data.entities.ListDetail
import kotlin.collections.ArrayList


class CateListAdapter(
    private val onClick: OnClickListener
    )
    : RecyclerView.Adapter<CateListAdapter.ViewHolder>() {

    //
    private var mList: List<CateList> = ArrayList()

    // interface for passing the onClick event to fragment.
    interface OnClickListener {
        fun onItemClick(cateID: Long)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflate the custom view from xml layout file
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.cardview_item,parent,false)


        // return the view holder
        return ViewHolder(view)

    }


    //@SuppressLint("ResourceAsColor")
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // display the custom class
        mList[position].apply {
            holder.tvName.text = name
            holder.tvNote.visibility = View.GONE
            holder.checkComplete.visibility = View.GONE

            // pass the item click listener to fragment
            holder.aItem.setOnClickListener {
                onClick.onItemClick(cateID)
            }
        }


    }


    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<CateList>){
        mList = list
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int {
        // the data set held by the adapter.
        return mList.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val tvName: TextView = itemView.findViewWithTag("name")
        val tvNote: TextView = itemView.findViewWithTag("note")
        var checkComplete: CheckBox = itemView.findViewWithTag("check")
        val aItem: ConstraintLayout = itemView.findViewWithTag("item")
    }


    // this two methods useful for avoiding duplicate item
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onViewAttachedToWindow(holder: ViewHolder) {
        super.onViewAttachedToWindow(holder)

        holder.itemView.clearAnimation()
        holder.itemView.startAnimation(AnimationUtils.loadAnimation(holder.itemView.context, R.anim.scale_in_scroll))
    }
}