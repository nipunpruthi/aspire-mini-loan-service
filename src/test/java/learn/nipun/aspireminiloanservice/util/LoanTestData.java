package learn.nipun.aspireminiloanservice.util;

import java.time.LocalDate;
import java.util.UUID;
import learn.nipun.aspireminiloanservice.loan.entity.Loan;
import learn.nipun.aspireminiloanservice.loan.entity.Installment;
import learn.nipun.aspireminiloanservice.loan.model.LoanStatus;
import learn.nipun.aspireminiloanservice.loan.model.PaymentStatus;

public class LoanTestData {

    public static Loan aLoan() {

        return Loan.builder()
                .id(UUID.randomUUID())
                .customerId("user1")
                .amount(100.0)
                .loanTerm(5)
                .installmentAmount(20.0)
                .pendingTerms(5)
                .status(LoanStatus.PENDING)
                .loanAppliedDate(LocalDate.now())
                .loanApprovedDate(null)
                .pendingAmount(100.0)
                .updatedBy(null)
                .build();
    }

    public static Installment anInstallment() {

        return Installment.builder()
                .id(UUID.randomUUID())
                .loanId(UUID.randomUUID())
                .termNo(1)
                .scheduledPaymentDate(LocalDate.now().plusDays(7))
                .actualPaymentDate(null)
                .pendingAmount(100.0)
                .receivedAmount(null)
                .status(PaymentStatus.PENDING)
                .build();
    }

}
