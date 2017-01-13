package pack.loan.rest

import groovy.json.JsonSlurper
import groovy.util.logging.Slf4j
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import pack.loan.app.Application
import pack.loan.dao.BlacklistRepository
import pack.loan.dao.Loan
import pack.loan.dao.LoanApplicationRepository
import pack.loan.dao.LoanRepository
import spock.lang.Specification

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import static org.springframework.http.HttpStatus.OK
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup
import static pack.loan.rest.LoanController.ALL
import static pack.loan.rest.LoanController.LOAN_PATH

@Slf4j
@ContextConfiguration(classes = [Application.class])
@SpringBootTest(webEnvironment = RANDOM_PORT)
class LoanControllerTest extends Specification {

    def loanRepository = Mock(LoanRepository)
    def loanApplicationRepository = Mock(LoanApplicationRepository)
    def blacklistRepository = Mock(BlacklistRepository)
    def loanController = new LoanController(loanRepository, loanApplicationRepository, blacklistRepository)
    MockMvc mockMvc = standaloneSetup(loanController).build()


    void setup() {
        loanRepository.findAll() >> Collections.singletonList(new Loan("50.45", "13", "fn", "ln", "12345", "RUS"))
    }

    def "get all"() {
        when:
        def response = mockMvc.perform(get(LOAN_PATH + ALL)).andReturn().response
        def content = new JsonSlurper().parseText(response.contentAsString)

        then:
        response.status == OK.value()
        response.contentType.contains('application/json')
        response.contentType == 'application/json;charset=UTF-8'
        content.status == "OK"
        content.content == "[{\"amount\":50.45,\"term\":13,\"personalId\":\"12345\",\"country\":\"RUS\"}]"
    }
}
