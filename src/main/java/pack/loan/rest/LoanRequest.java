package pack.loan.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoanRequest {
    static final String LOAN_AMOUNT = "loan amount";
    static final String NAME = "name";
    static final String SURNAME = "surname";
    static final String PERSONAL_ID = "personal_id";
    @JsonProperty(LOAN_AMOUNT)
    private String amount;
    private String term;
    @JsonProperty(NAME)
    private String firstName;
    @JsonProperty(SURNAME)
    private String lastName;
    @JsonProperty(PERSONAL_ID)
    private String personalId;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPersonalId() {
        return personalId;
    }

    public void setPersonalId(String personalId) {
        this.personalId = personalId;
    }
}
