package pack.loan.rest

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.web.context.WebApplicationContext
import spock.lang.Specification
//@RunWith(SpringRunner.class)
//@SpringBootTest
//@AutoConfigureMockMvc
@ContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LoanControllerTest extends Specification {
//    @Autowired
//    private MockMvc mockMvc
    @Autowired
    WebApplicationContext context

    void setup() {
    }

    void cleanup() {
    }

    def "Get"() {
        expect:
//        1 == 1
//        mockMvc != null
        context != null
    }
}
