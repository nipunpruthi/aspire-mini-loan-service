package learn.nipun.aspireminiloanservice.loan.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.UUID;
import learn.nipun.aspireminiloanservice.loan.model.LoanStatus;
import lombok.*;

@Getter
@Builder(toBuilder = true)
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "loan")
public class Loan {
    @Id
    private UUID id;

    @Column(name = "customer_id", nullable = false)
    private String customerId;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "loan_term", nullable = false)
    private Integer loanTerm;

    @Column(name = "installment_amount", nullable = false)
    private Double installmentAmount;

    @Column(name = "pending_amount", nullable = false)
    private Double pendingAmount;

    @Column(name = "pending_terms", nullable = false)
    private Integer pendingTerms;

    @Column(name = "status", nullable = false)
    private LoanStatus status;

    @Column(name = "loan_applied_date", nullable = false)
    private LocalDate loanAppliedDate;

    @Column(name = "loan_approved_date")
    private LocalDate loanApprovedDate;

    @Column (name = "updated_by")
    private String updatedBy;

}
