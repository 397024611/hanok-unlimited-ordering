plugins { id("com.android.application") }

android {
    namespace = "com.hanok.printbridge"
    compileSdk = 35
    defaultConfig {
        applicationId = "com.hanok.printbridge"
        minSdk = 26
        targetSdk = 35
        versionCode = 12
        versionName = "1.12"
    }
    buildTypes { release { isMinifyEnabled = false } }
}

// v1.12 workflow trigger via PR
