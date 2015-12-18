package com.svv.core;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

/**
 * @author Vasyl Spachynskyi
 * @version $Id:
 * @since 17.12.2015
 */
@Retention(RUNTIME) @Target({FIELD, METHOD, TYPE})
public @interface Relation {
    String NO_RELATION = "";

    String value() default NO_RELATION;

    String collection() default NO_RELATION;
}