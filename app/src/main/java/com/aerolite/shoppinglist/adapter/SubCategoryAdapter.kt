package com.aerolite.shoppinglist.adapter


import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.aerolite.shoppinglist.R
import com.aerolite.shoppinglist.data.entities.Category
import kotlin.collections.ArrayList


class SubCategoryAdapter(
    private val onClickListener: OnClickListener
)
    : RecyclerView.Adapter<SubCategoryAdapter.ViewHolder>() {

    private var subCategory: List<Category> = ArrayList()
    private var checkable: Boolean = false

    // interface for passing the onClick event to fragment.
    interface OnClickListener {
        fun onItemClick(cateID: Long, parentID: Long, checkBox: Boolean = false)
        fun onItemLongClick(cateID: Long, name: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflate the custom view from xml layout file
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.cardview_sub_category,parent,false)


        // return the view holder
        return ViewHolder(view)

    }


    //@SuppressLint("ResourceAsColor")
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // display the custom class
        subCategory[position].apply {
            // load data
            holder.subCategoryName.text = Category_Name



            if (Category_Completed == -1){
                // add
                holder.subCategoryCheckBox.visibility = View.INVISIBLE

                holder.subCategoryName.setTextColor(holder.addButtonTextColor)
                holder.subCategoryCL.setOnClickListener {
                    onClickListener.onItemClick(Category_ID, Category_ParentID,true)
                }

            }else{
//                if (checkable) {
//                    // text color
//                    when (Category_Completed) {
//                        2 -> holder.subCategoryName.setTextColor(holder.normalTextColor)
//                        3 -> holder.subCategoryName.setTextColor(holder.completedTextColor)
//                        else -> holder.subCategoryName.setTextColor(holder.normalTextColor)
//                    }
//                }
                if (checkable){
                    // check box
                    holder.subCategoryCheckBox.visibility = View.VISIBLE
                    when (Category_Completed){
                        0 -> {
                            holder.subCategoryCheckBox.isChecked = false
                            holder.subCategoryName.setTextColor(holder.normalTextColor)
                        }
                        1 -> {
                            holder.subCategoryCheckBox.isChecked = true
                            holder.subCategoryName.setTextColor(holder.normalTextColor)
                        }
                        2 -> {
                            holder.subCategoryCheckBox.isChecked = false
                            holder.subCategoryName.setTextColor(holder.normalTextColor)
                        }
                        3 -> {
                            holder.subCategoryCheckBox.isChecked = true
                            holder.subCategoryName.setTextColor(holder.completedTextColor)
                        }
                    }

                }else{
                    holder.subCategoryCheckBox.visibility = View.INVISIBLE
                    holder.subCategoryName.setTextColor(holder.normalTextColor)
                }

                // click item
                holder.subCategoryCL.setOnClickListener {
                    if (checkable) {
                        holder.subCategoryCheckBox.isChecked = !holder.subCategoryCheckBox.isChecked

                        // text color
                        if (Category_Completed > 1){
                            when (holder.subCategoryCheckBox.isChecked){
                                true -> holder.subCategoryName.setTextColor(holder.completedTextColor)
                                false -> holder.subCategoryName.setTextColor(holder.normalTextColor)
                            }
                        }else{
                            holder.subCategoryName.setTextColor(holder.normalTextColor)
                        }
                    }
                    onClickListener.onItemClick(Category_ID, Category_ParentID, holder.subCategoryCheckBox.isChecked)
                }


                // long click item
                holder.subCategoryCL.setOnLongClickListener {
                    onClickListener.onItemLongClick(Category_ID, Category_Name)
                    true
                }

            }

        }
    }


    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<Category>, mode: Boolean = false){
        subCategory = list
        checkable = mode
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int {
        // the data set held by the adapter.
        return subCategory.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val subCategoryName: TextView = itemView.findViewById(R.id.tv_sub_category_name)
        val subCategoryCheckBox: CheckBox = itemView.findViewById(R.id.cb_check)
        val subCategoryCL: ConstraintLayout = itemView.findViewById(R.id.cl_sub_category)

        val addButtonTextColor = Color.GRAY
        val normalTextColor = Color.WHITE
        val completedTextColor = Color.GRAY


    }


    // this two methods useful for avoiding duplicate item
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}