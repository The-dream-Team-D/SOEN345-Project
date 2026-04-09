import org.gradle.testing.jacoco.tasks.JacocoCoverageVerification
import org.gradle.testing.jacoco.tasks.JacocoReport
import java.util.Properties

val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(localPropertiesFile.inputStream())
}

plugins {
    alias(libs.plugins.android.application)
    id("jacoco")
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.popin"
    compileSdk = 36

    buildFeatures {
        buildConfig = true
    }

    testOptions {
        animationsDisabled = true
    }

    defaultConfig {
        applicationId = "com.example.popin"
        minSdk = 25
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        val myKey = localProperties.getProperty("SENDGRID_API_KEY") ?: ""
        val myKey1 = localProperties.getProperty("TWILIO_ACCOUNT_SID") ?: ""
        val myKey2 = localProperties.getProperty("TWILIO_AUTH_TOKEN") ?: ""
        val myKey3 = localProperties.getProperty("TWILIO_PHONE_NUMBER") ?: ""

        buildConfigField("String", "SENDGRID_API_KEY", "\"$myKey\"")
        buildConfigField("String", "TWILIO_ACCOUNT_SID", "\"$myKey1\"")
        buildConfigField("String", "TWILIO_AUTH_TOKEN", "\"$myKey2\"")
        buildConfigField("String", "TWILIO_PHONE_NUMBER", "\"$myKey3\"")


        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("debug") {
            enableUnitTestCoverage = true
            enableAndroidTestCoverage = false   //This android E2E tests are not needed for the checks rn
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
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
    testOptions {
        unitTests.isIncludeAndroidResources = true
        unitTests.all {
            it.jvmArgs("-Dnet.bytebuddy.experimental=true")
            it.extensions.configure(org.gradle.testing.jacoco.plugins.JacocoTaskExtension::class) {
                isIncludeNoLocationClasses = true
                excludes = listOf(
                    "jdk.internal.*",
                    "sun.security.smartcardio.*"
                )
            }
        }
    }
}

jacoco {
    toolVersion = "0.8.13"
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
            "**/service",           // Excludes service since it just uses API calls
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
                minimum = 0.8.toBigDecimal()
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
            "**/service",
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


dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.core:core-splashscreen:1.2.0")
    implementation(libs.firebase.database)
    implementation("com.github.bumptech.glide:glide:4.16.0")


    testImplementation(libs.junit)
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.mockito:mockito-core:5.14.2")

    testImplementation("org.robolectric:robolectric:4.12.2")
    testImplementation("androidx.test:core:1.6.1")
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}

tasks.register<JacocoReport>("jacocoDebugUnitTestReport") {
    dependsOn("testDebugUnitTest")

    val excludes = listOf(
        "**/R.class",
        "**/R$*.class",
        "**/BuildConfig.*",
        "**/Manifest*.*"
    )

    classDirectories.setFrom(
        fileTree(layout.buildDirectory.dir("intermediates/javac/debug/compileDebugJavaWithJavac/classes")) {
            exclude(excludes)
        }
    )
    sourceDirectories.setFrom(files("src/main/java"))
    executionData.setFrom(
        files(
            layout.buildDirectory.file("outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec")
        )
    )

    reports {
        xml.required.set(true)
        html.required.set(true)
    }
}
