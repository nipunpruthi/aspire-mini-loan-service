package learn.nipun.aspireminiloanservice.loan.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import learn.nipun.aspireminiloanservice.loan.model.Loan;
import learn.nipun.aspireminiloanservice.loan.model.LoanFilter;

public interface LoanRepository {

    Optional<Loan> findById(UUID loanId);

    Loan save(Loan loan);

    List<Loan> findAll(LoanFilter loanFilter);

}
