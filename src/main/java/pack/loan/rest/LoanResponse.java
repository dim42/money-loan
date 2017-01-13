package pack.loan.rest;

public class LoanResponse {
    private final String status;
    private final String content;

    public LoanResponse(ResultCode resultCode, String content) {
        this.status = resultCode.toString();
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public String getContent() {
        return content;
    }
}
