package learn.nipun.aspireminiloanservice.loan.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;
import learn.nipun.aspireminiloanservice.loan.LoanService;
import learn.nipun.aspireminiloanservice.loan.dto.LoanApplyDto;
import learn.nipun.aspireminiloanservice.loan.dto.PayInstallmentDto;
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

import static learn.nipun.aspireminiloanservice.loan.controller.CustomerLoanController.LOAN_PATH;
import static learn.nipun.aspireminiloanservice.util.LoanTestData.aLoan;
import static learn.nipun.aspireminiloanservice.util.LoanTestData.anInstallment;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CustomerLoanControllerApiTest {

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
    void getLoans_returnForbiddenWhenCredentialsDoesnotBelongsToAnyUser() throws Exception {

        //Given
        var request = get(LOAN_PATH)
                .header(AUTHORIZATION, getBasicAuthenticationHeader("dummyuser", "dummypassword"))
                .contentType(MediaType.APPLICATION_JSON_VALUE);

        //When & Then
        mockMvc.perform(request)
                .andExpect(status().isUnauthorized());
    }

    @Test
    void applyLoan_returnOk() throws Exception {
        //Given
        LoanApplyDto loanApplyDto = LoanApplyDto.builder()
                .loanTerm(5)
                .amount(100.0)
                .build();
        String jsonLoanApplyDto = objectMapper.writeValueAsString(loanApplyDto);

        var request = post(LOAN_PATH)
                .header(AUTHORIZATION, getBasicAuthenticationHeader("user1", "userpass"))
                .content(jsonLoanApplyDto)
                .contentType(MediaType.APPLICATION_JSON_VALUE);

        //When & Then
        Loan loan = aLoan();
        when(loanRepository.save(any(Loan.class))).thenReturn(loan);
        mockMvc.perform(request)
                .andExpect(status().isCreated());
    }

    @Test
    void getInstallment_returnOk() throws Exception {

        Loan loan = aLoan().toBuilder().customerId("user1").status(LoanStatus.APPROVED).build();
        Installment installment = anInstallment().toBuilder().loanId(loan.getId()).build();
        var request = get(LOAN_PATH + "/plan")
                .header(AUTHORIZATION, getBasicAuthenticationHeader("user1", "userpass"))
                .param("loanId", String.valueOf(loan.getId()))
                .contentType(MediaType.APPLICATION_JSON_VALUE);

        //When & Then
        when(loanRepository.findById(loan.getId())).thenReturn(Optional.of(loan));
        when(installmentRepository.findById(installment.getId())).thenReturn(Optional.of(anInstallment()));
        mockMvc.perform(request)
                .andExpect(status().isOk());
    }

    @Test
    void getInstallment_returnAccessDeniedWhenLoanIdDoesnotBelongToUser() throws Exception {

        Loan loan = aLoan().toBuilder().customerId("user2").status(LoanStatus.APPROVED).build();
        Installment installment = anInstallment().toBuilder().loanId(loan.getId()).build();
        var request = get(LOAN_PATH + "/plan")
                .header(AUTHORIZATION, getBasicAuthenticationHeader("user1", "userpass"))
                .param("loanId", String.valueOf(loan.getId()))
                .contentType(MediaType.APPLICATION_JSON_VALUE);

        //When & Then
        when(loanRepository.findById(loan.getId())).thenReturn(Optional.of(loan));
        when(installmentRepository.findById(installment.getId())).thenReturn(Optional.of(anInstallment()));
        mockMvc.perform(request)
                .andExpect(status().isForbidden());
    }

    @Test
    void getInstallment_returnWrongRequestWhenLoanIsRejected() throws Exception {

        Loan loan = aLoan().toBuilder().customerId("user1").status(LoanStatus.REJECTED).build();
        Installment installment = anInstallment().toBuilder().loanId(loan.getId()).build();
        var request = get(LOAN_PATH + "/plan")
                .header(AUTHORIZATION, getBasicAuthenticationHeader("user1", "userpass"))
                .param("loanId", String.valueOf(loan.getId()))
                .contentType(MediaType.APPLICATION_JSON_VALUE);

        //When & Then
        when(loanRepository.findById(loan.getId())).thenReturn(Optional.of(loan));
        when(installmentRepository.findById(installment.getId())).thenReturn(Optional.of(anInstallment()));
        mockMvc.perform(request)
                .andExpect(status().isNotAcceptable());
    }

    @Test
    void payInstallment_returnOk() throws Exception {

        Loan loan = aLoan().toBuilder().customerId("user1").status(LoanStatus.APPROVED).build();
        Installment installment = anInstallment().toBuilder().loanId(loan.getId())
                .termNo(1)
                .receivedAmount(null)
                .pendingAmount(loan.getInstallmentAmount())
                .scheduledPaymentDate(LocalDate.now().plusDays(7))
                .build();
        PayInstallmentDto payInstallmentDto = PayInstallmentDto.builder().loanId(loan.getId())
                .amount(loan.getInstallmentAmount()).build();
        String jsonDto = objectMapper.writeValueAsString(payInstallmentDto);
        var request = patch(LOAN_PATH + "/plan")
                .header(AUTHORIZATION, getBasicAuthenticationHeader("user1", "userpass"))
                .content(jsonDto)
                .contentType(MediaType.APPLICATION_JSON_VALUE);

        when(loanRepository.findById(loan.getId())).thenReturn(Optional.of(loan));
        when(installmentRepository.findById(installment.getId())).thenReturn(Optional.of(anInstallment()));
        when(installmentRepository.findTop1ByLoanIdOrderByScheduledPaymentDateDesc(any(UUID.class))).thenReturn(
                installment);
        mockMvc.perform(request)
                .andExpect(status().isOk());
    }

    private static String getBasicAuthenticationHeader(String username, String password) {

        String valueToEncode = username + ":" + password;
        return "Basic " + Base64.getEncoder().encodeToString(valueToEncode.getBytes());
    }
}
