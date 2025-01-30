package com.tworoot2.scrollguard

import android.app.Application
import com.tworoot2.scrollguard.data.local.application_data.SelectedApplicationDB
import com.tworoot2.scrollguard.data.local.application_data.SelectedApplicationDao
import com.tworoot2.scrollguard.domain.models.ApplicationModel
import com.tworoot2.scrollguard.utils.SharedPref
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class MyApplication : Application() {

    @Inject
    lateinit var db: SelectedApplicationDB

    @Inject
    lateinit var selectedApplicationDao: SelectedApplicationDao


    override fun onCreate() {
        super.onCreate()

        CoroutineScope(Dispatchers.IO).launch {
            preloadTheData()
        }

    }

    private suspend fun preloadTheData() {
        val is_first_time = SharedPref.getSavedData(this, "is_first_time")
        if (is_first_time != "true") {
            val preloadedData = listOf(
                ApplicationModel(
                    id = "com.instagram.android",
                    name = "Instagram",
                    viewId = "root_clips_layout",
                    isSelected = false
                ),
                ApplicationModel(
                    id = "com.google.android.youtube",
                    name = "YouTube",
                    viewId = "reel_watch_fragment_root",
                    isSelected = false
                )

            )

            selectedApplicationDao.addApplications(preloadedData)
            SharedPref.saveData(this, "is_first_time", "true")

        }

    }
}