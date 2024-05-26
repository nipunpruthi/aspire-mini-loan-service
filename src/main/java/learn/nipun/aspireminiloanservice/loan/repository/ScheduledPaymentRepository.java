package learn.nipun.aspireminiloanservice.loan.repository;

import java.util.List;
import learn.nipun.aspireminiloanservice.loan.model.LoanFilter;
import learn.nipun.aspireminiloanservice.loan.model.ScheduledPayment;

public interface ScheduledPaymentRepository {

    void save(ScheduledPayment scheduledPayment);
    List<ScheduledPayment> findAll(LoanFilter loanFilter);
}
