package com.nookure.staff.api.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NookSubscribe {
    /**
     * The event priority
     *
     * @return The event priority
     */
    EventPriority priority() default EventPriority.NORMAL;
}
