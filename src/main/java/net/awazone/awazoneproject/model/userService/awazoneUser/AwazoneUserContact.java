package net.awazone.awazoneproject.model.userService.awazoneUser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "user_contact")
public class AwazoneUserContact {

    @Id
    @SequenceGenerator(
            name = "contact_id_generator",
            sequenceName = "contact_id_generator",
            allocationSize = 1
    )
    @GeneratedValue(
            generator = "contact_id_generator",
            strategy = GenerationType.SEQUENCE
    )
    @Column(name = "contact_identity")
    private Long contactId;

    @Column(name = "user_email")
    private String email;

    @Column(name = "user_mobile_phone")
    private String mobilePhone;

    @Column(name = "user_other_phone")
    private String otherPhone;
}
