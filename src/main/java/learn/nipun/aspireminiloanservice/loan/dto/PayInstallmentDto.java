package learn.nipun.aspireminiloanservice.loan.dto;

import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PayInstallmentDto {
    private UUID loanId;
    private Double amount;
}
