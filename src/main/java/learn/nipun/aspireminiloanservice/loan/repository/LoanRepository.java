package learn.nipun.aspireminiloanservice.loan.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import learn.nipun.aspireminiloanservice.loan.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepository extends JpaRepository<Loan, UUID> {

    Optional<Loan> findById(UUID loanId);

    Loan save(Loan loan);
    List<Loan> findAll();

//    List<Loan> findAll(LoanFilter loanFilter);

    List<Loan> findAllByCustomerId(String customerId);

}
