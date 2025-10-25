package com.fatherofapps.fc671.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration

@Composable
fun FUnderlineButton(modifier: Modifier = Modifier,title : String, onClick: () -> Unit) {
    Box(modifier = modifier.clickable(
        onClick = onClick
    ), contentAlignment = Alignment.Center) {
        Text(
            title,
            style = MaterialTheme.typography.bodyMedium.copy(textDecoration = TextDecoration.Underline),
        )
    }
}