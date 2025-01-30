package com.tworoot2.scrollguard.data.repositories

import com.tworoot2.scrollguard.data.local.application_data.SelectedApplicationDao
import com.tworoot2.scrollguard.domain.models.ApplicationModel
import kotlinx.coroutines.flow.Flow

class SelectedApplicationRepository(private val selectedApplicationDao: SelectedApplicationDao) {

    suspend fun addApplication(applicationModel: ApplicationModel) {
        selectedApplicationDao.addApplication(applicationModel)
    }

    suspend fun updateApplication(applicationModel: ApplicationModel) {
        selectedApplicationDao.updateApplication(applicationModel)
    }

    suspend fun deleteApplication(applicationModel: ApplicationModel) {
        selectedApplicationDao.deleteApplication(applicationModel)
    }

    suspend fun getApplicationById(applicationModel: ApplicationModel):  ApplicationModel {
      return  selectedApplicationDao.getApplicationDetailById(applicationModel.id)
    }

    suspend fun getAllApplications(): Flow<List<ApplicationModel>> {

        return selectedApplicationDao.getAllApplications()
    }


}
