package learn.nipun.aspireminiloanservice.loan.model;

import java.time.LocalDate;
import java.util.UUID;
import lombok.*;

@Getter
@Builder(toBuilder = true)
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Loan {
    private UUID id;
    private String customerId;
    private Double amount;
    private Integer loanTerm;
    private Double pendingAmount;
    private Integer pendingTerms;
    private LoanStatus status;
    private LocalDate loanAppliedDate;
    private LocalDate loanApprovedDate;

}
