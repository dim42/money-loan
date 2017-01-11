package pack.loan.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoanRequest {
    private String amount;
    private String term;
    private String name;
    @JsonProperty("surname")
    private String surName;
    @JsonProperty("personal_id")
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public String getPersonalId() {
        return personalId;
    }

    public void setPersonalId(String personalId) {
        this.personalId = personalId;
    }
}
