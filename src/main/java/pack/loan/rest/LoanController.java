package pack.loan.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class LoanController {

    @RequestMapping(method = GET, path = "/ml")
    public LoanResponse get(@RequestParam(name = "p1", defaultValue = "v1") String name) {
        return new LoanResponse("name:" + name);
    }
}
