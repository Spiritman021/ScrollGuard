package com.tworoot2.scrollguard.presentation.ui.screens.main_screen.specific_components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tworoot2.scrollguard.presentation.ui.components.CustomToggleButton
import com.tworoot2.scrollguard.ui.theme.Ui_1

@Composable
fun SelectApplicationLayout(
    modifier: Modifier = Modifier,
    text: String,
    image: Painter,
    selected: Boolean = false,
    onUpdate: (Boolean) -> Unit,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 4.dp),
        shape = RoundedCornerShape(50.dp)
    ) {
        Row(
            modifier = Modifier
                .background(Ui_1)
                .fillMaxWidth()
                .padding(8.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(10.dp))
            Image(
                painter = image, contentDescription = "Menu",
                modifier = Modifier.size(30.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = text,
                color = Color.White,
                fontSize = 18.sp,
                modifier = Modifier.weight(weight = 1f),
                fontWeight = FontWeight.Normal
            )

            CustomToggleButton(
                selected = selected, selectedColor = Color.Black,
                unselectedColor = Color.DarkGray, onUpdate = onUpdate
            )


        }
    }
}

