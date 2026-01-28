package com.congty9a4.backend.annotation.profileTagName;

import org.hibernate.annotations.ValueGenerationType;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Custom annotation for Profile tag name generation.
 * Uses ProfileTagNameGenerator to generate tag names with pattern: @{username}{sequence}
 * This annotation is used on non-ID fields to auto-generate unique identifiers.
 */
@ValueGenerationType(generatedBy = ProfileTagNameGenerator.class)
@Retention(RUNTIME)
@Target({FIELD, METHOD})
public @interface ProfileTagName {
}

