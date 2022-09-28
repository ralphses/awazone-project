package net.awazone.awazoneproject.utility.event;

import lombok.Getter;
import net.awazone.awazoneproject.model.userService.awazoneUser.AwazoneUser;
import org.springframework.context.ApplicationEvent;

@Getter
public class PasswordResetEvent extends ApplicationEvent {
    private AwazoneUser awazoneUser;
    private String passwordResetLink;

    public PasswordResetEvent(AwazoneUser awazoneUser, String passwordResetLink) {
        super(awazoneUser);
        this.passwordResetLink = passwordResetLink;
        this.awazoneUser = awazoneUser;
    }
}
