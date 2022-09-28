package net.awazone.awazoneproject.utility.validation.annotations;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target(METHOD)
@PreAuthorize("hasAuthority('ROLE_SUPER:ADMIN')")
public @interface AdminSuper {
}
