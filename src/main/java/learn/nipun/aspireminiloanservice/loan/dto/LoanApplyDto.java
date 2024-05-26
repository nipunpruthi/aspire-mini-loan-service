package learn.nipun.aspireminiloanservice.loan.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class LoanApplyDto {

    private Double amount;

    @JsonProperty("loan_term")
    private Integer loanTerm;
}
