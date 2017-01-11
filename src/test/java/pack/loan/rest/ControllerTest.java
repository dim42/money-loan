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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pack.loan.rest.LoanController.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class ControllerTest {

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
        mockMvc.perform(get(BY_USER)).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("name:v1"));
    }

    @Test
    public void all() throws Exception {
        mockMvc.perform(get(ALL))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("name:Spring Community"));
    }

    @Test
    public void paramGreetingShouldReturnTailoredMessage() throws Exception {
        mockMvc.perform(get(BY_USER).param("p1", "Spring Community"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("name:Spring Community"));
    }

    @Test
    public void addLoan() throws Exception {
        mockMvc.perform(post(APPLY).param("amount", "5.2"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("name:Spring Community"));
    }
}
