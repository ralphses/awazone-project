package net.awazone.awazoneproject.utility.validation.annotations;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@PreAuthorize("hasAnyAuthority(" +
        "'ROLE_SUPPER_ADMIN_LIST', " +
        "'ROLE_SUPPER_ADMIN_CREATE', " +
        "'ROLE_SUPPER_ADMIN_EDIT', " +
        "'ROLE_SUPPER_ADMIN_UPDATE'" +
        ",'ROLE_SUPPER_ADMIN_DELETE'," +
        "'ROLE_NATIONAL_ADMIN_CREATE'," +
        "'ROLE_NATIONAL_ADMIN_EDIT', " +
        "'ROLE_NATIONAL_ADMIN_UPDATE', " +
        "'ROLE_NATIONAL_ADMIN_DELETE'" +
        ",'ROLE_NATIONAL_ADMIN_LIST', " +
        "'ROLE_STATE_ADMIN_CREATE', " +
        "'ROLE_STATE_ADMIN_EDIT', " +
        "'ROLE_STATE_ADMIN_UPDATE', " +
        "'ROLE_STATE_ADMIN_DELETE'" +
        ", 'ROLE_STATE_ADMIN_LIST', " +
        "'ROLE_LOCAL_ADMIN_CREATET', " +
        "'ROLE_LOCAL_ADMIN_EDIT', " +
        "'ROLE_LOCAL_ADMIN_UPDATE', " +
        "'ROLE_LOCAL_ADMIN_DELETE', " +
        "'ROLE_LOCAL_ADMIN_LIST'" +
        ",'ROLE_PARTNER_ADMIN_CREATE', " +
        "'ROLE_PARTNER_ADMIN_EDIT', " +
        "'ROLE_PARTNER_ADMIN_UPDATE', " +
        "'ROLE_PARTNER_ADMIN_DELETE', " +
        "'ROLE_PARTNER_ADMIN_LIST'" +
        ", 'ROLE_ADMIN_CREATE', " +
        "'ROLE_ADMIN_EDIT', " +
        "'ROLE_ADMIN_UPDATE', " +
        "'ROLE_ADMIN_DELETE', " +
        "'ROLE_ADMIN_LIST'," +
        "'ROLE_CUSTOMER_CREATE'," +
        "'ROLE_CUSTOMER_LIST'," +
        "'ROLE_CUSTOMER_VIEW'" +
        ", 'ROLE_CUSTOMER_EDIT'" +
        ", 'ROLE_CUSTOMER_DELETE'" +
        ", 'ROLE_CUSTOMER_REQUEST'" +
        ", 'ROLE_CUSTOMER_EXPIRED_SUBSCRIPTION', 'PAYMENT_GATEWAY_SETUP'" +
        ", 'ROLE_PAYMENT_GATEWAY_CONFIG'" +
        ", 'ROLE_PAYMENT_GATEWAY_ADD','DOMAIN_CREATE'" +
        ", 'ROLE_DOMAIN_EDIT'" +
        ", 'ROLE_USER_DOMAIN_EDIT'" +
        ", 'ROLE_DOMAIN_LIST'" +
        ", 'ROLE_DOMAIN_DELETE'" +
        ", 'ROLE_ORDER_CREATE'" +
        ", 'ROLE_ORDER_EDIT'" +
        ", 'ROLE_ORDER_DELETE'" +
        ", 'ROLE_ORDER_LIST'" +
        ", 'ROLE_ORDER_VIEW'" +
        ", 'ROLE_PLAN_CREATE'" +
        ", 'ROLE_PLAN_EDIT'" +
        ", 'ROLE_PLAN_SHOW'" +
        ", 'ROLE_PLAN_UPDATE'" +
        ", 'ROLE_PLAN_DELETE'" +
        ", 'ROLE_PLAN_LIST'" +
        ", 'ROLE_ROLES_CREATE'" +
        ", 'ROLE_ROLES_EDIT'" +
        ", 'ROLE_ROLES_UPDATE'" +
        ", 'ROLE_ROLES_DELETE'" +
        ", 'ROLE_ROLES_LIST'" +
        ", 'ROLE_UPLOADED_FILES_CONTROL'" +
        ", 'ROLE_UPLOADED_FILES_DIRECTORY_CONTROL'" +
        ", 'ROLE_PRODUCT_CONTROL'" +
        ", 'ROLE_INVOICES_CONTROL'" +
        ", 'ROLE_KYC_APPROVE'" +
        ", 'ROLE_KYC_VIEW'" +
        ", 'ROLE_KYC_REJECT'" +
        ", 'ROLE_KYC_VERIFY'" +
        ", 'ROLE_KYC_ADD'" +
        ", 'ROLE_SMS_CREATE'" +
        ", 'ROLE_SMS_DELETE'" +
        ", 'ROLE_SMS_GATEWAY_EDIT'" +
        ", 'ROLE_SMS_GATEWAY_CREATE'" +
        ", 'ROLE_SMS_GATEWAY_ADD'" +
        ", 'ROLE_SMS_GATEWAY_VIEW'" +
        ", 'ROLE_EMAIL_CREATE'" +
        ", 'ROLE_EMAIL_DELETE'" +
        ", 'ROLE_EMAIL_GATEWAY_EDIT'" +
        ", 'ROLE_EMAIL_GATEWAY_CREATE'" +
        ", 'ROLE_EMAIL_GATEWAY_ADD'" +
        ", 'ROLE_EMAIL_GATEWAY_VIEW'" +
        ", 'ROLE_EMAIL_GATEWAY_LIST'" +
        ", 'ROLE_SMS_GATEWAY_LIST'" +
        ", 'ROLE_SITE_SETTINGS'" +
        ", 'ROLE_MARKETING_TOOLS')")
@Target(METHOD)
public @interface SuperAdmin {
}
