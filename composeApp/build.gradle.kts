import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    kotlin("plugin.serialization")
}

kotlin {
    js(IR) {
        browser{}
        binaries.executable()
    }
    
//    @OptIn(ExperimentalWasmDsl::class)
//    wasmJs {
//        browser()
//        binaries.executable()
//    }
    
    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)


            implementation(libs.gitlive.firebase.firestore)
            implementation(libs.kotlinx.serialization.json)
        }

        val jsMain by getting {
            dependencies {
                implementation(npm("firebase", "10.12.4"))
                implementation(npm("@firebase/firestore", "4.6.2"))
                implementation(npm("@firebase/app", "0.10.1"))
            }
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}


