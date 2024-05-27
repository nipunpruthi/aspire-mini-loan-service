package learn.nipun.aspireminiloanservice.loan.repository;

import learn.nipun.aspireminiloanservice.Application;
import learn.nipun.aspireminiloanservice.loan.entity.Installment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static learn.nipun.aspireminiloanservice.util.LoanTestData.anInstallment;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = Application.class)
@TestPropertySource(locations = "classpath:application.properties")
class InstallmentRepositoryITest {

    @Autowired
    private InstallmentRepository installmentRepository;

    @BeforeEach
    void setUp() {

        installmentRepository.deleteAll();
    }

    @Test
    void insertNewRecordSuccessfully() {
        //Given
        Installment installment = anInstallment();

        //When
        Installment saved = installmentRepository.save(installment);

        //Then
        var actual = installmentRepository.findById(installment.getId());
        assertThat(actual).isPresent();
        assertThat(actual.get())
                .usingRecursiveComparison()
                .isEqualTo(installment);
    }

}
