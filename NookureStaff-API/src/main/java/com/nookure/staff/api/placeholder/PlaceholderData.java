package com.nookure.staff.api.placeholder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.jetbrains.annotations.NotNull;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface PlaceholderData {
    /**
     * Get the key of the placeholder
     *
     * <p>
     * For example if the key is server_count
     * the placeholder would be %nkstaff_server_count%
     * </p>
     *
     * @return the key of the placeholder
     */
    @NotNull String value();
}
