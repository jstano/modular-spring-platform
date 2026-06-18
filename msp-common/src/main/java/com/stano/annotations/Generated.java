package com.stano.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks source code that has been generated. Classes annotated with this are excluded from JaCoCo
 * test coverage reports (requires CLASS retention for JaCoCo to detect it at analysis time).
 *
 * <h2>Examples:</h2>
 *
 * <pre>
 *   &#064;Generated("com.example.Generator")
 * </pre>
 *
 * <pre>
 *   &#064;Generated(value="com.example.Generator", date="2017-07-04T12:08:56.235-0700")
 * </pre>
 *
 * <pre>
 *   &#064;Generated(value="com.example.Generator", date="2017-07-04T12:08:56.235-0700",
 *      comments="comment 1")
 * </pre>
 */
@Documented
@Retention(RetentionPolicy.CLASS)
@Target({
  ElementType.PACKAGE,
  ElementType.TYPE,
  ElementType.METHOD,
  ElementType.CONSTRUCTOR,
  ElementType.FIELD,
  ElementType.LOCAL_VARIABLE,
  ElementType.PARAMETER
})
public @interface Generated {

  /** Fully qualified name of the code generator. */
  String[] value();

  /** ISO 8601 date when the source was generated (e.g. {@code 2017-07-04T12:08:56.235-0700}). */
  String date() default "";

  /** Optional comments from the code generator. */
  String comments() default "";
}
