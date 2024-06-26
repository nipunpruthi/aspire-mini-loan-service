package learn.nipun.aspireminiloanservice.loan.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class LoanApply {

    private Double amount;
    private Integer loanTerm;
    private String userName;
}