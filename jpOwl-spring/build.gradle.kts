plugins {
    id("java")
}

group = "com.youpeng.jpowl"
version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
}


dependencies {
    implementation(project(":jpOwl-core"))
    implementation(project(":jpOwl-extension-logging"))
} 