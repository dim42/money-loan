package pack.loan.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import pack.loan.dao.BlackPerson;
import pack.loan.dao.BlacklistRepository;
import pack.loan.dao.Loan;
import pack.loan.dao.LoanRepository;
import pack.loan.rest.MarkerController;

@ComponentScan(basePackageClasses = MarkerController.class)
@EnableJpaRepositories(basePackageClasses = LoanRepository.class)
@EntityScan(basePackageClasses = Loan.class)
@SpringBootApplication
public class Application {
    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner fillBlacklist(BlacklistRepository repository) {
        return (args) -> {
            repository.save(new BlackPerson("1234"));
            repository.save(new BlackPerson("1235"));
            repository.save(new BlackPerson("1236"));
            long count = repository.count();
            log.info("Blacklist number:" + count);
        };
    }
}
