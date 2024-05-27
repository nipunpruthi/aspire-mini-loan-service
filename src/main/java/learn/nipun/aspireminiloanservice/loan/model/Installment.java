package learn.nipun.aspireminiloanservice.loan.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.UUID;
import lombok.*;

@Getter
@Builder(toBuilder = true)
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "installment")
public class Installment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "loan_id", nullable = false)
    private UUID loanId;

    @Column(name = "term_no", nullable = false)
    private int termNo;

    @Column(name = "scheduled_payment_date", nullable = false)
    private LocalDate scheduledPaymentDate;

    @Column(name = "actual_payment_date")
    private LocalDate actualPaymentDate;

    @Column(name = "pending_amount", nullable = false)
    private Double pendingAmount;

    @Column(name = "received_amount", nullable = false)
    private Double receivedAmount;

    @Column(name = "payment_status", nullable = false)
    private PaymentStatus status;
}
