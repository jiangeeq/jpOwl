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
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.springframework.boot:spring-boot-starter-test:2.7.18")
    testImplementation("org.mockito:mockito-core:4.5.1")
    testImplementation("org.slf4j:slf4j-simple:2.0.13")
    implementation("org.slf4j:slf4j-api:2.0.9")
    implementation("com.google.protobuf:protobuf-java:3.19.1")
    implementation("io.netty:netty-all:4.1.68.Final")
    implementation("org.springframework.boot:spring-boot-autoconfigure:2.7.18")
    implementation("org.springframework:spring-aspects:5.3.31")
    implementation("org.reflections:reflections:0.9.12")
    implementation("ch.qos.logback:logback-classic:1.5.6")  //如果需要额外的Logback功能
    implementation("ch.qos.logback:logback-core:1.5.6")
    implementation("ch.qos.logback:logback-access:1.4.14")
    implementation("com.lmax:disruptor:3.4.2")
    implementation("net.jodah:failsafe:2.4.4")
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
