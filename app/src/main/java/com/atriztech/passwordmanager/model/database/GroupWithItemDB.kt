package com.atriztech.passwordmanager.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.atriztech.passwordmanager.model.dao.ItemGroupDao
import com.atriztech.passwordmanager.model.entity.ItemEntity
import com.atriztech.passwordmanager.model.entity.GroupEntity
import javax.inject.Singleton

@Singleton
@Database(entities = [ItemEntity::class, GroupEntity::class], version = 1)
abstract class GroupWithItemDB : RoomDatabase() {
    abstract fun itemDao(): ItemGroupDao
}