package pack.loan.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pack.loan.dao.LoanRepository;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class LoanController {

    static final String APPLY = "/apply-for-loan";
    static final String ALL = "/loans";
    static final String BY_USER = "/loans-by-user";

    private final LoanRepository repository;

    public LoanController(LoanRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(method = GET, path = ALL)
    public LoanResponse all() {
        return new LoanResponse("all");
    }

    @RequestMapping(method = GET, path = BY_USER)
    public LoanResponse get(@RequestParam(name = "p1", defaultValue = "v1") String name) {
        return new LoanResponse("name:" + name);
    }

    @RequestMapping(method = POST, path = APPLY)
    public LoanResponse add(LoanRequest request) {

        return new LoanResponse("add:");
    }
}
