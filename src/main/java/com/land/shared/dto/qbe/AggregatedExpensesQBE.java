package com.land.shared.dto.qbe;

@SuppressWarnings("serial")
public class AggregatedExpensesQBE extends AbstractQBE {

    private Long year = null;

    public AggregatedExpensesQBE() {}

    public AggregatedExpensesQBE(Long first, Long count) {
        super(first, count);
    }

    public Long getYear() {
        return year;
    }

    public void setYear(Long year) {
        this.year = year;
    }

}
