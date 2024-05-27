package learn.nipun.aspireminiloanservice.loan;

import jakarta.validation.ValidationException;
import java.util.Optional;
import java.util.UUID;
import learn.nipun.aspireminiloanservice.exception.InvalidStatusException;
import learn.nipun.aspireminiloanservice.exception.ResourceAccessForbidden;
import learn.nipun.aspireminiloanservice.exception.ResourceNotFoundException;
import learn.nipun.aspireminiloanservice.loan.entity.Loan;
import learn.nipun.aspireminiloanservice.loan.model.LoanStatus;
import learn.nipun.aspireminiloanservice.loan.model.PaymentStatus;
import learn.nipun.aspireminiloanservice.loan.repository.LoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoanValidator {

    private final LoanRepository loanRepository;

    public void validateLoan(UUID loanId) {

        Optional<Loan> optionalLoan = loanRepository.findById(loanId);
        if (optionalLoan.isEmpty()) {
            throw new ResourceNotFoundException("Loan with id " + loanId + " not found");
        }
    }

    public void validateLoanCustomerRelation(String customerUserId, UUID loanId) {

        Optional<Loan> loan = loanRepository.findById(loanId);
        if (loan.isEmpty()) {
            throw new ResourceNotFoundException("The loan with id " + loanId + " does not exist");
        }
        if (!loan.get().getCustomerId().equals(customerUserId)) {
            throw new ResourceAccessForbidden(
                    "The loan with id " + loanId + " does not belong to customer " + customerUserId);
        }
    }

    public void validateLoanStatus(UUID loanId, LoanStatus status) {

        Optional<Loan> loan = loanRepository.findById(loanId);
        if (loan.isEmpty()) {
            throw new ResourceNotFoundException("The loan with id " + loanId + " does not exist");
        }
        if (!loan.get().getStatus().equals(status)) {
            throw new InvalidStatusException(
                    "The loan with id " + loanId + " does not belong to the status " + status);
        }
    }

    public void validateAmount(Double payingAmount, Double installmentAmount, Double loanDueAmount) {

        if (payingAmount < installmentAmount) {
            throw new ValidationException("The paying amount is less than the installment amount " + installmentAmount);
        }

        if (payingAmount > loanDueAmount) {
            throw new ValidationException("The paying amount is greater than the loan due amount " + loanDueAmount);
        }
    }

    public void validateInstallmentStatus(PaymentStatus status, PaymentStatus finalStatus) {

        if (status.equals(finalStatus)) {
            throw new InvalidStatusException(
                    "The intallment payment status " + status + " does not match the required status " + finalStatus);
        }
    }
}
