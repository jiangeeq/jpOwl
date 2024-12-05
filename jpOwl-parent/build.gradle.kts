plugins {
    `java-platform`
    `maven-publish`
    signing
}

group = "com.youpeng.jpowl"
version = "1.0.0-SNAPSHOT"

// 依赖版本管理
javaPlatform {
    allowDependencies()
}

dependencies {
    constraints {
        // Spring Boot 相关
        api("org.springframework.boot:spring-boot-starter:2.7.18")
        api("org.springframework.boot:spring-boot-autoconfigure:2.7.18")
        api("org.springframework.boot:spring-boot-configuration-processor:2.7.18")
        api("org.springframework.boot:spring-boot-starter-test:2.7.18")
        api("org.springframework:spring-aspects:5.3.31")
        api("org.springframework:spring-webmvc:5.3.31")
        
        // 日志相关
        api("org.slf4j:slf4j-api:2.0.9")
        api("ch.qos.logback:logback-classic:1.5.6")
        api("ch.qos.logback:logback-core:1.5.6")
        api("ch.qos.logback:logback-access:1.4.14")
        
        // 工具库
        api("com.google.protobuf:protobuf-java:3.19.1")
        api("io.netty:netty-all:4.1.68.Final")
        api("org.reflections:reflections:0.9.12")
        api("com.lmax:disruptor:3.4.2")
        api("net.jodah:failsafe:2.4.4")
        
        // 测试相关
        api("org.junit.jupiter:junit-jupiter:5.10.0")
        api("org.mockito:mockito-core:4.5.1")
        
        // 验证相关
        api("org.hibernate.validator:hibernate-validator:6.2.0.Final")
        api("javax.validation:validation-api:2.0.1.Final")
    }
}

// 子项目通用配置
subprojects {
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")
    apply(plugin = "signing")

    group = "com.youpeng.jpowl"
    version = "1.0.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }

    // Java编译配置
    configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
        withJavadocJar()
        withSourcesJar()
    }

    // 测试配置
    tasks.withType<Test> {
        useJUnitPlatform()
    }

    // 发布配置
    publishing {
        publications {
            create<MavenPublication>("mavenJava") {
                from(components["java"])
                
                pom {
                    name.set("jpOwl ${project.name}")
                    description.set("A monitoring framework for Spring Boot applications")
                    url.set("https://github.com/yourusername/jpowl")
                    
                    licenses {
                        license {
                            name.set("The Apache License, Version 2.0")
                            url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                        }
                    }
                    
                    developers {
                        developer {
                            id.set("yourusername")
                            name.set("Your Name")
                            email.set("your.email@example.com")
                        }
                    }
                    
                    scm {
                        connection.set("scm:git:git://github.com/yourusername/jpowl.git")
                        developerConnection.set("scm:git:ssh://github.com/yourusername/jpowl.git")
                        url.set("https://github.com/yourusername/jpowl")
                    }
                }
            }
        }
        
        repositories {
            maven {
                val releasesRepoUrl = "https://oss.sonatype.org/service/local/staging/deploy/maven2/"
                val snapshotsRepoUrl = "https://oss.sonatype.org/content/repositories/snapshots/"
                url = uri(if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl)
                
                credentials {
                    username = project.findProperty("ossrhUsername") as String? ?: ""
                    password = project.findProperty("ossrhPassword") as String? ?: ""
                }
            }
        }
    }

    // 签名配置
    signing {
        sign(publishing.publications["mavenJava"])
    }
}

// 质量检查配置
tasks.register("checkstyle") {
    group = "verification"
    description = "Runs Checkstyle verification"
}

tasks.register("spotbugs") {
    group = "verification"
    description = "Runs SpotBugs analysis"
}