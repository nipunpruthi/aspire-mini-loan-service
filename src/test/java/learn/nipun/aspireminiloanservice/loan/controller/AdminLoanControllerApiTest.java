package learn.nipun.aspireminiloanservice.loan.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import learn.nipun.aspireminiloanservice.loan.LoanService;
import learn.nipun.aspireminiloanservice.loan.dto.ApprovalDecisionDto;
import learn.nipun.aspireminiloanservice.loan.dto.LoanApprovalRequestDto;
import learn.nipun.aspireminiloanservice.loan.entity.Loan;
import learn.nipun.aspireminiloanservice.loan.entity.Installment;
import learn.nipun.aspireminiloanservice.loan.model.LoanStatus;
import learn.nipun.aspireminiloanservice.loan.repository.InstallmentRepository;
import learn.nipun.aspireminiloanservice.loan.repository.LoanRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static learn.nipun.aspireminiloanservice.loan.controller.AdminLoanController.LOAN_PATH;
import static learn.nipun.aspireminiloanservice.util.LoanTestData.aLoan;
import static learn.nipun.aspireminiloanservice.util.LoanTestData.anInstallment;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AdminLoanControllerApiTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    private LoanRepository loanRepository;

    @MockBean
    private InstallmentRepository installmentRepository;

    @Autowired
    private LoanService loanService;

    @Autowired
    protected MockMvc mockMvc;

    @Test
    void getLoans_returnUnauthorisedWhenCredentialsIsNotCorrect() throws Exception {

        //Given
        var request = get(LOAN_PATH)
                .header(AUTHORIZATION, getBasicAuthenticationHeader("dummyUser", "wrongPassword"))
                .contentType(MediaType.APPLICATION_JSON_VALUE);

        //When & Then
        mockMvc.perform(request)
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getLoans_returnForbiddenWhenCredentialsDoesnotBelongsToAdmin() throws Exception {

        //Given
        var request = get(LOAN_PATH)
                .header(AUTHORIZATION, getBasicAuthenticationHeader("user1", "userpass"))
                .contentType(MediaType.APPLICATION_JSON_VALUE);

        //When & Then
        mockMvc.perform(request)
                .andExpect(status().isForbidden());
    }

    @Test
    void getLoans_returnAllLoansWhenCredentialsAreCorrect() throws Exception {
        //Given
        Loan aloan = aLoan();
        when(loanService.getAllLoans()).thenReturn(List.of(aloan));
        var request = get(LOAN_PATH)
                .header(AUTHORIZATION, getBasicAuthenticationHeader("admin1", "adminpass"))
                .contentType(MediaType.APPLICATION_JSON_VALUE);

        //When & Then
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].customerId").value(aloan.getCustomerId()));
    }

    @Test
    void getLoans_return2xxWhenLoanIsProvidedForApproval() throws Exception {
        //Given
        Loan loan = aLoan();

        Loan approvedLoan = loan.toBuilder()
                .status(LoanStatus.APPROVED)
                .loanApprovedDate(LocalDate.now())
                .updatedBy("admin")
                .build();

        when(loanRepository.findById(loan.getId())).thenReturn(Optional.of(loan));
        when(loanRepository.save(any())).thenReturn(approvedLoan);

        LoanApprovalRequestDto loanApprovalRequestDto = LoanApprovalRequestDto.builder()
                .loanId(loan.getId())
                .decision(ApprovalDecisionDto.valueOf("APPROVED"))
                .build();

        String jsonRequestDto = objectMapper.writeValueAsString(loanApprovalRequestDto);
        when(loanService.getAllLoans()).thenReturn(List.of(loan));
        var request = patch(LOAN_PATH)
                .header(AUTHORIZATION, getBasicAuthenticationHeader("admin1", "adminpass"))
                .content(jsonRequestDto)
                .contentType(MediaType.APPLICATION_JSON_VALUE);

        //When & Then
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }

    @Test
    void getLoans_return2xxWhenLoanStatusIsNotPending() throws Exception {
        //Given
        Loan loan = aLoan().toBuilder().status(LoanStatus.REJECTED).build();

        when(loanRepository.findById(loan.getId())).thenReturn(Optional.of(loan));
//        when(loanRepository.save(any())).thenReturn(approvedLoan);

        LoanApprovalRequestDto loanApprovalRequestDto = LoanApprovalRequestDto.builder()
                .loanId(loan.getId())
                .decision(ApprovalDecisionDto.valueOf("APPROVED"))
                .build();

        String jsonRequestDto = objectMapper.writeValueAsString(loanApprovalRequestDto);
//        when(loanService.getAllLoans()).thenReturn(List.of(loan));
        var request = patch(LOAN_PATH)
                .header(AUTHORIZATION, getBasicAuthenticationHeader("admin1", "adminpass"))
                .content(jsonRequestDto)
                .contentType(MediaType.APPLICATION_JSON_VALUE);

        //When & Then
        mockMvc.perform(request)
                .andExpect(status().isNotAcceptable())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void getInstallments_return2xxWhenLoanIdDoesNotExist() throws Exception {

        UUID randomLoanId = UUID.randomUUID();

        when(loanRepository.findById(randomLoanId)).thenReturn(Optional.empty());

        LoanApprovalRequestDto loanApprovalRequestDto = LoanApprovalRequestDto.builder()
                .loanId(randomLoanId)
                .decision(ApprovalDecisionDto.valueOf("APPROVED"))
                .build();

        String jsonRequestDto = objectMapper.writeValueAsString(loanApprovalRequestDto);
        var request = patch(LOAN_PATH)
                .header(AUTHORIZATION, getBasicAuthenticationHeader("admin1", "adminpass"))
                .content(jsonRequestDto)
                .contentType(MediaType.APPLICATION_JSON_VALUE);

        //When & Then
        mockMvc.perform(request)
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void getLoans_returnAllInstallments() throws Exception {
        //Given
        Loan loan = aLoan();
        Installment installment = anInstallment().toBuilder().loanId(loan.getId()).build();
        when(loanRepository.findById(loan.getId())).thenReturn(Optional.of(loan));
        when(installmentRepository.findAllByLoanId(loan.getId())).thenReturn(List.of(installment));
        var request = get(LOAN_PATH + "/plans")
                .header(AUTHORIZATION, getBasicAuthenticationHeader("admin1", "adminpass"))
                .param("loan_id", loan.getId().toString())
                .contentType(MediaType.APPLICATION_JSON_VALUE);

        //When & Then
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(installment.getId().toString()));
    }


    private static String getBasicAuthenticationHeader(String username, String password) {

        String valueToEncode = username + ":" + password;
        return "Basic " + Base64.getEncoder().encodeToString(valueToEncode.getBytes());
    }
}
