package learn.nipun.aspireminiloanservice.loan.repository;

import learn.nipun.aspireminiloanservice.Application;
import learn.nipun.aspireminiloanservice.loan.entity.Loan;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static learn.nipun.aspireminiloanservice.util.LoanTestData.aLoan;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = Application.class)
@TestPropertySource(
        locations = "classpath:application.properties")
class LoanRepositoryITest {

    @Autowired
    private LoanRepository loanRepository;

    @BeforeEach
    void setUp() {

        loanRepository.deleteAll();
    }

    @Test
    void insertNewRecordSuccessfully() {
        //Given
        Loan loan = aLoan();

        //When
        Loan saved = loanRepository.save(loan);

        //Then
        var actual = loanRepository.findById(loan.getId());
        assertThat(actual).isPresent();
        assertThat(actual.get())
                .usingRecursiveComparison()
                .isEqualTo(loan);
    }

}
