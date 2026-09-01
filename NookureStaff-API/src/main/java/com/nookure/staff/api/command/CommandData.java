package com.nookure.staff.api.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandData {
    String name();

    String[] aliases() default {};

    String permission() default "";

    String usage() default "";

    String description() default "";

    Class<? extends Command>[] subCommands() default {};
}
