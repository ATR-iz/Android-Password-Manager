package com.atriztech.passwordmanager.model.dao

import androidx.room.*
import com.atriztech.passwordmanager.model.entity.ItemEntity

@Dao
interface ItemDao {
    @Query("SELECT * FROM items_table")
    fun getAll(): List<ItemEntity>

    @Query("SELECT * FROM items_table WHERE item_id IN (:position)")
    fun getItem(position: Long?): ItemEntity

    @Update
    fun update(item: ItemEntity)

    @Query("DELETE FROM items_table WHERE item_id IN (:position)")
    fun deleteFromPosition(position: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: ItemEntity): Long

    @Delete
    fun delete(item: ItemEntity)
}