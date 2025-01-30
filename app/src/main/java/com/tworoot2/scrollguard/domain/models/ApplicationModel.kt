package com.tworoot2.scrollguard.domain.models

import android.graphics.drawable.Icon
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "applications_table")
data class ApplicationModel(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val name: String,
    val viewId: String,
    val isSelected: Boolean
)

