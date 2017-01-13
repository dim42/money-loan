package pack.loan.rest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import pack.loan.app.Application;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pack.loan.rest.LoanController.*;
import static pack.loan.rest.LoanRequest.*;
import static pack.loan.rest.ResultCode.OK;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class ControllerTest {

    private static final String COLON_DELIMITER = "\":\"";
    private static final String COMMA_DELIMITER = "\",\"";
    @Autowired
    private MockMvc mockMvc;

    @After
    public void tearDown() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void noParamGreetingShouldReturnDefaultMessage() throws Exception {
        mockMvc.perform(get(LOAN_PATH + BY_USER)).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("name:v1"));
    }

    @Test
    public void all() throws Exception {
        mockMvc.perform(get(LOAN_PATH + ALL))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("name:Spring Community"));
    }

    @Test
    public void paramGreetingShouldReturnTailoredMessage() throws Exception {
        mockMvc.perform(get(LOAN_PATH + BY_USER).param("p1", "Spring Community"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("name:Spring Community"));
    }

    @Test
    public void testApplyForLoan() throws Exception {
        String json = "{\"" + LOAN_AMOUNT + COLON_DELIMITER + "100.35" + COMMA_DELIMITER + "term" + COLON_DELIMITER + 12 + COMMA_DELIMITER + NAME +
                COLON_DELIMITER + "Name1" + COMMA_DELIMITER + SURNAME + COLON_DELIMITER + "Surname1" + COMMA_DELIMITER + PERSONAL_ID + COLON_DELIMITER + 2222
                + "\"}";

        mockMvc.perform(post(LOAN_PATH + APPLY).contentType(APPLICATION_JSON).content(json))
                .andDo(print()).andDo(log()).andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(OK.toString()))
                .andExpect(jsonPath("$.content").value("2222"));

        mockMvc.perform(get(LOAN_PATH + ALL))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(OK.toString()))
                .andExpect(jsonPath("$.content").value("1"));
    }
}
