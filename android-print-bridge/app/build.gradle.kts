plugins { id("com.android.application") }

android {
    namespace = "com.hanok.printbridge"
    compileSdk = 35
    defaultConfig {
        applicationId = "com.hanok.printbridge"
        minSdk = 26
        targetSdk = 35
        versionCode = 11
        versionName = "1.11"
    }
    buildTypes { release { isMinifyEnabled = false } }
}

// v1.11 build trigger
