/**
 * This piece of code is a processor that processes the Addon annotation
 * and generates the necessary data for the addon to be loaded.
 * <p>
 * This code is inspired by the Velocity Plugin Annotations Processor
 * </p>
 */
package com.nookure.staff.ap.addon;

import com.google.auto.service.AutoService;
import com.nookure.staff.api.addons.annotations.Addon;
import java.io.IOException;
import java.io.Writer;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

@AutoService(Processor.class)
@SupportedAnnotationTypes({"com.nookure.staff.api.addons.annotations.Addon"})
public class AddonAnnotationProcessor extends AbstractProcessor {
    private String pluginClassFound;
    private boolean warnedAboutMultiplePlugins;

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (roundEnv.processingOver()) {
            return false;
        }

        for (Element element : roundEnv.getElementsAnnotatedWith(Addon.class)) {
            if (element.getKind() != ElementKind.CLASS) {
                processingEnv
                        .getMessager()
                        .printMessage(Diagnostic.Kind.ERROR, "Only classes can be annotated with @Addon", element);
                return false;
            }

            Name qualifiedName = ((TypeElement) element).getQualifiedName();

            if (Objects.equals(pluginClassFound, qualifiedName.toString())) {
                if (!warnedAboutMultiplePlugins) {
                    processingEnv
                            .getMessager()
                            .printMessage(
                                    Diagnostic.Kind.WARNING,
                                    "NookureStaff does not yet currently support "
                                            + "multiple addons. We are using " + pluginClassFound
                                            + " for your addon's main class.");
                    warnedAboutMultiplePlugins = true;
                }
                return false;
            }

            Addon addon = element.getAnnotation(Addon.class);

            if (addon == null) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Addon annotation is null", element);
                return false;
            }

            pluginClassFound = qualifiedName.toString();
            try {
                FileObject resource = processingEnv
                        .getFiler()
                        .createResource(StandardLocation.CLASS_OUTPUT, "", "addon.properties", element);

                try (Writer writer = resource.openWriter()) {
                    writer.write(String.format("""
              main-class=%s
              uuid=%s
              """, pluginClassFound, UUID.randomUUID()));
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return false;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
