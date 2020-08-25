package com.atriztech.passwordmanager.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "items_table")
data class ItemEntity(
    @ColumnInfo(name = "item_name") var name: String,
    @ColumnInfo(name = "item_password") var password: String?,
    @ColumnInfo(name = "item_group_id") var idGroup: Long?
): Serializable {
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "item_id") var id: Long = 0
}