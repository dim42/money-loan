package pack.loan.rest;

import net.webservicex.GeoIP;
import net.webservicex.GeoIPService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pack.loan.app.Application;
import pack.loan.dao.BlackPerson;
import pack.loan.dao.BlacklistRepository;
import pack.loan.dao.Loan;
import pack.loan.dao.LoanApplication;
import pack.loan.dao.LoanApplicationRepository;
import pack.loan.dao.LoanRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.String.format;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
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
    private static final String DEFAULT_COUNTRY_CODE = "lv";

    private final LoanRepository loanRepository;
    private final LoanApplicationRepository loanApplicationRepository;
    private final BlacklistRepository blacklistRepository;
    /*
    Country service classes have been generated with: wsimport http://www.webservicex.net/geoipservice.asmx?WSDL -keep
     */
    private final GeoIPService geoIPService = new GeoIPService();
    @Value("${country.count.limit}")
    private Long countLimit;
    @Value("${application.period}")
    private Long applicationPeriod;

    public LoanController(LoanRepository loanRepository, LoanApplicationRepository loanApplicationRepository, BlacklistRepository blacklistRepository) {
        this.loanRepository = loanRepository;
        this.loanApplicationRepository = loanApplicationRepository;
        this.blacklistRepository = blacklistRepository;
    }

    @RequestMapping(method = GET, path = ALL)
    public LoanResponse getAll() {
        AtomicInteger i = new AtomicInteger(0);
        loanRepository.findAll().forEach(loan -> i.incrementAndGet());
        return new LoanResponse(OK, Integer.toString(i.get()));
    }

    @RequestMapping(method = GET, path = BY_USER)
    public LoanResponse getByUser(@RequestParam(name = "name") String name) {
        AtomicInteger i = new AtomicInteger(0);
        loanRepository.findByLastName(name).forEach(loan -> i.incrementAndGet());
        return new LoanResponse(OK, Integer.toString(i.get()));
    }

    @RequestMapping(method = POST, path = APPLY)
    public LoanResponse applyForLoan(@RequestBody LoanRequest request, HttpServletRequest httpRequest) {
        try {
            String countryCode = getCountryCode(httpRequest);
            validateAgainstTime(countryCode);
            validateAgainstBlacklisted(request.getPersonalId());
            Loan loan = new Loan(request.getAmount(), request.getTerm(), request.getFirstName(), request.getLastName(), request.getPersonalId(), countryCode);
            loanRepository.save(loan);
            return new LoanResponse(OK, loan.getPersonalId());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new LoanResponse(FAIL, e.getMessage());
        }
    }

    private String getCountryCode(HttpServletRequest httpRequest) {
        String ipAddress = getIpAddress(httpRequest);
        try {
            GeoIP geoIP = geoIPService.getGeoIPServiceSoap().getGeoIP(ipAddress);
            String countryCode = geoIP.getCountryCode();
            if (countryCode == null || countryCode.trim().isEmpty()) {
                return DEFAULT_COUNTRY_CODE;
            }
            return countryCode;
        } catch (Exception e) {
            log.error(format("Getting country error for ip: %s", ipAddress), e);
            return DEFAULT_COUNTRY_CODE;
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

    private void validateAgainstTime(String countryCode) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime from = now.plusNanos(-applicationPeriod * 1000000);
        loanApplicationRepository.save(new LoanApplication(countryCode, now));
        Long count = loanApplicationRepository.countFrom(countryCode, from);
        if (count > countLimit) {
            throw new RuntimeException(format("Loan application limit for country (%s) is exceeded", countryCode));
        }
    }

    private void validateAgainstBlacklisted(String personalId) {
        BlackPerson person = blacklistRepository.findByPersonalId(personalId);
        if (person != null) {
            throw new RuntimeException(format("Person (%s) is in blacklist!", personalId));
        }
    }
}
