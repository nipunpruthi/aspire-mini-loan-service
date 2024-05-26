package learn.nipun.aspireminiloanservice.loan.model;

import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoanFilter {

    private UUID loanId;
    private String userName;
    private LoanStatus status;
}