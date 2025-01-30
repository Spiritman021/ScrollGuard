package com.tworoot2.scrollguard.presentation.ui.screens.main_screen.specific_components

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import com.tworoot2.scrollguard.presentation.ui.viewmodels.PermissionsViewModel

@Composable
fun AllPermissionLayout(permissionsViewModel: PermissionsViewModel) {


    val isAccessibilityGranted by permissionsViewModel.isAccessibilityGranted.observeAsState(
        initial = false
    )
    val isUsageStatsGranted by permissionsViewModel.isUsageStatsGranted.observeAsState(initial = false)


    val context = LocalContext.current

    if (!isAccessibilityGranted) {
        PermissionsLayout(text = "Accessibility") {
            permissionsViewModel.handleAccessibilityPermission(context)
        }
    }

    if (!isUsageStatsGranted) {
        Log.e("PermissionUsage: ", "Accepted")
        PermissionsLayout(text = "App Usage") {
            permissionsViewModel.handleUsageStatsPermission(context)
        }
    }

    PermissionsLayout(text = "Run in Background") {

    }

    PermissionsLayout(text = "Notification") {

    }


}
