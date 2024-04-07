import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.serialization)
    alias(libs.plugins.googleServices)
}

val javaVersion = JavaVersion.VERSION_11

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = javaVersion.toString()
            }
        }
    }
    
    jvm("desktop")
    
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
        val desktopMain by getting
        
        androidMain.dependencies {
            implementation(libs.compose.ui.tooling.preview)
            implementation(libs.androidx.activity.compose)
            implementation(project.dependencies.platform("com.google.firebase:firebase-bom:32.7.2"))
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.gitlive.firebase.auth)
            //implementation(libs.gitlive.firebase.crashlytics)
            implementation(libs.gitlive.firebase.perf)
            implementation(libs.gitlive.firebase.firestore)
            implementation(libs.gitlive.firebase.common)
            //implementation("androidx.compose.material:material-icons-extended:1.6.0")
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
        versionCode = 2
        versionName = "0.2.0"
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

    commonMainApi(libs.mvvm.core) // only ViewModel, EventsDispatcher, Dispatchers.UI
    commonMainApi(libs.mvvm.flow) // api mvvm-core, CFlow for native and binding extensions
    commonMainApi(libs.mvvm.livedata) // api mvvm-core, LiveData and extensions
    commonMainApi(libs.mvvm.state) // api mvvm-livedata, ResourceState class and extensions
    commonMainApi(libs.mvvm.livedata.resources) // api mvvm-core, moko-resources, extensions for LiveData with moko-resources
    commonMainApi(libs.mvvm.flow.resources) // api mvvm-core, moko-resources, extensions for Flow with moko-resources

    // compose multiplatform
    commonMainApi(libs.mvvm.compose) // api mvvm-core, getViewModel for Compose Multiplatform
    commonMainApi(libs.mvvm.flow.compose) // api mvvm-flow, binding extensions for Compose Multiplatform
    commonMainApi(libs.mvvm.livedata.compose) // api mvvm-livedata, binding extensions for Compose Multiplatform

    /*
    androidMainApi("dev.icerock.moko:mvvm-livedata-material:0.16.1") // api mvvm-livedata, Material library android extensions
    androidMainApi("dev.icerock.moko:mvvm-livedata-glide:0.16.1") // api mvvm-livedata, Glide library android extensions
    androidMainApi("dev.icerock.moko:mvvm-livedata-swiperefresh:0.16.1") // api mvvm-livedata, SwipeRefreshLayout library android extensions
    androidMainApi("dev.icerock.moko:mvvm-databinding:0.16.1") // api mvvm-livedata, DataBinding support for Android
    androidMainApi("dev.icerock.moko:mvvm-viewbinding:0.16.1") // api mvvm-livedata, ViewBinding support for Android
    */
    commonTestImplementation(libs.mvvm.test) // test utilities

    implementation(platform("com.google.firebase:firebase-bom:32.7.3"))
    implementation(libs.firebase.common)
    implementation(libs.firebase.auth)
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