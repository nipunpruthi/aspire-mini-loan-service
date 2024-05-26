package learn.nipun.aspireminiloanservice.loan.model;

import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class LoanApprovalRequest {

    private UUID loanId;
    private LoanStatus status;
    private String adminId;
}
