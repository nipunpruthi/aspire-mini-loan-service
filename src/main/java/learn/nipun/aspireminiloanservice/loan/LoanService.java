package learn.nipun.aspireminiloanservice.loan;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import learn.nipun.aspireminiloanservice.loan.model.*;
import learn.nipun.aspireminiloanservice.loan.repository.LoanRepository;
import learn.nipun.aspireminiloanservice.loan.repository.ScheduledPaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static learn.nipun.aspireminiloanservice.loan.LoanMapper.toLoan;
import static learn.nipun.aspireminiloanservice.loan.LoanMapper.toScheduledPayment;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final LoanRepository loanRepository;
    private final ScheduledPaymentRepository scheduledPaymentRepository;
//    private final PaymentService paymentService;

    private void createScheduledPayments(Loan loan) {

        int loanTerm = loan.getLoanTerm();
        Double amount = loan.getAmount() / loanTerm;

        LocalDate paymentDate = loan.getLoanApprovedDate();

        for (int i = 0; i < loanTerm; i++) {
            paymentDate = paymentDate.plusDays(7);
            ScheduledPayment scheduledPayment = toScheduledPayment(loan, paymentDate, amount, i);
            scheduledPaymentRepository.save(scheduledPayment);
        }
    }

    public Loan submitNewLoanRequest(LoanApply loanApply) {

        Loan loan = toLoan(loanApply);
        return loanRepository.save(loan);
    }

    public List<Loan> getAllLoans(final LoanFilter loanFilter) {

        return loanRepository.findAll(loanFilter);
    }

    public List<ScheduledPayment> getScheduledPayments(final LoanFilter loanFilter) {

        return scheduledPaymentRepository.findAll(loanFilter);
    }


    public Loan loanApproval(LoanApprovalRequest loanApprovalRequest) {

        Optional<Loan> optionalLoan = loanRepository.findById(loanApprovalRequest.getLoanId());
        if (optionalLoan.isEmpty()) {
            throw new RuntimeException();
        }

        Loan loan = optionalLoan.get();
        LoanStatus status = loan.getStatus();
        if (!status.equals(LoanStatus.PENDING)) {
            throw new RuntimeException();
        }

        loan = loan.toBuilder()
                .status(loanApprovalRequest.getStatus())
                .loanApprovedDate(LocalDate.now())
                .build();

        createScheduledPayments(loan);
        return loanRepository.save(loan);
    }

//    public void payLoanInstallment();


}
