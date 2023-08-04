package com.sofka.practicaMambu.domain.seedWork.mappers;

import com.sofka.practicaMambu.domain.activeProducts.dto.CreateLoanAccountCommand;
import com.sofka.practicaMambu.domain.model.activeProducts.LoanAccount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LoanAccountMapper {

    LoanAccountMapper INSTANCE = Mappers.getMapper( LoanAccountMapper.class );
    @Mapping(source = "interestRate", target = "interestSettings.interestRate")
    @Mapping(source = "installmentsNumber", target = "scheduleSettings.repaymentInstallments")
    LoanAccount toLoanAccount(CreateLoanAccountCommand loanAccountCommand);
}
