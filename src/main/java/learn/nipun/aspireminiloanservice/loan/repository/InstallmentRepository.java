package learn.nipun.aspireminiloanservice.loan.repository;

import java.util.List;
import java.util.UUID;
import learn.nipun.aspireminiloanservice.loan.entity.Installment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstallmentRepository extends JpaRepository<Installment, UUID> {

    Installment save(Installment installment);

    List<Installment> findAllByLoanId(UUID loanId);

    Installment findTop1ByLoanIdOrderByScheduledPaymentDateDesc(UUID loanId);

}
