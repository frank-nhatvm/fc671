package com.fatherofapps.fc671.components

import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.unit.dp

@Composable
fun FScaffold(
    modifier: Modifier = Modifier,
    isScrollable: Boolean = true,
    topBar: @Composable () -> Unit = {},
    content: @Composable ColumnScope.() -> Unit
) {


    Column(
        modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
    ) {
        topBar()

        if(isScrollable) {
            Column(
                modifier = Modifier.weight(1f).padding(start = 16.dp, end = 16.dp, top = 12.dp).verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start,
            ) {
                content()
            }
        }
        else{
            Column(
                modifier = Modifier.weight(1f).padding(start = 16.dp, end = 16.dp, top = 12.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start,
            ) {
                content()
            }
        }


    }
}