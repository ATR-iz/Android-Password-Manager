package com.atriztech.passwordmanager.model.entity

import androidx.room.Embedded
import androidx.room.Relation
import java.io.Serializable

data class ItemGroupEntity (
    @Embedded
    val item: ItemEntity,
    @Relation(
        parentColumn = "item_group_id",
        entity = GroupEntity::class,
        entityColumn = "group_id"
    )
    val group: GroupEntity

): Serializable