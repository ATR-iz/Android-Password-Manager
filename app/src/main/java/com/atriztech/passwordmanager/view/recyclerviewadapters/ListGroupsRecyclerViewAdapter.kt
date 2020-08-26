package com.atriztech.passwordmanager.view.recyclerviewadapters

import android.graphics.Paint
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.View.LAYER_TYPE_HARDWARE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.atriztech.passwordmanager.R
import com.atriztech.passwordmanager.model.Dir
import com.atriztech.passwordmanager.model.entity.GroupEntity
import java.util.*

interface ListGroupsDelegate{
    fun OpenGroup(itemGroup: GroupEntity)
    fun EditGroup(itemGroup: GroupEntity)
}

class ListGroupsRecyclerViewAdapter: RecyclerView.Adapter<ListGroupsRecyclerViewAdapter.ViewHolder>() {
    val listData: MutableList<GroupEntity> = LinkedList()
    private var delegate: ListGroupsDelegate? = null

    fun attachDelegate(delegate: ListGroupsDelegate){
        this.delegate = delegate
    }

    fun setData(newList: List<GroupEntity>){
        listData.clear()
        listData.addAll(newList)

        notifyDataSetChanged()
    }

    //Велосипед
    fun InsertItem(item: GroupEntity){
        val position = itemCount
        listData.add(position, item)
        notifyItemInserted(position)
    }

    fun UpdateItem(item: GroupEntity){
        val position = PositionItem(item)
        listData[position] = item
        notifyItemChanged(position)
    }

    fun DeleteItem(item: GroupEntity){
        val position = PositionItem(item)
        listData.removeIf{ it.id == item.id }
        notifyItemRemoved(position)
    }

    fun PositionItem(item: GroupEntity): Int{
        return listData.indexOfFirst { it.id == item.id }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(itemGroupView = LayoutInflater.from(viewGroup.context).inflate(R.layout.group, viewGroup, false),
            delegate = delegate)
    }

    override fun getItemCount(): Int {
        return listData.count()
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(model = listData[position])
    }

    class ViewHolder(itemGroupView: View, val delegate: ListGroupsDelegate?): RecyclerView.ViewHolder(itemGroupView){
        private val txtGroup: TextView = itemGroupView.findViewById(R.id.group_name)
        private val imgImage: ImageView = itemView.findViewById(R.id.group_image)

        fun bind(model: GroupEntity){
            txtGroup.text = model.name
            imgImage.setImageURI(Uri.parse(Dir.homeDirOnMemory + "/" + model.url))

            itemView.setOnClickListener {
                delegate?.OpenGroup(itemGroup = model)
            }

            itemView.setOnLongClickListener {
                delegate?.EditGroup(itemGroup = model)
                return@setOnLongClickListener true
            }


        }
    }
}