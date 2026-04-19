package com.cozisoft.starter.account.core.model.entities;

import lombok.Getter;

@Getter
public enum AccountStatus {
    ACTIVE("active"),
    SUSPENDED("suspended"),
    DELETED("deleted");

    private final String status;

    AccountStatus(String status) {
        this.status = status;
    }

    public static AccountStatus fromString(String status) {
        for (AccountStatus accountStatus : AccountStatus.values()) {
            if (accountStatus.status.equalsIgnoreCase(status)) {
                return accountStatus;
            }
        }
        throw new IllegalArgumentException("No constant with status " + status + " found");
    }
}
