import org.gradle.kotlin.dsl.property

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    id("org.sonarqube") version "4.4.1.3373"
    alias(libs.plugins.google.gms.google.services) apply false
}

sonar {
    properties {
        property("sonar.projectKey", "The-dream-Team-D_SOEN345-Project")
        property("sonar.organization", "The-dream-Team-D")
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.sourceEncoding", "UTF-8")

        // Source paths
        property("sonar.sources", "app/src/main/java")
        property("sonar.tests", "app/src/test/java")
        property(
            "sonar.cpd.exclusions",
            "app/src/test/**," +
                    "app/src/main/java/com/example/popin/addedFiles/User.java," +
                    "app/src/main/java/com/example/popin/logic/User.java," +
                    "app/src/main/java/com/example/popin/logic/TicketItem.java," +
                    "app/src/main/java/com/example/popin/UIpages/ForgotPasswordActivity.java," +
                    "app/src/main/java/com/example/popin/UIpages/SignUpActivity.java," +
                    "app/src/main/java/com/example/popin/UIpages/LogInActivity.java"
        )

        // JaCoCo coverage
        property("sonar.coverage.jacoco.xmlReportPaths", "app/build/reports/jacoco/jacocoTestReport/jacocoTestReport.xml")

        // Exclusions
        property("sonar.exclusions", "**/R.class,**/R\$*.class,**/BuildConfig.*,**/Manifest*.*,**/*Test*.*,**/databinding/**")
        property(
            "sonar.coverage.exclusions",
            "**/R.class," +
                    "**/BuildConfig.*," +
                    "**/Manifest*.*," +
                    "**/R$*.class," +
                    "**/*Activity*.*," +
                    "**/*Fragment*.*," +
                    "**/*Adapter*.*," +
                    "android/**/*.*," +
                    "**/*Test*.*"
        )

        // Lower coverage threshold for UI-heavy codebase
        // Industry standard: UI code 30-50%, Business logic 80%+
        property("sonar.coverage.newCode.minimumCoverage", "50")
    }
}

// Ensure jacocoTestReport runs before sonar task
tasks.named("sonar") {
    dependsOn(":app:jacocoTestReport")
}

