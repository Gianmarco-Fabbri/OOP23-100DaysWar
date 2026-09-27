plugins {
    // Apply the java plugin to add support for Java
    java

    // Apply the application plugin to add support for building a CLI application
    // You can run your app via task "run": ./gradlew run
    application


    /*
     * Adds tasks to export a runnable jar.
     * In order to create it, launch the "shadowJar" task.
     * The runnable jar will be found in build/libs/projectname-all.jar
     */
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("org.danilopianini.gradle-java-qa") version "1.66.0"
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

repositories { // Where to search for dependencies
    mavenCentral()
}

dependencies {
    // Suppressions for SpotBugs
    compileOnly("com.github.spotbugs:spotbugs-annotations:4.8.6")

    // RxJava 3 for Reactive Programming
    implementation("io.reactivex.rxjava3:rxjava:3.1.8")

    // JUnit API and testing engine
    val jUnitVersion = "5.11.1"
    // when dependencies share the same version, grouping in a val helps to keep them in sync
    testImplementation("org.junit.jupiter:junit-jupiter-api:$jUnitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$jUnitVersion")
}

application {
    // Define the main class for the application.
    mainClass.set("it.unibo.the100dayswar.application.The100DaysWar")
}

tasks.test {
    useJUnitPlatform()
    val testHome = layout.buildDirectory.dir("test-home")
    systemProperty("user.home", testHome.get().asFile.absolutePath)
    doFirst {
        testHome.get().asFile.resolve("Documents").mkdirs()
    }
    testLogging {
        events(*org.gradle.api.tasks.testing.logging.TestLogEvent.values())
        showStandardStreams = true
    }
}
