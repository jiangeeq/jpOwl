plugins {
    id("java")
}

group = "com.youpeng.jpowl"
version = "1.0.0-SNAPSHOT"


dependencies {
    implementation(project(":jpOwl-core"))

    implementation(libs.disruptor)
    implementation(libs.slf4j.api)
    implementation(libs.logback.classic)
    // 各种输出源的依赖设为compileOnly，由使用方决定使用哪些实现
    compileOnly("co.elastic.clients:elasticsearch-java:8.12.1")
    compileOnly("org.mongodb:mongodb-driver-sync:4.11.1")
    compileOnly("com.influxdb:influxdb-client-java:6.10.0")
    
    // 通用依赖
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.3")
    implementation("io.netty:netty-buffer:4.1.68.Final")
    implementation("io.micrometer:micrometer-core:1.12.0")
    implementation("org.apache.commons:commons-lang3:3.12.0")

    testImplementation("org.testcontainers:testcontainers:1.19.3")
    testImplementation("org.testcontainers:junit-jupiter:1.19.3")
    testImplementation(libs.junit.jupiter)
    testImplementation("org.testcontainers:elasticsearch:1.19.3")
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
    // 跳过测试
    enabled = false
}

tasks.jar {
    manifest {
        attributes(
            "Implementation-Title" to project.name,
            "Implementation-Version" to project.version
        )
    }
    
    // 确保SPI配置文件被包含在jar中
    from("src/main/resources") {
        include("META-INF/services/**")
    }
}