package learn.nipun.aspireminiloanservice.loan;

import java.time.LocalDate;
import java.util.UUID;
import learn.nipun.aspireminiloanservice.loan.dto.LoanApplyDto;
import learn.nipun.aspireminiloanservice.loan.dto.LoanApprovalRequestDto;
import learn.nipun.aspireminiloanservice.loan.dto.LoanDto;
import learn.nipun.aspireminiloanservice.loan.entity.Loan;
import learn.nipun.aspireminiloanservice.loan.model.*;
import lombok.experimental.UtilityClass;

@UtilityClass
public class LoanMapper {

    public static Loan toLoan(LoanApply loanRequest) {

        return Loan.builder()
                .id(UUID.randomUUID())
                .customerId(loanRequest.getUserName())
                .amount(loanRequest.getAmount())
                .loanTerm(loanRequest.getLoanTerm())
                .installmentAmount(loanRequest.getAmount()/loanRequest.getLoanTerm())
                .pendingAmount(loanRequest.getAmount())
                .pendingTerms(loanRequest.getLoanTerm())
                .status(LoanStatus.PENDING)
                .loanAppliedDate(LocalDate.now())
                .loanApprovedDate(null)
                .updatedBy(null)
                .build();
    }

    public static LoanApply toLoanApply(LoanApplyDto loanApplyDto, String userName) {

        return LoanApply.builder()
                .userName(userName)
                .amount(loanApplyDto.getAmount())
                .loanTerm(loanApplyDto.getLoanTerm())
                .build();
    }

    public static Installment toScheduledPayment(Loan loan, LocalDate dueDate, Double amount, int termNo) {

        return Installment.builder()
                .id(UUID.randomUUID())
                .loanId(loan.getId())
                .termNo(termNo)
                .scheduledPaymentDate(dueDate)
                .actualPaymentDate(null)
                .pendingAmount(amount)
                .receivedAmount(amount)
                .status(PaymentStatus.PENDING)
                .build();
    }

    public static LoanApprovalRequest toLoanApprovalRequest(LoanApprovalRequestDto requestDto, String adminId) {

        return LoanApprovalRequest.builder()
                .adminId(adminId)
                .loanId(requestDto.getLoanId())
                .status(LoanStatus.valueOf(requestDto.getDecision().name()))
                .build();
    }

    public static LoanDto toLoanDto(Loan loan) {
        return LoanDto.builder()
                .id(loan.getId())
                .customerId(loan.getCustomerId())
                .amount(loan.getAmount())
                .loanTerm(loan.getLoanTerm())
                .installmentAmount(loan.getInstallmentAmount())
                .pendingAmount(loan.getPendingAmount())
                .pendingTerms(loan.getPendingTerms())
                .status(LoanStatus.valueOf(loan.getStatus().name()))
                .loanAppliedDate(loan.getLoanAppliedDate())
                .loanApprovedDate(loan.getLoanApprovedDate())
                .build();
    }

}
