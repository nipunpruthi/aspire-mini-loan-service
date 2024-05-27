package learn.nipun.aspireminiloanservice.loan.repository;

import java.util.List;
import java.util.UUID;
import learn.nipun.aspireminiloanservice.loan.model.Installment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstallmentRepository extends JpaRepository<Installment, UUID> {

    Installment save(Installment installment);

    List<Installment> findAllByLoanId(UUID loanId);

    Installment findTop1ByLoanIdOrderByScheduledPaymentDateDesc(UUID loanId);

//    List<ScheduledPayment> findAllByCustomerIdAndLoanId(String customerId, UUID loanId);


//    @Query(value = """
//            select sp from ScheduledPayment SP where sp.loanId=:loanFilter.getloanId()""")
//    List<ScheduledPayment> findAll(LoanFilter loanFilter);
}
