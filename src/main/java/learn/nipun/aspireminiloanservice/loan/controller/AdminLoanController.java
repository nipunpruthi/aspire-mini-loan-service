package learn.nipun.aspireminiloanservice.loan.controller;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import learn.nipun.aspireminiloanservice.loan.LoanService;
import learn.nipun.aspireminiloanservice.loan.dto.LoanApprovalRequestDto;
import learn.nipun.aspireminiloanservice.loan.entity.Loan;
import learn.nipun.aspireminiloanservice.loan.entity.Installment;
import learn.nipun.aspireminiloanservice.loan.model.LoanApprovalRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static learn.nipun.aspireminiloanservice.loan.LoanMapper.toLoanApprovalRequest;

@RestController
@RequiredArgsConstructor
public class AdminLoanController {

    public static final String LOAN_PATH = "/api/v1/admin/loan";

    private final LoanService loanService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(path = LOAN_PATH)
    public ResponseEntity<List<Loan>> getLoans() {

        return new ResponseEntity<>(loanService.getAllLoans(), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping(path = LOAN_PATH)
    public ResponseEntity<Loan> approveLoan(@RequestHeader HttpHeaders headers,
            @RequestBody LoanApprovalRequestDto requestDto) {

        String adminName = getUserName(headers);
        LoanApprovalRequest request = toLoanApprovalRequest(requestDto, adminName);
        return new ResponseEntity<>(loanService.loanApproval(request), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping(path = LOAN_PATH + "/plans")
    public ResponseEntity<List<Installment>> getAllInstallments(@RequestHeader HttpHeaders headers,
            @RequestParam("loan_id") UUID loanId) {

        return new ResponseEntity<>(loanService.getAllInstallments(loanId), HttpStatus.OK);
    }

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