import net.minecrell.pluginyml.paper.PaperPluginDescription

plugins {
  alias(libs.plugins.pluginYmlPaper)
}

group = "com.nookure.staff"

dependencies {
  implementation(project(":NookureStaff-API"))
  implementation(project(":NookureStaff-Common"))
  compileOnly(libs.paperApi)
  compileOnly(libs.superVanish)
  compileOnly(libs.libbyPaper)
  compileOnly(libs.configurateYaml)
  compileOnly(libs.jedis)
  compileOnly(libs.placeholderApi)
  compileOnly(libs.lucko.commodore)
  compileOnly(libs.nookure.core.inventory)
  compileOnly(libs.lucko.luckperms)
  compileOnly(libs.neznamy.tab.api)
  paperLibrary(libs.guice)
}

paper {
  name = "NookureStaff"
  version = rootProject.version.toString()
  description = "A staff plugin by Nookure"
  apiVersion = "1.21"
  website = "https://lunna.dev/"
  authors = listOf("Lunna5")
  loader = "com.nookure.staff.paper.bootstrap.StaffPaperPluginLoader"
  main = "com.nookure.staff.paper.bootstrap.StaffBootstrapper"
  generateLibrariesJson = true
  serverDependencies {
    register("PlaceholderAPI") {
      required = false
      load = PaperPluginDescription.RelativeLoadOrder.BEFORE
      joinClasspath = true
    }
    register("SuperVanish") {
      required = false
      load = PaperPluginDescription.RelativeLoadOrder.BEFORE
      joinClasspath = true
    }
    register("PremiumVanish") {
      required = false
      load = PaperPluginDescription.RelativeLoadOrder.BEFORE
      joinClasspath = true
    }
    register("LuckPerms") {
      required = false
      load = PaperPluginDescription.RelativeLoadOrder.BEFORE
      joinClasspath = true
    }
    register("TAB") {
      required = false
      load = PaperPluginDescription.RelativeLoadOrder.BEFORE
      joinClasspath = true
    }
  }
}