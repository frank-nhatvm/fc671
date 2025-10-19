package com.fatherofapps.fc671

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.FirebaseOptions
import dev.gitlive.firebase.initialize
import kotlinx.coroutines.launch


fun initFirebaseForWeb() {
    val options = FirebaseOptions(
        apiKey = "AIzaSyCfu822WX4if6zOqmv5dJpkJvFMhcKZ5P0",
        authDomain = "fc671-d5326.firebaseapp.com",         // optional but recommended for web
        projectId = "fc671-d5326",
        applicationId = "1:157816090799:web:079d8232cb332685fb645a",
        storageBucket = "fc671-d5326.firebasestorage.app",
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