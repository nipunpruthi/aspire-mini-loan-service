package learn.nipun.aspireminiloanservice.loan.model;

import java.time.LocalDate;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ScheduledPayment {
    private UUID id;
    private UUID loanId;
    private int termNo;
    private LocalDate scheduledPaymentDate;
    private LocalDate actualPaymentDate;
    private Double dueAmount;
    private Double receivedAmount;
    private PaymentStatus status;
}
