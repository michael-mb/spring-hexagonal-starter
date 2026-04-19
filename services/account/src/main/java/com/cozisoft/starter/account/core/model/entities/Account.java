package com.cozisoft.starter.account.core.model.entities;

import com.cozisoft.starter.shared.core.model.db.BaseEntity;
import com.cozisoft.starter.shared.core.model.db.PhoneNumber;
import com.cozisoft.starter.shared.core.model.db.PhoneNumberConverter;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Objects;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@Table(name = "account", schema = "account_service")
public class Account extends BaseEntity {
    @Column(name = "idp_user_id", unique = true, nullable = false)
    private String idpUserId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email", unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private AccountStatus status = AccountStatus.ACTIVE;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Convert(converter = PhoneNumberConverter.class)
    @Column(name = "phone_number")
    private PhoneNumber phoneNumber;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "address_id")
    private AccountAddress address;

    @Transient
    private AccountCreationState creationState;

    public String getFullName() {
        if (Objects.isNull(this.firstName) && Objects.isNull(this.lastName)) {
            return null;
        }
        if (Objects.nonNull(this.firstName) && Objects.nonNull(this.lastName)) {
            return this.firstName + " " + this.lastName;
        }
        if (Objects.nonNull(this.firstName)) {
            return this.firstName;
        }
        return this.lastName;
    }

    public AccountCreationState getCreationState() {
        if (this.getFirstName() == null
                || this.getLastName() == null
                || this.getPhoneNumber() == null
                || this.getAddress() == null
                || this.getAddress().isEmpty()) {
            return AccountCreationState.INCOMPLETE;
        }
        return AccountCreationState.COMPLETE;
    }
}
