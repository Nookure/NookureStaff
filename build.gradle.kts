plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "4.0.4"
}

group = "es.angelillo15"
version = "2.0.0"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://oss.sonatype.org/content/repositories/central")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks.shadowJar {
    relocate("es.angelillo15.configmanager", "es.angelillo15.mast.libs.config.manager")
    relocate("org.yaml.snakeyaml", "es.angelillo15.mast.libs.snakeyaml")
    relocate("org.simpleyaml", "es.angelillo15.mast.libs.simpleyaml")
}

dependencies {
    implementation(project(":MAStaff-API"))
    implementation(project(":MAStaff-Bukkit"))
    implementation("org.yaml:snakeyaml:1.33")
    implementation("com.github.Carleslc.Simple-YAML:Simple-Yaml:1.8.3")
    implementation("com.github.Angelillo15:ConfigManager:1.4")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

allprojects {
    apply(plugin = "java")

    repositories {
        mavenCentral()
        maven("https://jitpack.io")
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    dependencies {
        compileOnly("org.projectlombok:lombok:1.18.24")
        annotationProcessor("org.projectlombok:lombok:1.18.24")
    }
}