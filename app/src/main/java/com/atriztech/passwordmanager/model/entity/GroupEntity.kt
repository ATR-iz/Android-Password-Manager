package com.atriztech.passwordmanager.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "group_table")
data class GroupEntity(
    @ColumnInfo(name = "group_name") var name: String,
    @ColumnInfo(name = "group_url") var url: String
): Serializable {
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "group_id") var id: Long = 0
}