package learn.nipun.aspireminiloanservice.loan;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import learn.nipun.aspireminiloanservice.loan.dto.LoanApplyDto;
import learn.nipun.aspireminiloanservice.loan.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static learn.nipun.aspireminiloanservice.loan.LoanMapper.toLoanApply;

@RestController
@RequiredArgsConstructor
public class CustomerLoanController {

    public static final String LOAN_PATH = "/api/v1/loan";

    private final LoanService loanService;

    @PreAuthorize("hasAnyAuthority('CUSTOMER')")
    @GetMapping(path = LOAN_PATH)
    public ResponseEntity<List<Loan>> getLoans(@RequestHeader HttpHeaders headers,
            @RequestParam(required = false) LoanStatus status) {

        String userName = getUserName(headers);

        LoanFilter loanFilter = LoanFilter.builder()
                .status(status)
                .userName(userName)
                .loanId(null)
                .build();

        return new ResponseEntity<>(loanService.getAllLoans(loanFilter), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('CUSTOMER')")
    @PostMapping(path = LOAN_PATH)
    public ResponseEntity<Loan> applyLoan(@RequestHeader HttpHeaders headers, @RequestBody LoanApplyDto loanApplyDto) {

        String userName = getUserName(headers);

        LoanApply loanApply = toLoanApply(loanApplyDto, userName);
        return new ResponseEntity<>(loanService.submitNewLoanRequest(loanApply), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyAuthority('CUSTOMER')")
    @GetMapping(path = LOAN_PATH + "/plan")
    public ResponseEntity<List<ScheduledPayment>> getScheduledPayment(@RequestHeader HttpHeaders headers,
            @RequestParam UUID loanId) {

        String userName = getUserName(headers);
        LoanFilter loanFilter = LoanFilter.builder()
                .status(null)
                .userName(userName)
                .loanId(loanId)
                .build();

        return new ResponseEntity<>(loanService.getScheduledPayments(loanFilter), HttpStatus.OK);
    }

//    @PatchMapping(path = LOAN_PATH + "/plan")
//    public ResponseEntity<ScheduledPayment> payScheduledPayment(UUID loanId,)

    private static String getUserName(HttpHeaders headers) {

        String userName = headers.getFirst(HttpHeaders.AUTHORIZATION);
        final String authorization = headers.getFirst(HttpHeaders.AUTHORIZATION);
        if (authorization != null && authorization.toLowerCase().startsWith("basic")) {
            String base64Credentials = authorization.substring("Basic".length()).trim();
            byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
            String credentials = new String(credDecoded, StandardCharsets.UTF_8);
            final String[] values = credentials.split(":", 2);
            userName = values[0];
        }
        return userName;
    }
}
