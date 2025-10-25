package com.fatherofapps.fc671.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.browser.window
import org.jetbrains.compose.resources.painterResource
import fc671.composeapp.generated.resources.Res
import fc671.composeapp.generated.resources.ic_copy
import kotlinx.coroutines.delay


@Composable
fun CopyToClipboardButton(modifier: Modifier = Modifier,textToCopy: String) {
    var copied by remember { mutableStateOf(false) }

    Row(horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically,
        ) {
        IconButton(modifier = modifier, onClick = {
            window.navigator.clipboard.writeText(textToCopy)
            copied = true
        }) {
            Icon(painter = painterResource(Res.drawable.ic_copy), contentDescription = "Copy")
        }
        Spacer(Modifier.width(4.dp))
        if (copied) {
            LaunchedEffect(copied) {
                delay(3000)
                copied = false
            }
            Text("Copied!", style = MaterialTheme.typography.labelSmall)
        }
    }
}
