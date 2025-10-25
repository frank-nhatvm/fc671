package com.fatherofapps.fc671

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.FirebaseOptions
import dev.gitlive.firebase.initialize
import kotlinx.coroutines.launch


fun initFirebaseForWeb() {
    val options = FirebaseOptions(
        apiKey = AppConfig.APIKEY,
        authDomain = AppConfig.AUTH_DOMAIN,
        projectId = AppConfig.PROJECT_ID,
        applicationId = AppConfig.APPLICATION_ID,
        storageBucket = AppConfig.STORAGE_BUCKET,
    )

    // This initializer style is used in examples / community posts for GitLive SDK:
    Firebase.initialize(Unit, options)
}


@OptIn(ExperimentalComposeUiApi::class)
fun main() {

    kotlinx.coroutines.GlobalScope.launch {
        initFirebaseForWeb()
        ComposeViewport {
            App()
        }
    }
//    initFirebaseForWeb()
//    ComposeViewport {
//        App()
//    }
}