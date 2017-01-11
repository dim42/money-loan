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
    public CommandLineRunner demo(LoanRepository repository) {
        return (args) -> {
            repository.save(new Loan("Jack", "Bauer"));
            repository.save(new Loan("Chloe", "O'Brian"));
            repository.save(new Loan("Kim", "Bauer"));
            repository.save(new Loan("David", "Palmer"));
            repository.save(new Loan("Michelle", "Dessler"));

            log.info("Loans found with findAll():");
            log.info("-------------------------------");
            for (Loan loan : repository.findAll()) {
                log.info(loan.toString());
            }
            log.info("");

            // fetch an individual loan by ID
            Loan loan = repository.findOne(1L);
            log.info("Loan found with findOne(1L):");
            log.info("--------------------------------");
            log.info(loan.toString());
            log.info("");

            log.info("Loan found with findByLastName('Bauer'):");
            log.info("--------------------------------------------");
            for (Loan bauer : repository.findByLastName("Bauer")) {
                log.info(bauer.toString());
            }
            log.info("");
        };
    }
}
