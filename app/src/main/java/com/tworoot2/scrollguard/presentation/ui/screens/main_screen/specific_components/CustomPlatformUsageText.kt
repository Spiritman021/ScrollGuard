package com.tworoot2.scrollguard.presentation.ui.screens.main_screen.specific_components

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.tworoot2.scrollguard.ui.theme.Ui_RED_1

@Composable
fun CustomPlatformUsageText(modifier: Modifier = Modifier) {
    Row(

    ) {
        Text(
            text = "Shorts: ",
            color = Color.White,
            fontSize = 16.sp,
            textAlign = TextAlign.Start,
        )

        Text(
            text = "00h 05m",
            color = Ui_RED_1,
            fontSize = 16.sp,
            textAlign = TextAlign.Start,
        )
    }
}
