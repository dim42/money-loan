package pack.loan.rest;

public class LoanResponse {
    private final String content;

    public LoanResponse(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}
