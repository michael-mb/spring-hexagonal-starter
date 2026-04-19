package com.cozisoft.starter.account.core.model.entities;

import com.cozisoft.starter.shared.core.model.db.GenericAddress;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = "address", schema = "account_service")
public class AccountAddress extends GenericAddress {
    @Override
    public String toString() {
        return super.toString();
    }
}
