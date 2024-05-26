package learn.nipun.aspireminiloanservice.loan.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import learn.nipun.aspireminiloanservice.loan.model.Loan;
import learn.nipun.aspireminiloanservice.loan.model.LoanFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class InMemLoanRepositoryImpl implements LoanRepository {

    private List<Loan> loans = new ArrayList<>();

    @Override
    public Optional<Loan> findById(UUID loanId) {

        System.out.println(loans);
        Loan loan = loans.stream().filter(e -> e.getId().equals(loanId)).findFirst().orElse(null);
        if (loan == null) {
            return Optional.empty();
        }
        return Optional.of(loan);
    }

    @Override
    public Loan save(Loan loan) {

        Optional<Loan> optionalLoan  = findById(loan.getId());
        optionalLoan.ifPresent(value -> loans.remove(value));
        loans.add(loan);
        System.out.println(loans);
        return loan;
    }

    @Override
    public List<Loan> findAll(LoanFilter loanFilter) {

        List<Loan> filteredLoans = loans.stream().toList();
        if (loanFilter.getLoanId() != null) {
            filteredLoans = loans.stream().filter(e -> e.getId().equals(loanFilter.getLoanId())).toList();
        }

        if (loanFilter.getUserName() != null) {
            filteredLoans = filteredLoans.stream().filter(e -> e.getCustomerId().equals(loanFilter.getUserName()))
                    .toList();
        }

        if (loanFilter.getStatus() != null) {
            filteredLoans = filteredLoans.stream().filter(e -> e.getStatus().equals(loanFilter.getStatus())).toList();
        }
        System.out.println(loans);
        return filteredLoans;
    }
}
