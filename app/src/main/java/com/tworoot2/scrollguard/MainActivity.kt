package com.tworoot2.scrollguard

import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tworoot2.scrollguard.presentation.ui.screens.main_screen.MainScreenLayout
import com.tworoot2.scrollguard.presentation.ui.viewmodels.PermissionsViewModel
import com.tworoot2.scrollguard.presentation.viewmodels.ApplicationDataViewModel
import com.tworoot2.scrollguard.settings.AppSettings
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val permissionsViewModel: PermissionsViewModel by viewModels()
    private val applicationDataViewModel: ApplicationDataViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        setContent {
            Column(
                Modifier
                    .background(Color.Black)
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(10.dp)
                    .padding(
                        PaddingValues(top = 50.dp)
                    ),
            ) {


                MainScreenLayout(
                    permissionsViewModel = permissionsViewModel,
                    applicationDataViewModel = applicationDataViewModel
                )


            }

        }
    }

    override fun onResume() {
        super.onResume()
        permissionsViewModel.checkPermissions()
    }

}








