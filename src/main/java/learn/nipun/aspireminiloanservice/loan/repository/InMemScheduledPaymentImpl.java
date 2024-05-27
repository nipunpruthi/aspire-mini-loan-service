//package learn.nipun.aspireminiloanservice.loan.repository;
//
//import java.util.*;
//import learn.nipun.aspireminiloanservice.loan.entity.Loan;
//import learn.nipun.aspireminiloanservice.loan.model.LoanFilter;
//import learn.nipun.aspireminiloanservice.loan.model.ScheduledPayment;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Repository;
//
//@Repository
//@RequiredArgsConstructor
//public class InMemScheduledPaymentImpl implements ScheduledPaymentRepository {
//
//    private final LoanRepository loanRepository;
//    private Map<UUID, List<ScheduledPayment>> loanToScheduledPayments = new HashMap<>();
//
//    @Override
//    public void save(ScheduledPayment scheduledPayment) {
//
//        if (!loanToScheduledPayments.containsKey(scheduledPayment.getLoanId())) {
//            loanToScheduledPayments.put(scheduledPayment.getLoanId(), new ArrayList<>());
//        }
//
//        loanToScheduledPayments.get(scheduledPayment.getLoanId()).add(scheduledPayment);
//    }
//
//    @Override
//    public List<ScheduledPayment> findAll(LoanFilter loanFilter) {
//
//        List<Loan> loans = loanRepository.findAll(loanFilter);
//        List<ScheduledPayment> scheduledPayments = new ArrayList<>();
//
//        for (Loan loan : loans) {
//            scheduledPayments.addAll(loanToScheduledPayments.get(loan.getId()));
//        }
//        return scheduledPayments;
//
//    }
//}
