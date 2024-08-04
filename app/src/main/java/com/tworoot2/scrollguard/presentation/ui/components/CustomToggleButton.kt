package com.tworoot2.scrollguard.presentation.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

//
//val DarkPink = Color(0XFFFF597B)
//val Pink = Color(0XFFFF8E9E)
//val LightPink = Color(0XFFF9B5D0)
//val Background = Color(0XFFEEEEEE)

@Composable
fun CheckCircle(
    modifier: Modifier = Modifier,

    ) {

    Card(
        shape = CircleShape,
        modifier = modifier.size(15.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
        Box(modifier = Modifier.background(Color.White))
    }

}


@SuppressLint("NotConstructor")
@Composable
fun CustomToggleButton(
    selected: Boolean,
    modifier: Modifier = Modifier,
    selectedColor: Color,
    unselectedColor: Color,
    onUpdate: (Boolean) -> Unit,

) {

    Card(
        modifier = modifier
            .width(45.dp)
            .clickable {
                onUpdate(!selected)
            }, shape = RoundedCornerShape(16.dp), elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
        Box(
            modifier = Modifier
                .background(
                    if (selected) selectedColor else unselectedColor.copy(0.4f)
                )
                .fillMaxWidth(),
            contentAlignment = if (selected) Alignment.TopEnd else Alignment.TopStart,

            ) {
            CheckCircle(modifier = Modifier.padding(5.dp))
        }
    }

}
