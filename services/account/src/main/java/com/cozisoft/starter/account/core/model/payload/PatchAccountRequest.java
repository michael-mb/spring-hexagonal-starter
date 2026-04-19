package com.cozisoft.starter.account.core.model.payload;

import com.cozisoft.starter.account.core.model.entities.Gender;
import com.cozisoft.starter.shared.core.model.db.PhoneNumber;
import com.cozisoft.starter.shared.core.model.payload.GenericAddressPayload;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatchAccountRequest {
    private String firstName;
    private String lastName;
    private Gender gender;
    private PhoneNumber phoneNumber;
    private String email;
    private GenericAddressPayload address;
}
