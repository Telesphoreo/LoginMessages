package me.telesphoreo.commands;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

// Credit to TF

@Retention(RetentionPolicy.RUNTIME)
public @interface CommandPermissions
{
    SourceType source();

    boolean blockHostConsole() default false;
}