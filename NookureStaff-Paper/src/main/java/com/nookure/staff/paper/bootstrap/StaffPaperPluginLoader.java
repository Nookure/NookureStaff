package com.nookure.staff.paper.bootstrap;

import io.papermc.paper.plugin.loader.PluginClasspathBuilder;
import io.papermc.paper.plugin.loader.PluginLoader;
import io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.RemoteRepository;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("UnstableApiUsage")
public class StaffPaperPluginLoader implements PluginLoader {
    @Override
    public void classloader(@NotNull PluginClasspathBuilder classpathBuilder) {
        MavenLibraryResolver resolver = new MavenLibraryResolver();
        resolver.addDependency(new Dependency(new DefaultArtifact("io.ebean:ebean:15.1.0"), null));
        resolver.addDependency(new Dependency(new DefaultArtifact("io.ebean:ebean-migration:14.0.0"), null));
        resolver.addDependency(new Dependency(new DefaultArtifact("io.ebean:ebean-ddl-generator:15.1.0"), null));
        resolver.addDependency(new Dependency(new DefaultArtifact("com.google.inject:guice:7.0.0"), null));
        resolver.addDependency(
                new Dependency(new DefaultArtifact("com.google.inject.extensions:guice-assistedinject:7.0.0"), null));

        resolver.addRepository(
                new RemoteRepository.Builder("paper", "default", "https://repo.papermc.io/repository/maven-public/")
                        .build());
        resolver.addRepository(
                new RemoteRepository.Builder("nookure", "default", "https://maven.nookure.com/").build());

        classpathBuilder.addLibrary(resolver);
    }
}
