package net.awazone.awazoneproject.utility.validation.annotations;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@PreAuthorize("hasAnyAuthority(" +
        " 'ROLE_USER'" +
        ", 'ROLE_USER_DOMAIN_EDIT'" +
        ", 'ROLE_CUSTOMER_ORDER_VIEW'" +
        ", 'ROLE_ORDER_CREATE'" +
        ", 'ROLE_ORDER_EDIT'" +
        ", 'ROLE_ORDER_LIST'" +
        ", 'ROLE_KYC_ADD'" +
        ", 'ROLE_PLAN_ADD')")
@Target(METHOD)
public @interface IsUser {
}
