package pack.loan.rest;

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
import static pack.loan.rest.ResultCode.FAIL;
import static pack.loan.rest.ResultCode.OK;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class ControllerTest {

    private static final String COLON = "\":\"";
    private static final String COMMA = "\",\"";
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testApplyForLoan() throws Exception {
        String surname1 = "Surname1";
        String json = "{\"" + LOAN_AMOUNT + COLON + "100.35" + COMMA + "term" + COLON + 12 + COMMA + NAME + COLON + "Name1" + COMMA + SURNAME + COLON +
                surname1 + COMMA + PERSONAL_ID + COLON + 2222 + "\"}";
        mockMvc.perform(post(LOAN_PATH + APPLY).contentType(APPLICATION_JSON).content(json))
                .andDo(print()).andDo(log()).andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(OK.toString()))
                .andExpect(jsonPath("$.content").value("2222"));

        json = "{\"" + LOAN_AMOUNT + COLON + "140.35" + COMMA + "term" + COLON + 7 + COMMA + NAME + COLON + "Name1" + COMMA + SURNAME + COLON +
                surname1 + COMMA + PERSONAL_ID + COLON + 2222 + "\"}";
        mockMvc.perform(post(LOAN_PATH + APPLY).contentType(APPLICATION_JSON).content(json))
                .andDo(print()).andDo(log()).andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(OK.toString()))
                .andExpect(jsonPath("$.content").value("2222"));

        json = "{\"" + LOAN_AMOUNT + COLON + "65.8" + COMMA + "term" + COLON + 15 + COMMA + NAME + COLON + "Name2" + COMMA + SURNAME + COLON +
                "Surname2" + COMMA + PERSONAL_ID + COLON + 3333 + "\"}";
        mockMvc.perform(post(LOAN_PATH + APPLY).contentType(APPLICATION_JSON).content(json))
                .andDo(print()).andDo(log()).andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(OK.toString()))
                .andExpect(jsonPath("$.content").value("3333"));

        mockMvc.perform(get(LOAN_PATH + BY_USER).param("name", surname1))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(OK.toString()))
                .andExpect(jsonPath("$.content").value("[{\"amount\":100.35,\"term\":12,\"personalId\":\"2222\",\"country\":\"ZZZ\"},{\"amount\":140.35," +
                        "\"term\":7,\"personalId\":\"2222\",\"country\":\"ZZZ\"}]"));

        mockMvc.perform(get(LOAN_PATH + ALL))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(OK.toString()))
                .andExpect(jsonPath("$.content").value("[{\"amount\":100.35,\"term\":12,\"personalId\":\"2222\",\"country\":\"ZZZ\"},{\"amount\":140.35," +
                        "\"term\":7,\"personalId\":\"2222\",\"country\":\"ZZZ\"},{\"amount\":65.80,\"term\":15,\"personalId\":\"3333\",\"country\":\"ZZZ\"}]"));

        json = "{\"" + LOAN_AMOUNT + COLON + "170.35" + COMMA + "term" + COLON + 12 + COMMA + NAME + COLON + "Name1" + COMMA + SURNAME + COLON +
                surname1 + COMMA + PERSONAL_ID + COLON + 1235 + "\"}";
        mockMvc.perform(post(LOAN_PATH + APPLY).contentType(APPLICATION_JSON).content(json))
                .andDo(print()).andDo(log()).andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(FAIL.toString()))
                .andExpect(jsonPath("$.content").value("Person (1235) is in blacklist!"));

        json = "{\"" + LOAN_AMOUNT + COLON + "75.8" + COMMA + "term" + COLON + 17 + COMMA + NAME + COLON + "Name2" + COMMA + SURNAME + COLON +
                "Surname2" + COMMA + PERSONAL_ID + COLON + 3333 + "\"}";
        mockMvc.perform(post(LOAN_PATH + APPLY).contentType(APPLICATION_JSON).content(json))
                .andDo(print()).andDo(log()).andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(FAIL.toString()))
                .andExpect(jsonPath("$.content").value("Loan application limit is exceeded for the country"));
    }
}
