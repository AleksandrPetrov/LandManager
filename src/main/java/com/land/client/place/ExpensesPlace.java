package com.land.client.place;

import java.util.Date;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

@SuppressWarnings("deprecation")
public class ExpensesPlace extends AbstractPlace {
    private static final String VIEW_HISTORY_TOKEN = "expenses";

    private Long year = null;

    private Long page = null;

    public ExpensesPlace(Long year, Long page) {
        this.year = year;
        this.page = page;
    }

    public Long getYear() {
        return year;
    }

    public void setYear(Long year) {
        this.year = year;
    }

    public Long getPage() {
        return page;
    }

    public void setPage(Long page) {
        this.page = page;
    }

    @Prefix(value = VIEW_HISTORY_TOKEN)
    public static class Tokenizer implements PlaceTokenizer<ExpensesPlace> {

        public ExpensesPlace getPlace(String token) {
            String[] arr = token.split("&");
            Long year = new Date().getYear() + 1900L;
            Long page = 1L;

            if (arr.length > 0) {
                try {
                    year = Long.parseLong(arr[0]);
                }
                catch (Exception e) {}
            }

            if (arr.length > 1) {
                try {
                    page = Long.parseLong(arr[1]);
                }
                catch (Exception e) {}
            }

            return new ExpensesPlace(year, page);
        }

        public String getToken(ExpensesPlace place) {
            return place.getYear() + "&" + place.getPage();
        }
    }
}
