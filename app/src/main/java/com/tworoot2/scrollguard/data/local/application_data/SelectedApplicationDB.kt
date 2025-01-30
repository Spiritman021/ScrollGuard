package com.tworoot2.scrollguard.data.local.application_data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tworoot2.scrollguard.domain.models.ApplicationModel

@Database(
    entities = [ApplicationModel::class],
    version = 1,
    exportSchema = true
)
abstract class SelectedApplicationDB : RoomDatabase() {
    abstract fun selectedApplicationDao(): SelectedApplicationDao
}

