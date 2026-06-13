plugins {
  id("net.kyori.blossom") version "2.1.0"
  alias(libs.plugins.grgit)
  alias(libs.plugins.grgitPublish)
  alias(libs.plugins.ebean)
  id("java-library")
  id("maven-publish")
}

val major: String by project
val minor: String by project
val patch: String by project

java {
  withJavadocJar()
  withSourcesJar()

  sourceSets["main"].java {
    srcDir("src/ap/java")
  }
}

dependencies {
  implementation(libs.configurateYaml)
  compileOnly(libs.protocol.buffers.lite)
  compileOnlyApi(libs.paperApi)
  compileOnlyApi(libs.guava)
  compileOnlyApi(libs.guice)
  compileOnlyApi(libs.apacheCommons)
  compileOnlyApi(libs.jedis)
  compileOnlyApi(libs.adventureApi)
  compileOnlyApi(libs.miniMessage)
  compileOnlyApi(libs.reflections)
  compileOnlyApi(rootProject.libs.adventureApi)
  compileOnlyApi(rootProject.libs.miniMessage)
  compileOnlyApi(rootProject.libs.caffeine)
  compileOnlyApi(rootProject.libs.ebean)
  compileOnlyApi(rootProject.libs.placeholderApi)
  annotationProcessor(rootProject.libs.ebean)

  compileOnly(libs.auto.service.annotations)
  compileOnly(rootProject.libs.liblyBukkit)
  annotationProcessor(libs.auto.service)
  annotationProcessor(libs.google.auto.value.processor)
  implementation(libs.google.auto.value.annotations)

  testImplementation(libs.guice)
  testImplementation(libs.gson)
}

publishing {
  repositories {
    maven {
      name = "local"
      url = uri("${rootProject.rootDir}/maven")
    }
  }

  publications {
    create<MavenPublication>("maven") {
      groupId = "com.nookure.staff"
      artifactId = "NookureStaff-API"
      version = "${rootProject.version}"
      from(components["java"])
    }
  }
}

gitPublish {
  repoUri = "https://github.com/Nookure/maven.git"
  branch = "main"
  fetchDepth = null
  commitMessage = "NookureStaff ${rootProject.version}"

  contents {
    from("${rootProject.rootDir}/maven")
  }

  preserve {
    include("**")
  }
}

tasks.test {
  useJUnitPlatform()
}

tasks {
  withType<Javadoc> {
    val o = options as StandardJavadocDocletOptions
    o.encoding = "UTF-8"
    o.source = "21"

    o.links(
      "https://guava.dev/releases/${libs.guava.get().version}/api/docs/",
      "https://docs.oracle.com/en/java/javase/21/docs/api/",
      "https://google.github.io/guice/api-docs/${libs.guice.get().version}/javadoc/",
      "https://jd.advntr.dev/api/${libs.adventureApi.get().version}/",
      "https://javadoc.io/doc/com.github.ben-manes.caffeine/caffeine",
      "https://jd.papermc.io/paper/1.20.6/"
    )
  }
}

sourceSets {
  main {
    blossom {
      javaSources {
        property("version", rootProject.version.toString())
        property("versionCode", "${major}.${minor}.${patch}")
        property("branch", grgit.branch.current().name)
        property("commit", grgit.head().abbreviatedId)
        property("time", System.currentTimeMillis().toString())
        property("nookureInventoryVersion", rootProject.libs.nookure.core.inventory.get().version)
        property("jenkinsBuildNumber", System.getenv("BUILD_NUMBER") ?: "-1")
        property("jenkinsBuildId", System.getenv("BUILD_ID") ?: "-1")
        property("jenkinsBuildUrl", System.getenv("BUILD_URL") ?: "https://ci.nookure.com")
      }
    }
  }
}

ebean {
  debugLevel = 2
}