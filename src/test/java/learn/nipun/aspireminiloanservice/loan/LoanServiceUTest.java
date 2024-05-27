package learn.nipun.aspireminiloanservice.loan;


import jakarta.validation.ValidationException;
import java.util.Optional;
import learn.nipun.aspireminiloanservice.exception.InvalidStatusException;
import learn.nipun.aspireminiloanservice.exception.ResourceAccessForbidden;
import learn.nipun.aspireminiloanservice.exception.ResourceNotFoundException;
import learn.nipun.aspireminiloanservice.loan.dto.PayInstallmentDto;
import learn.nipun.aspireminiloanservice.loan.entity.Loan;
import learn.nipun.aspireminiloanservice.loan.entity.Installment;
import learn.nipun.aspireminiloanservice.loan.model.LoanApprovalRequest;
import learn.nipun.aspireminiloanservice.loan.model.LoanStatus;
import learn.nipun.aspireminiloanservice.loan.repository.InstallmentRepository;
import learn.nipun.aspireminiloanservice.loan.repository.LoanRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static learn.nipun.aspireminiloanservice.util.LoanTestData.aLoan;
import static learn.nipun.aspireminiloanservice.util.LoanTestData.anInstallment;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@SpringBootTest
class LoanServiceUTest {

    @MockBean
    private LoanRepository loanRepository;

    @MockBean
    private InstallmentRepository installmentRepository;

    @Autowired
    private LoanValidator loanValidator;

    @Autowired
    private LoanService loanService;

//    @BeforeEach
//    public void setup() {
//        MockitoAnnotations.initMocks(this);
//    }

    @Test
    void getInstallment_throwsException_whenGetNextInstallmentCustomerIdAndLoanIdAreNotRelated() {
        //Give
        Loan loan = aLoan().toBuilder().customerId("user1").build();
        String userName = "user2";
        when(loanRepository.findById(loan.getId())).thenReturn(Optional.of(loan));

        //WhenThen
        assertThatThrownBy(() -> loanService.getNextInstallment(userName, loan.getId()))
                .isExactlyInstanceOf(ResourceAccessForbidden.class);
    }

    @Test
    void getInstallment_throwsException_whenLoanDoesnotExist() {
        //Give
        Loan loan = aLoan().toBuilder().customerId("user1").build();
        String userName = "user1";
        when(loanRepository.findById(loan.getId())).thenReturn(Optional.empty());

        //WhenThen
        assertThatThrownBy(() -> loanService.getNextInstallment(userName, loan.getId()))
                .isExactlyInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getInstallment_throwsException_whenLoanStatusIsNotApproved() {

        String userName = "user1";
        Loan loan = aLoan().toBuilder().customerId(userName).status(LoanStatus.PENDING).build();
        when(loanRepository.findById(loan.getId())).thenReturn(Optional.of(loan));

        //WhenThen
        assertThatThrownBy(() -> loanService.getNextInstallment(userName, loan.getId()))
                .isExactlyInstanceOf(InvalidStatusException.class);

    }

    @Test
    void loanApproval_throwsException_whenLoanStatusIsNotPending() {

        Loan loan = aLoan().toBuilder().customerId("user1").status(LoanStatus.REJECTED).build();
        String userName = "user1";
        when(loanRepository.findById(loan.getId())).thenReturn(Optional.of(loan));

        LoanApprovalRequest loanApprovalRequest = LoanApprovalRequest.builder()
                .adminId("admin")
                .status(LoanStatus.APPROVED)
                .loanId(loan.getId())
                .build();

        assertThatThrownBy(() -> loanService.loanApproval(loanApprovalRequest))
                .isExactlyInstanceOf(InvalidStatusException.class);
    }

    @Test
    void payInstallment_throwsException_whenAmountLessThenInstallmentAmount() {

        Loan loan = aLoan().toBuilder().customerId("user1").status(LoanStatus.APPROVED).build();
        String userName = "user1";
        Installment installment = anInstallment().toBuilder().pendingAmount(loan.getInstallmentAmount()).build();
        PayInstallmentDto payInstallmentDto = PayInstallmentDto.builder().loanId(loan.getId())
                .amount(loan.getInstallmentAmount() - 1).build();
        when(loanRepository.findById(loan.getId())).thenReturn(Optional.of(loan));
        when(installmentRepository.findById(installment.getId())).thenReturn(Optional.of(installment));
        when(installmentRepository.findTop1ByLoanIdOrderByScheduledPaymentDateDesc(loan.getId())).thenReturn(installment);
        assertThatThrownBy(() -> loanService.payInstallment(userName, payInstallmentDto))
                .isExactlyInstanceOf(ValidationException.class);
    }
}
