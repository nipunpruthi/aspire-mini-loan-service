package learn.nipun.aspireminiloanservice.loan;

import java.time.LocalDate;
import java.util.UUID;
import learn.nipun.aspireminiloanservice.loan.dto.LoanApplyDto;
import learn.nipun.aspireminiloanservice.loan.dto.LoanApprovalRequestDto;
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
                .pendingAmount(loanRequest.getAmount())
                .pendingTerms(loanRequest.getLoanTerm())
                .status(LoanStatus.PENDING)
                .loanAppliedDate(LocalDate.now())
                .loanApprovedDate(null)
                .build();
    }

    public static LoanApply toLoanApply(LoanApplyDto loanApplyDto, String userName) {

        return LoanApply.builder()
                .userName(userName)
                .amount(loanApplyDto.getAmount())
                .loanTerm(loanApplyDto.getLoanTerm())
                .build();
    }

    public static ScheduledPayment toScheduledPayment(Loan loan, LocalDate dueDate, Double amount, int termNo) {

        return ScheduledPayment.builder()
                .id(UUID.randomUUID())
                .loanId(loan.getId())
                .termNo(termNo)
                .scheduledPaymentDate(dueDate)
                .actualPaymentDate(null)
                .dueAmount(amount)
                .receivedAmount(amount)
                .status(PaymentStatus.PENDING)
                .build();
    }

    public static LoanApprovalRequest toLoanApprovalRequest(LoanApprovalRequestDto requestDto, String adminId){
        return LoanApprovalRequest.builder()
                .adminId(adminId)
                .loanId(requestDto.getLoanId())
                .status(LoanStatus.valueOf(requestDto.getDecision().name()))
                .build();
    }

}
