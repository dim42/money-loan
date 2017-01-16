package pack.loan.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pack.loan.api.CountryService;
import pack.loan.app.Application;
import pack.loan.dao.BlackPerson;
import pack.loan.dao.BlacklistRepository;
import pack.loan.dao.Loan;
import pack.loan.dao.LoanApplication;
import pack.loan.dao.LoanApplicationRepository;
import pack.loan.dao.LoanRepository;

import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.StreamSupport;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static pack.loan.api.CountryService.GEO_IP_SERVICE;
import static pack.loan.rest.LoanController.LOAN_PATH;
import static pack.loan.rest.ResultCode.FAIL;
import static pack.loan.rest.ResultCode.OK;

@RestController
@RequestMapping(path = LOAN_PATH)
public class LoanController {
    private static final Logger log = LoggerFactory.getLogger(Application.class);

    static final String LOAN_PATH = "/loan";
    static final String APPLY = "/apply";
    static final String ALL = "/all";
    static final String BY_USER = "/by-user";

    private final LoanRepository loanRepository;
    private final LoanApplicationRepository loanApplicationRepository;
    private final BlacklistRepository blacklistRepository;
    private final CountryService countryService;

    @Value("${country.count.limit}")
    private Long countLimit;
    @Value("${application.period}")
    private Long applicationPeriod;

    public LoanController(LoanRepository loanRepository, LoanApplicationRepository loanApplicationRepository, BlacklistRepository blacklistRepository,
                          @Named(GEO_IP_SERVICE) CountryService countryService) {
        this.loanRepository = loanRepository;
        this.loanApplicationRepository = loanApplicationRepository;
        this.blacklistRepository = blacklistRepository;
        this.countryService = countryService;
    }

    @RequestMapping(method = GET, path = ALL)
    public LoanResponse getAll() {
        try {
            Iterable<Loan> loans = loanRepository.findAll();
            return new LoanResponse(OK, toJsonString(loans));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new LoanResponse(FAIL, e.getMessage());
        }
    }

    private String toJsonString(Iterable<Loan> loans) throws JsonProcessingException {
        List<LoadDto> dtos = getLoadDtos(loans);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(dtos);
    }

    private List<LoadDto> getLoadDtos(Iterable<Loan> loans) {
        return StreamSupport.stream(loans.spliterator(), false)
                .map(loan -> new LoadDto(loan.getAmount(), loan.getTerm(), loan.getPersonalId(), loan.getCountry())).collect(toList());
    }

    @RequestMapping(method = GET, path = BY_USER)
    public LoanResponse getByUser(@RequestParam(name = "name") String name) {
        try {
            List<Loan> loans = loanRepository.findByLastName(name);
            return new LoanResponse(OK, toJsonString(loans));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new LoanResponse(FAIL, e.getMessage());
        }
    }

    @RequestMapping(method = POST, path = APPLY)
    public LoanResponse applyForLoan(@RequestBody LoanRequest request, HttpServletRequest httpRequest) {
        try {
            String countryCode = countryService.getCountryCode(getIpAddress(httpRequest));
            validateAgainstTime(countryCode);
            String personalId = request.getPersonalId();
            validateAgainstBlacklisted(personalId);
            Loan loan = new Loan(request.getAmount(), request.getTerm(), request.getFirstName(), request.getLastName(), personalId, countryCode);
            loanRepository.save(loan);
            return new LoanResponse(OK, loan.getPersonalId());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new LoanResponse(FAIL, e.getMessage());
        }
    }

    private String getIpAddress(HttpServletRequest httpRequest) {
        String ipAddress = httpRequest.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = httpRequest.getRemoteAddr();
        }
        if (ipAddress == null || ipAddress.trim().isEmpty()) {
            throw new RuntimeException("Unknown IP address");
        }
        return ipAddress;
    }

    private synchronized void validateAgainstTime(String countryCode) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime from = now.plusNanos(-applicationPeriod * 1000_000);
        loanApplicationRepository.save(new LoanApplication(countryCode, now));
        Long count = loanApplicationRepository.countFrom(countryCode, from);
        if (count > countLimit) {
            throw new RuntimeException("Loan application limit is exceeded for the country");
        }
    }

    private void validateAgainstBlacklisted(String personalId) {
        BlackPerson person = blacklistRepository.findByPersonalId(personalId);
        if (person != null) {
            throw new RuntimeException(format("Person (%s) is in blacklist!", personalId));
        }
    }
}
