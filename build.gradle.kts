import Build_gradle.DependencyVersion.APACHE_HTTP_CLIENT_VERSION
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    val SPRING_BOOT_VERSION = "2.7.3"
    val SPRING_DEPENDENCY_MANAGEMENT_VERSION = "1.0.13.RELEASE"
    val JVM_VERSION = "1.6.21"
    val KOTLIN_VERSION = "1.4.30"
    val PLUGIN_SPRING_VERSION = "1.6.21"

    id("org.springframework.boot") version SPRING_BOOT_VERSION
    id("io.spring.dependency-management") version SPRING_DEPENDENCY_MANAGEMENT_VERSION
    id("maven-publish")
    kotlin("jvm") version JVM_VERSION
    kotlin("kapt") version KOTLIN_VERSION
    kotlin("plugin.spring") version PLUGIN_SPRING_VERSION

    idea
}

group = "com.goofy"
version = "0.0.1-snapshot"
java.sourceCompatibility = JavaVersion.VERSION_11

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

object DependencyVersion {
    const val APACHE_HTTP_CLIENT_VERSION = "4.5.13"
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.apache.httpcomponents:httpclient:$APACHE_HTTP_CLIENT_VERSION")

    developmentOnly("org.springframework.boot:spring-boot-devtools")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
}

idea {
    module {
        val kaptMain = file("build/generated/source/kapt/main")
        sourceDirs.add(kaptMain)
        generatedSourceDirs.add(kaptMain)
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<Wrapper> {
    gradleVersion = "7.4.2"
}

tasks.jar {
    enabled = true
}

tasks.bootJar {
    enabled = false
}

java {
    withJavadocJar()
    withSourcesJar()
}

publishing {
    publications {
        create<MavenPublication>("bootJava") {
            from(components["java"])
        }
    }

    repositories {
        upload(subPath())
    }
}

fun RepositoryHandler.upload(path: String): MavenArtifactRepository {
    val repository = uri("https://github.com/DongGeon0908/goofy-maven-repository/$path")

    return maven {
        url = repository
    }
}

fun Build_gradle.subPath(): String {
    if (version.toString().endsWith("SNAPSHOT")) {
        return "snapshots"
    }
    return "releases"
}
