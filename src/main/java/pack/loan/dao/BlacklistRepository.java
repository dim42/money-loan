package pack.loan.dao;

import org.springframework.data.repository.CrudRepository;

public interface BlacklistRepository extends CrudRepository<BlackPerson, Long> {
    BlackPerson findByPersonalId(String personalId);
}
