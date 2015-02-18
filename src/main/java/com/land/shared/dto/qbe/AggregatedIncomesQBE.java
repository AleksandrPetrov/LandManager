package com.land.shared.dto.qbe;

@SuppressWarnings("serial")
public class AggregatedIncomesQBE extends AbstractQBE {

    private Long year = null;

    public AggregatedIncomesQBE() {}

    public AggregatedIncomesQBE(Long first, Long count) {
        super(first, count);
    }

    public Long getYear() {
        return year;
    }

    public void setYear(Long year) {
        this.year = year;
    }

}
