package com.fatherofapps.fc671.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import fc671.composeapp.generated.resources.Res
import fc671.composeapp.generated.resources.arrow_back
import org.jetbrains.compose.resources.painterResource

@Composable
fun FTopBar(modifier: Modifier = Modifier, title: String, onBack: (() -> Unit)? = null) {
    Row(modifier =modifier.background(color = Color.White).padding(vertical = 12.dp, horizontal = 16.dp)) {
        onBack?.let {
            IconButton(onClick = onBack) {
                Icon(
                    painterResource(Res.drawable.arrow_back),
                    contentDescription = "Add player",
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Text(
            text = title, modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.headlineLarge.copy(textAlign = TextAlign.Center)
        )
    }
}