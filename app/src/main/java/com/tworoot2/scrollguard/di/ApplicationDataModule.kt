package com.tworoot2.scrollguard.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.tworoot2.scrollguard.data.local.application_data.SelectedApplicationDB
import com.tworoot2.scrollguard.data.local.application_data.SelectedApplicationDao
import com.tworoot2.scrollguard.data.repositories.SelectedApplicationRepository
import com.tworoot2.scrollguard.domain.models.ApplicationModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationDataModule {


    @Provides
    @Singleton
    fun provideRoomDatabase(
        @ApplicationContext context: Context,
    ): SelectedApplicationDB {
        return Room.databaseBuilder(context, SelectedApplicationDB::class.java, "ScrollGuardDB")
            //            .addMigrations()  // later add migrations if table changes
            .build()
    }

    @Provides
    @Singleton
    fun provideSelectedApplicationDao(db: SelectedApplicationDB): SelectedApplicationDao {
        return db.selectedApplicationDao()
    }

    @Provides
    @Singleton
    fun provideSelectedApplicationRepository(dao: SelectedApplicationDao): SelectedApplicationRepository {
        return SelectedApplicationRepository(dao)
    }


}

