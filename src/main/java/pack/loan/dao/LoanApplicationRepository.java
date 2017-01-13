package pack.loan.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;

public interface LoanApplicationRepository extends CrudRepository<LoanApplication, Long> {

    @Query("select count(la) from LoanApplication la where la.country = ?1 and la.date >= ?2")
    Long countFrom(String country, Date from);
}
