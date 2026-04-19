package com.cozisoft.starter.business.api.model;

import com.cozisoft.starter.account.core.model.entities.Account;
import com.cozisoft.starter.account.core.model.payload.PatchAccountRequest;
import com.cozisoft.starter.business.config.CentralMapperConfiguration;
import com.cozisoft.starter.business.core.model.UpdateAccountStatusRequest;
import com.cozisoft.starter.dto.AccountDto;
import com.cozisoft.starter.dto.PatchAccountRequestDto;
import com.cozisoft.starter.dto.PhoneNumberDto;
import com.cozisoft.starter.dto.UpdateAccountStatusRequestDto;
import com.cozisoft.starter.shared.core.model.db.PhoneNumber;
import org.mapstruct.Mapper;

@Mapper(config = CentralMapperConfiguration.class, uses = {CommonMapper.class})
public interface AccountMapper {
    AccountDto map(Account account);
    PhoneNumber map(PhoneNumberDto phoneNumber);
    PhoneNumberDto map(PhoneNumber phoneNumber);
    PatchAccountRequest map(PatchAccountRequestDto request);
    UpdateAccountStatusRequest map(UpdateAccountStatusRequestDto request);
}
