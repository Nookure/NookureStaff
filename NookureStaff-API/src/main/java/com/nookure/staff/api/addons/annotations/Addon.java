package com.nookure.staff.api.addons.annotations;

import com.nookure.staff.api.command.Command;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Addon {
    String name();

    String version();

    String author();

    String description() default "";

    boolean loadOnScan() default true;

    AddonPlatform platform() default AddonPlatform.COMMON;

    Class<?>[] listeners() default {};

    Class<? extends Command>[] commands() default {};

    enum AddonPlatform {
        BUKKIT,
        VELOCITY,
        BUNGEECORD,
        COMMON
    }
}
