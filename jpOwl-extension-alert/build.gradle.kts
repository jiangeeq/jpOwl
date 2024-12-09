plugins {
    id("java")
}

group = "org.youpeng"
version = "1.0-SNAPSHOT"


dependencies {
    implementation(project(":jpOwl-core"))
    implementation(libs.slf4j.api)
    implementation ("com.fasterxml.jackson.core:jackson-databind:2.13.0") // 请使用最新版本

    // 邮件发送依赖
    compileOnly("org.springframework.boot:spring-boot-starter-mail")
    // 钉钉SDK依赖
    compileOnly("com.aliyun:alibaba-dingtalk-service-sdk")
    // HTTP客户端依赖
    compileOnly("org.apache.httpcomponents:httpclient")
    
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
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