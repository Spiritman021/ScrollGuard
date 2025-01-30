package com.tworoot2.scrollguard.presentation.ui.screens.main_screen

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.tworoot2.scrollguard.R
import com.tworoot2.scrollguard.domain.models.ApplicationModel
import com.tworoot2.scrollguard.presentation.ui.components.CustomToolBar
import com.tworoot2.scrollguard.presentation.ui.screens.main_screen.specific_components.AllPermissionLayout
import com.tworoot2.scrollguard.presentation.ui.screens.main_screen.specific_components.SelectApplicationLayout
import com.tworoot2.scrollguard.presentation.ui.screens.main_screen.specific_components.TimerProgressLayout
import com.tworoot2.scrollguard.presentation.ui.viewmodels.PermissionsViewModel
import com.tworoot2.scrollguard.presentation.viewmodels.ApplicationDataViewModel


fun getAppIcon(packageName: String, packageManager: PackageManager): Drawable? {
    return try {
        // Get the ApplicationInfo for the given package
        // this method will give the application info of the package
        val applicationInfo: ApplicationInfo = packageManager.getApplicationInfo(packageName, 0)
        // Extract and return the app icon as a Drawable
        packageManager.getApplicationIcon(applicationInfo)
    } catch (e: PackageManager.NameNotFoundException) {
        Log.e("AppIcon", "App not found: $packageName", e)
        null // Return null if the app is not found
    }
}

fun checkApplicationInstalledOrNot(packageName: String, packageManager: PackageManager): Boolean {
    try {
        packageManager.getApplicationInfo(packageName, 0)
        return true
    } catch (e: PackageManager.NameNotFoundException) {
        return false
    }
}

// this function converts drawable to bitmap
fun drawableToBitmap(drawable: Drawable): Bitmap? {
    // first we check if the drawable is already a BitmapDrawable
    if (drawable is BitmapDrawable) {
        return drawable.bitmap
    }
    // if it's not a BitmapDrawable, we need to convert it to one
    // create a new Bitmap with the same dimensions as the drawable
    val bitmap = Bitmap.createBitmap(
        drawable.intrinsicWidth,
        drawable.intrinsicHeight,
        // here we are giving the qualit of the image
        Bitmap.Config.ARGB_8888
    )

    // create a new Canvas with the Bitmap we created
    // and draw the drawable on it
    // working of this code is explained in the comments
    // here is the explanation
    val canvas = android.graphics.Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)
    return bitmap
}

@Composable
fun MainScreenLayout(
    modifier: Modifier = Modifier, permissionsViewModel: PermissionsViewModel,
    applicationDataViewModel: ApplicationDataViewModel
) {

    LaunchedEffect(true) {
        applicationDataViewModel.getAllApplications()
    }

//    applicationDataViewModel.insertApplication(
//        ApplicationModel(
//            "com.instagram.android", "Instagram", "na", false
//        )
//    )

    val context = LocalContext.current


    val packageName = "com.instagram.android" // replace with actual package name
    val appIcon: Drawable? = getAppIcon(packageName, context.packageManager)
    val bitmapIcon = appIcon?.let { drawableToBitmap(it) }

    if (checkApplicationInstalledOrNot(
            "com.instagram.android",
            packageManager = context.packageManager
        )
    ) {
        Log.e("AppIcon", "App found: ")
    } else {
        Log.e("AppIcon", "App not found: ")
    }

    val installedApps = applicationDataViewModel.applications

    Column(
        modifier
            .background(Color.Black)
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(10.dp)
    ) {

        CustomToolBar()

        TimerProgressLayout()

        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Permissions ",
            color = Color.White,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(8.dp))

        AllPermissionLayout(permissionsViewModel)

        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Applications ",
            color = Color.White,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn {
            items(installedApps.size) {
                val app = installedApps[it]
                val appLogo = getAppIcon(app.id, context.packageManager)
                var selected by remember {
                    mutableStateOf(app.isSelected)
                }
                SelectApplicationLayout(
                    text = app.name,
                    selected = selected,
                    image = rememberDrawablePainter(appLogo),
                ) { isChecked ->
                    selected = isChecked
                    applicationDataViewModel.updateApplication(
                        ApplicationModel(
                            id = app.id,
                            name = app.name,
                            viewId = app.viewId,
                            isSelected = isChecked
                        )
                    )
                }

            }
        }


    }
}
