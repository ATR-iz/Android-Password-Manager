package com.atriztech.passwordmanager.model.dao

import androidx.room.*
import com.atriztech.passwordmanager.model.entity.ItemGroupEntity
import com.atriztech.passwordmanager.model.entity.ItemEntity
import com.atriztech.passwordmanager.model.entity.GroupEntity

@Dao
interface ItemGroupDao {
    @Transaction
    fun insert(itemGroupEntity: ItemGroupEntity): Long{
        var allGroups = getGroups()

        var id: Long = -1

        for (group in allGroups){
            if (group.name == itemGroupEntity.group.name){
                id = group.id
                break
            }
        }

        if (id < 0){
            itemGroupEntity.item.idGroup = insert(itemGroupEntity.group)
        } else {
            itemGroupEntity.item.idGroup = id
        }

        return insert(itemGroupEntity.item)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(group: GroupEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(itemEntity: ItemEntity): Long

    @Query("SELECT * FROM group_table")
    fun getGroups(): List<GroupEntity>

    @Query("SELECT * FROM items_table, group_table WHERE items_table.item_group_id = group_table.group_id AND item_group_id IN (:idGroup)")
    fun getItemsFromGroup(idGroup: Long): List<ItemGroupEntity>

    @Query("SELECT * FROM items_table WHERE  item_id IN (:position)")
    fun getItem(position: Long): ItemEntity

    @Query("SELECT * FROM items_table, group_table WHERE items_table.item_group_id = group_table.group_id AND item_id IN (:position)")
    fun getItemGroup(position: Long): ItemGroupEntity

    @Query("SELECT * FROM items_table, group_table WHERE items_table.item_group_id = group_table.group_id")
    fun getAll(): List<ItemGroupEntity>

    @Query("SELECT * FROM items_table")
    fun getAllItems(): List<ItemEntity>

    @Delete
    fun delete(item: ItemEntity)

    @Query("SELECT * FROM group_table WHERE group_id IN (:position)")
    fun getGroupId(position: Long): GroupEntity

    @Query("DELETE FROM items_table WHERE item_id IN (:position)")
    fun deleteItemId(position: Long)

    @Query("DELETE FROM group_table WHERE group_id IN (:position)")
    fun deleteGroupId(position: Long)
}