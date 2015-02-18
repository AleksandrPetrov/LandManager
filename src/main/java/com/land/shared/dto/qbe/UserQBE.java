package com.land.shared.dto.qbe;

@SuppressWarnings("serial")
public class UserQBE extends AbstractQBE {

    private String filter = null;

    public UserQBE() {
        super();
    }

    public UserQBE(Long first, Long count) {
        super(first, count);
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

}
