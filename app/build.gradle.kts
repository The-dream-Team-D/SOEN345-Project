import org.gradle.testing.jacoco.plugins.JacocoPluginExtension
import org.gradle.testing.jacoco.tasks.JacocoCoverageVerification
import org.gradle.testing.jacoco.tasks.JacocoReport

plugins {
    alias(libs.plugins.android.application)
    id("jacoco")


}

configure<JacocoPluginExtension> {
    toolVersion = "0.8.11"
}

android {
    namespace = "com.example.popin"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.popin"
        minSdk = 25
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {

        getByName("debug") {
            enableUnitTestCoverage = true
            enableAndroidTestCoverage = false   //This android E2E tests are not needed for the checks rn
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation("androidx.core:core-splashscreen:1.2.0")
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}

jacoco {
    toolVersion = "0.8.11"
}

val jacocoTestReport = tasks.register<JacocoReport>("jacocoTestReport") {
    group = "Reporting"
    description = "Generate Jacoco coverage reports for the debug build."
    dependsOn("testDebugUnitTest")

    reports {
        xml.required.set(true)
        html.required.set(true)
    }

    val classDirectoriesTree = fileTree(layout.buildDirectory.dir("intermediates/javac/debug")) {
        exclude(
            "**/R.class",
            "**/BuildConfig.*",
            "**/Manifest*.*",
            "**/R$*.class",
            "**/*Activity*.*",    // Excludes MainActivity and any other Activity
            "**/*Fragment*.*",    // Excludes UI Fragments
            "**/*Adapter*.*",     // Excludes RecyclerView Adapters (they are UI)
            "android/**/*.*",      // Excludes Android system classes
            "**/*Test*.*"          // Excludes any tests
        )
    }

    sourceDirectories.from(files("src/main/java"))
    classDirectories.from(classDirectoriesTree)
    executionData.from(fileTree(layout.buildDirectory) {
        include("outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec")
    })

    doLast {
        val reportFile = file("${layout.buildDirectory.get()}/reports/jacoco/jacocoTestReport/html/index.html")
        if (reportFile.exists()) {
            println("-------------------------------------------------------")
            println("COVERAGE REPORT GENERATED: ${reportFile.absolutePath}")
            println("-------------------------------------------------------")
        }
    }
}

val jacocoTestCoverageVerification = tasks.register<JacocoCoverageVerification>("jacocoTestCoverageVerification") {
    group = "Verification"
    description = "Verifies code coverage; fails if below 80%."
    dependsOn(jacocoTestReport)

    violationRules {
        rule {
            limit {
                counter = "LINE"
                value = "COVEREDRATIO"
                minimum = 0.80.toBigDecimal() //80% coverage goal
            }
        }
    }

    //EXCLUSIONS
    val classDirectoriesTree = fileTree(layout.buildDirectory.dir("intermediates/javac/debug")) {
        exclude(
            "**/R.class",
            "**/BuildConfig.*",
            "**/Manifest*.*",
            "**/R$*.class",
            "**/*Activity*.*",    // Excludes MainActivity and any other Activity
            "**/*Fragment*.*",    // Excludes UI Fragments
            "**/*Adapter*.*",     // Excludes RecyclerView Adapters (they are UI)
            "android/**/*.*",      // Excludes Android system classes
            "**/*Test*.*"          // Excludes any tests
        )
    }

    sourceDirectories.from(files("src/main/java"))
    classDirectories.from(classDirectoriesTree)
    executionData.from(fileTree(layout.buildDirectory) {
        include("outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec")
    })
}

// 3. Link to standard 'check' task
tasks.named("check") {
    dependsOn(jacocoTestCoverageVerification)
}