package com.tworoot2.scrollguard.data.local.application_data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.tworoot2.scrollguard.domain.models.ApplicationModel
import kotlinx.coroutines.flow.Flow

@Dao
interface SelectedApplicationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addApplications(applications: List<ApplicationModel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addApplication(applicationModel: ApplicationModel)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateApplication(applicationModel: ApplicationModel)

    @Delete
    suspend fun deleteApplication(applicationModel: ApplicationModel)

    @Query("SELECT * FROM applications_table WHERE id=:id")
    suspend fun getApplicationDetailById(id: String): ApplicationModel

    @Query("SELECT * FROM applications_table")
    fun getAllApplications(): Flow<List<ApplicationModel>>



}