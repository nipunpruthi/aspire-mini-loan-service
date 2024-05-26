package learn.nipun.aspireminiloanservice.loan.dto;

import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class LoanApprovalRequestDto {

    private UUID loanId;
    private ApprovalDecisionDto decision;
}
