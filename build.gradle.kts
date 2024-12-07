

plugins {
    id("java")
}

// 所有项目通用配置
allprojects {
    group = "com.youpeng.jpowl"
    version = "1.0.0-SNAPSHOT"

    // 应用Java和Maven发布插件到所有子项目
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")

    // 配置Java编译选项
    java {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
//        withJavadocJar()
        withSourcesJar()
    }

    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
    }
}

// 简化的 Javadoc 配置
//tasks.withType<Javadoc> {
//    enabled = false
//    options.encoding = "UTF-8"
//}

// 配置编码
tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

// 子项目的公共依赖和配置
subprojects {
    dependencies {
        testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
        testImplementation("org.mockito:mockito-core:4.5.1")

        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.2")

        implementation("org.apache.commons:commons-lang3:3.12.0")
        implementation("com.google.guava:guava:31.1-jre")
    }

    tasks.test {
        useJUnitPlatform()
    }
}

// 根项目不需要生成JAR
tasks.jar {
    enabled = false
}
