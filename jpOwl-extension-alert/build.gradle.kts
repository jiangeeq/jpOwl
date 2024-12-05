plugins {
    id("java")
}

group = "org.youpeng"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":jpOwl-core"))
    
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