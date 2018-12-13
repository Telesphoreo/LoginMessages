package me.telesphoreo.commands;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

// Credit to TF

@Retention(RetentionPolicy.RUNTIME)
public @interface CommandParameters
{
    String description();

    String usage();

    String aliases() default "";
}