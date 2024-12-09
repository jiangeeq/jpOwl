plugins {
    id("java")
}

group = "org.youpeng"
version = "1.0-SNAPSHOT"
java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}
repositories {
    mavenCentral()
}

dependencies {
    api("org.slf4j:slf4j-api:1.7.32")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.0")
    implementation("org.apache.commons:commons-lang3:3.12.0")
    
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
}

tasks.test {
    useJUnitPlatform()
}

tasks.jar {
    manifest {
        attributes(
            mapOf("Main-Class" to "com.youpeng.jpowl.JpOwlApplication")
        )
    }
}
