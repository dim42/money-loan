package pack.loan.rest;

import java.math.BigDecimal;

public class LoadDto {
    private final BigDecimal amount;
    private final Integer term;
    private final String personalId;
    private final String country;

    public LoadDto(BigDecimal amount, Integer term, String personalId, String country) {

        this.amount = amount;
        this.term = term;
        this.personalId = personalId;
        this.country = country;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Integer getTerm() {
        return term;
    }

    public String getPersonalId() {
        return personalId;
    }

    public String getCountry() {
        return country;
    }
}
