package com.fatherofapps.fc671

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform