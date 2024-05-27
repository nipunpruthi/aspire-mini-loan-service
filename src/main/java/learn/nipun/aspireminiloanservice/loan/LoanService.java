package learn.nipun.aspireminiloanservice.loan;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import learn.nipun.aspireminiloanservice.exception.ResourceNotFoundException;
import learn.nipun.aspireminiloanservice.loan.dto.PayInstallmentDto;
import learn.nipun.aspireminiloanservice.loan.entity.Loan;
import learn.nipun.aspireminiloanservice.loan.model.*;
import learn.nipun.aspireminiloanservice.loan.repository.InstallmentRepository;
import learn.nipun.aspireminiloanservice.loan.repository.LoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static learn.nipun.aspireminiloanservice.loan.LoanMapper.toLoan;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final LoanRepository loanRepository;
    private final InstallmentRepository installmentRepository;
    private final LoanValidator loanValidator;

    public Loan submitNewLoanRequest(LoanApply loanApply) {

        Loan loan = toLoan(loanApply);
        return loanRepository.save(loan);
    }

    public List<Loan> getLoans(String customerUserId) {

        return loanRepository.findAllByCustomerId(customerUserId);
    }

    public List<Loan> getAllLoans(final LoanFilter loanFilter) {

        return loanRepository.findAll();
    }

    public Installment getNextInstallment(String customerUserId, UUID loanId) {

        loanValidator.validateLoanCustomerRelation(customerUserId, loanId);
        loanValidator.validateLoanStatus(loanId, LoanStatus.APPROVED);
        return installmentRepository.findTop1ByLoanIdOrderByScheduledPaymentDateDesc(loanId);
    }


    public Loan loanApproval(LoanApprovalRequest loanApprovalRequest) {

        Optional<Loan> optionalLoan = loanRepository.findById(loanApprovalRequest.getLoanId());
        if (optionalLoan.isEmpty()) {
            throw new ResourceNotFoundException("Loan with id " + loanApprovalRequest.getLoanId() + " not found");
        }
        Loan loan = optionalLoan.get();

        loanValidator.validateLoanStatus(loan.getId(), LoanStatus.PENDING);

        loan = loan.toBuilder()
                .status(loanApprovalRequest.getStatus())
                .loanApprovedDate(LocalDate.now())
                .updatedBy(loanApprovalRequest.getAdminId())
                .build();

        Installment nextInstallment = createNextScheduledPayment(loan, loan.getLoanApprovedDate());
        installmentRepository.save(nextInstallment);
        return loanRepository.save(loan);
    }


    @Transactional
    public Installment payInstallment(String customerUserName, PayInstallmentDto payInstallmentDto) {

        loanValidator.validateLoanCustomerRelation(customerUserName, payInstallmentDto.getLoanId());
        loanValidator.validateLoanStatus(payInstallmentDto.getLoanId(), LoanStatus.APPROVED);
        Loan loan = loanRepository.findById(payInstallmentDto.getLoanId()).get();
        Installment installment = installmentRepository.findTop1ByLoanIdOrderByScheduledPaymentDateDesc(
                payInstallmentDto.getLoanId());
        loanValidator.validateInstallmentStatus(installment.getStatus(), PaymentStatus.REPAID);
        loanValidator.validateAmount(payInstallmentDto.getAmount(), installment.getPendingAmount(),
                loan.getPendingAmount());

        installment = installment.toBuilder()
                .status(PaymentStatus.REPAID)
                .actualPaymentDate(LocalDate.now())
                .receivedAmount(payInstallmentDto.getAmount())
                .build();
        installmentRepository.save(installment);
        int loanTermPending = loan.getPendingTerms();
        double pendingAmount = loan.getPendingAmount() - payInstallmentDto.getAmount();

        LoanStatus status = loan.getStatus();
        if (pendingAmount <= 0) {
            status = LoanStatus.PAID;
        }

        loan = loan.toBuilder()
                .pendingTerms(loanTermPending - 1)
                .status(status)
                .pendingAmount(pendingAmount)
                .build();
        loanRepository.save(loan);

        if (status.equals(LoanStatus.PAID)) {
            return installment;
        }

        Double nextAmount = Math.min(pendingAmount, loan.getInstallmentAmount());

        Installment nextInstallment = createNextScheduledPayment(loan, installment.getScheduledPaymentDate());

        // createNextInstallment
//        Installment nextInstallment = Installment.builder()
//                .id(UUID.randomUUID())
//                .pendingAmount(nextAmount)
//                .termNo(installment.getTermNo() + 1)
//                .scheduledPaymentDate(installment.getScheduledPaymentDate().plusDays(7))
//                .loanId(installment.getLoanId())
//                .receivedAmount(null)
//                .actualPaymentDate(null)
//                .status(PaymentStatus.PENDING)
//                .build();

        installmentRepository.save(nextInstallment);
        return installment;
    }

    private Installment createNextScheduledPayment(Loan loan, LocalDate previousScheduledPaymentDate) {

        Double nextAmount = Math.min(loan.getPendingAmount(), loan.getInstallmentAmount());
        return Installment.builder()
                .id(UUID.randomUUID())
                .pendingAmount(nextAmount)
                .termNo(loan.getLoanTerm() - loan.getPendingTerms() + 1)
                .scheduledPaymentDate(previousScheduledPaymentDate.plusDays(7))
                .loanId(loan.getId())
                .receivedAmount(null)
                .actualPaymentDate(null)
                .status(PaymentStatus.PENDING)
                .build();
    }
}
