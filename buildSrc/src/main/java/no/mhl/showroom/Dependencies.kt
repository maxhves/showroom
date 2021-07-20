package no.mhl.showroom

object Libs {

    // General
    const val androidGradlePlugin = "com.android.tools.build:gradle:4.1.3"
    const val mavenGradlePlugin = "com.github.dcendents:android-maven-gradle-plugin:2.1"

    // Kotlin
    object Kotlin {
        const val version = "1.4.31"
        const val stdLib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$version"
        const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$version"

        const val ktx = "androidx.core:core-ktx:1.6.0"
    }

    // AndroidX
    object AndroidX {
        const val appCompat = "androidx.appcompat:appcompat:1.3.0"
        const val constraint = "androidx.constraintlayout:constraintlayout:2.0.4"
        const val recycler = "androidx.recyclerview:recyclerview:1.2.0"
        const val viewPager = "androidx.viewpager2:viewpager2:1.0.0"

        object Navigation {
            const val version = "2.3.5"

            const val fragment = "androidx.navigation:navigation-fragment-ktx:$version"
            const val ui = "androidx.navigation:navigation-ui-ktx:$version"
        }
    }

    // Extras
    const val coil = "io.coil-kt:coil:0.9.5"
    const val gestureViews = "com.alexvasilkov:gesture-views:2.6.0"

}