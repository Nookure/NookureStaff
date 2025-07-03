import com.adarshr.gradle.testlogger.theme.ThemeType

plugins {
  id("java")
  alias(libs.plugins.shadowJar)
  alias(libs.plugins.grgit)
  alias(libs.plugins.runPaper)
  alias(libs.plugins.testLogger)
  alias(libs.plugins.minotaur)
}

val major: String by project
val minor: String by project
val patch: String by project

group = "com.nookure.staff"

val versionCode = "${major}.${minor}.${patch}"

version = "${versionCode}-${grgit.head().abbreviatedId}"

val branch = grgit.branch.current().name

if (System.getenv("nookure_staff_version") != null) {
  version = "${versionCode}-${System.getenv("nookure_staff_version")}"
}

dependencies {
  implementation(project(":NookureStaff-Paper"))
  implementation(project(":NookureStaff-Velocity"))
  implementation(libs.liblyBukkit)
  implementation(libs.libbyPaper)
  implementation(libs.libbyVelocity)
  implementation(libs.configurateYaml)
  implementation(libs.lucko.commodore)
}

tasks.shadowJar {
  val dev = System.getenv("NOOKURE_STAFF_DEV") == "true"

  if (dev) {
    archiveFileName.set("NookureStaff-dev.jar")
  } else {
    archiveFileName.set("NookureStaff-${rootProject.version}.jar")
  }

  dependencies {
    exclude("com/mojang")
  }
  relocate("com.zaxxer", "com.nookure.staff.libs")
  relocate("me.lucko.commodore", "com.nookure.staff.libs.commodore")
  relocate("com.github.benmanes.caffeine", "com.nookure.staff.libs.caffeine")
  relocate("org.spongepowered.configurate", "com.nookure.staff.libs.configurate")
  relocate("com.alessiodp.libby", "com.nookure.staff.libs.libby")
}

allprojects {
  apply<JavaPlugin>()
  apply(plugin = rootProject.libs.plugins.shadowJar.get().pluginId)
  apply(plugin = rootProject.libs.plugins.testLogger.get().pluginId)

  repositories {
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://maven.nookure.com")
    maven("https://mvn.exceptionflug.de/repository/exceptionflug-public/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
    maven("https://repo.dmulloy2.net/repository/public/")
    maven("https://repo.aikar.co/content/groups/aikar/")
  }

  dependencies {
    compileOnly(rootProject.libs.guice)
    compileOnly(rootProject.libs.google.guice.assistedinject)
    compileOnly(rootProject.libs.adventureApi)
    compileOnly(rootProject.libs.miniMessage)
    compileOnly(rootProject.libs.caffeine)
    compileOnly(rootProject.libs.liblyBukkit)
    compileOnly(rootProject.libs.ebean)
    compileOnly(rootProject.libs.auto.service.annotations)
    annotationProcessor(rootProject.libs.auto.service)

    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
  }

  tasks {
    withType<JavaCompile> {
      options.encoding = "UTF-8"
    }

    withType<Javadoc> {
      options.encoding = "UTF-8"
    }
  }

  tasks.test {
    useJUnitPlatform()
  }

  java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
  }

  testlogger {
    theme = ThemeType.MOCHA
  }
}

tasks.withType(xyz.jpenilla.runtask.task.AbstractRun::class) {
  javaLauncher = javaToolchains.launcherFor {
    vendor = JvmVendorSpec.JETBRAINS
    languageVersion = JavaLanguageVersion.of(21)
  }
  jvmArgs("-XX:+AllowEnhancedClassRedefinition", "-XX:+AllowRedefinitionToAddDeleteMethods")
  systemProperties["nkstaff.inventory.replace"] = "true"
  systemProperties["file.encoding"] = "UTF-8"
}

val minecraftVersion: String by project

tasks {
  runServer {
    minecraftVersion(minecraftVersion)
  }
}

modrinth {
  token = System.getenv("MODRINTH_TOKEN")
  projectId = "staff"
  versionNumber.set(rootProject.version.toString())

  versionType = if (branch.startsWith("release/")) {
    "release"
  } else if (branch.startsWith("dev")) {
    "beta"
  } else if (branch.startsWith("feature/") || branch.startsWith("fix/")) {
    "alpha"
  } else {
    System.getenv("MODRINTH_VERSION_TYPE") ?: "release"
  }

  val changeLog = getChangeLog();

  changelog.set(changeLog)
  uploadFile.set(tasks.shadowJar.get().archiveFile)
  gameVersions.addAll("1.19.4", "1.20.6", "1.21", "1.21.4", "1.21.5", "1.21.6", "1.21.7")
  loaders.addAll("paper", "purpur", "velocity")

  syncBodyFrom = rootProject.file("README.md").readText()
}

fun getChangeLog(): String {
  return "${grgit.head().shortMessage} - ${grgit.head().author.name} (${grgit.head().abbreviatedId})"
}

tasks.modrinth {
  dependsOn(tasks.shadowJar)
  dependsOn(tasks.modrinthSyncBody)
}