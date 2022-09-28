package net.awazone.awazoneproject.utility.event;

import lombok.Getter;
import lombok.Setter;
import net.awazone.awazoneproject.model.userService.awazoneUser.AwazoneUser;
import org.springframework.context.ApplicationEvent;

@Setter
@Getter
public class RegistrationCompleteEvent extends ApplicationEvent {
    private AwazoneUser awazoneUser;
    private String urlLink;

    public RegistrationCompleteEvent(AwazoneUser awazoneUser, String urlLink) {
        super(awazoneUser);
        this.awazoneUser = awazoneUser;
        this.urlLink = urlLink;
    }
}
