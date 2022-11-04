package net.awazone.awazoneproject.model.user.awazoneUser;

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
@Table(name = "address_table")
public class AwazoneUserAddress {

    @Id
    @SequenceGenerator(
            name = "address_id_generator",
            sequenceName = "address_id_generator",
            allocationSize = 1
    )
    @GeneratedValue(
            generator = "address_id_generator",
            strategy = GenerationType.SEQUENCE
    )
    @Column(name = "address_identity")
    private Long id;
    private String street;
    private String country;
    private String houseAddress;
    private String province;
}
