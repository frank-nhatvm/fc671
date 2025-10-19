package com.fatherofapps.fc671

import dev.gitlive.firebase.initialize
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.FirebaseOptions


class JsPlatform: Platform {
    override val name: String = "Web with Kotlin/JS"
}

actual fun getPlatform(): Platform = JsPlatform()



