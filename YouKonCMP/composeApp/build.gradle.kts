import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.serialization)
    alias(libs.plugins.googleServices)
    alias(libs.plugins.compose.compiler)
}

val javaVersion = JavaVersion.VERSION_17

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = javaVersion.toString()
            }
        }
    }

    jvm("desktop")

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        outputModuleName.set("YouKon")
        browser {
            val rootDirPath = project.rootDir.path
            val projectDirPath = project.projectDir.path
            commonWebpackConfig {
                outputFileName = "youkon.js"
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    static = (static ?: mutableListOf()).apply {
                        // Serve sources to debug inside browser
                        add(rootDirPath)
                        add(projectDirPath)
                    }
                }
            }
        }
        binaries.executable()
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {
        val commonMain by getting
        val androidMain by getting
        val desktopMain by getting
        val wasmJsMain by getting
        wasmJsMain.dependsOn(commonMain)

        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting

        val sharedRevenueCatMain by creating {
            dependsOn(commonMain)
            dependencies {
                // Add the purchases-kmp dependencies.
                implementation(libs.purchases.core)
                implementation(libs.purchases.ui)
                implementation(libs.purchases.datetime)   // Optional
                implementation(libs.purchases.either)     // Optional
                implementation(libs.purchases.result)     // Optional
            }
        }
        val sharedFirebaseMain by creating {
            dependsOn(commonMain)
            dependencies {
                implementation(libs.gitlive.firebase.auth)
                //implementation(libs.gitlive.firebase.crashlytics)
                implementation(libs.gitlive.firebase.perf)
                implementation(libs.gitlive.firebase.firestore)
                implementation(libs.gitlive.firebase.common)
            }
        }

        val iosMain by creating {
            dependsOn(sharedRevenueCatMain)
            dependsOn(sharedFirebaseMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }

        androidMain.dependsOn(sharedRevenueCatMain)
        androidMain.dependsOn(sharedFirebaseMain)
        desktopMain.dependsOn(sharedFirebaseMain)

        androidMain.dependencies {
            implementation(libs.compose.ui.tooling.preview)
            implementation(libs.androidx.activity.compose)
            //implementation(project.dependencies.platform("com.google.firebase:firebase-bom:latest")) // Use the latest Firebase BoM
            //implementation(libs.firebase.appcheck.playintegrity)
            implementation(libs.review.ktx)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(libs.navigation.compose)
            implementation(libs.material.icons.core)
            implementation(libs.material.icons.extended)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.lifecycle.viewmodel.compose)

            // Add the FileKit support for image selection
            implementation(libs.filekit.core)
            implementation(libs.filekit.dialogs)
            implementation(libs.filekit.dialogs.compose)
            implementation(libs.filekit.coil)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
        }
    }

}

android {
    namespace = "com.dcengineer.youkon"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        applicationId = "com.dcengineer.youkon"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 131
        versionName = "1.3.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    buildFeatures {
        //compose = true
    }
    compileOptions {
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
    }
    dependencies {
        debugImplementation(libs.compose.ui.tooling)
    }
}
dependencies {
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.core.ktx)
    implementation(compose.material3)
    implementation(libs.androidx.constraintlayout.compose)
    implementation(libs.androidx.ui.tooling.preview.android)

    implementation(libs.firebase.firestore)
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.dcengineer.youkon"
            packageVersion = "1.0.0"
        }
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}
