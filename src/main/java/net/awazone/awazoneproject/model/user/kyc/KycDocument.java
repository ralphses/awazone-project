package net.awazone.awazoneproject.model.user.kyc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.awazone.awazoneproject.model.user.awazoneUser.AwazoneUser;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.CascadeType.DETACH;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.SEQUENCE;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KycDocument {

    @Id
    @GeneratedValue(generator = "kyc_id_generator", strategy = SEQUENCE)
    @SequenceGenerator(name = "kyc_id_generator", sequenceName = "kyc_id_generator", allocationSize = 1)
    private Long kycId;

    @OneToOne(fetch = EAGER, cascade = DETACH)
    private AwazoneUser awazoneUser;

    @Enumerated(STRING)
    private KycStatus kycStatus;

    private String documentType;
    private String documentName;
    private String downloadUri;
    private Long size;

    private LocalDateTime dateUploaded;
    private LocalDateTime dateVerified;
}










//    @Lob
//    private byte[] imageData;
