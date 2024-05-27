package learn.nipun.aspireminiloanservice.loan.dto;

import java.time.LocalDate;
import java.util.UUID;
import learn.nipun.aspireminiloanservice.loan.model.LoanStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class LoanDto {

    private UUID id;
    private String customerId;
    private Double amount;
    private Integer loanTerm;
    private Double installmentAmount;
    private Double pendingAmount;
    private Integer pendingTerms;
    private LoanStatus status;
    private LocalDate loanAppliedDate;
    private LocalDate loanApprovedDate;
}
