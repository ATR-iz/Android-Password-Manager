package com.atriztech.passwordmanager.view.recyclerviewadapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.atriztech.passwordmanager.R
import com.atriztech.passwordmanager.model.entity.ItemGroupEntity
import java.util.*

interface ListItemsDelegate{
    fun OpenItem(itemGroup: ItemGroupEntity)
    fun EditItem(itemGroup: ItemGroupEntity)
}

class ListItemsRecyclerViewAdapter: RecyclerView.Adapter<ListItemsRecyclerViewAdapter.ViewHolder>() {
    val listData: MutableList<ItemGroupEntity> = LinkedList()
    private var delegate: ListItemsDelegate? = null

    fun attachDelegate(delegate: ListItemsDelegate){
        this.delegate = delegate
    }

    fun setData(newList: List<ItemGroupEntity>){
        listData.clear()
        listData.addAll(newList)

        notifyDataSetChanged()
    }

    //Велосипед
    fun InsertItem(newItemGroup: ItemGroupEntity){
        val position = itemCount
        listData.add(position, newItemGroup)
        notifyItemInserted(position)
    }

    fun UpdateItem(newItemGroup: ItemGroupEntity){
        val position = PositionItem(newItemGroup)
        listData[position] = newItemGroup
        notifyItemChanged(position)
    }

    fun DeleteItem(newItemGroup: ItemGroupEntity){
        val position = PositionItem(newItemGroup)
        listData.removeIf{ it.item.id == newItemGroup.item.id }
        notifyItemRemoved(position)
    }

    fun PositionItem(newItemGroup: ItemGroupEntity): Int{
        return listData.indexOfFirst { it.item.id == newItemGroup.item.id }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(itemView = LayoutInflater.from(viewGroup.context).inflate(R.layout.item, viewGroup, false),
            delegate = delegate)
    }

    override fun getItemCount(): Int {
        return listData.count()
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(model = listData[position], position = position)
    }

    class ViewHolder(itemView: View, val delegate: ListItemsDelegate?): RecyclerView.ViewHolder(itemView){
        private val txtLogin: TextView = itemView.findViewById(R.id.item_login)
        private val txtGroup: TextView = itemView.findViewById(R.id.item_group)
        private val txtPosition: TextView = itemView.findViewById(R.id.item_position)

        fun bind(model: ItemGroupEntity, position: Int){
            txtLogin.text = model.item.name
            txtGroup.text = model.group.name
            txtPosition.text = (position + 1).toString()

            itemView.setOnClickListener {
                delegate?.OpenItem(itemGroup = model)
            }

            itemView.setOnLongClickListener {
                delegate?.EditItem(itemGroup = model)
                return@setOnLongClickListener true
            }
        }
    }
}