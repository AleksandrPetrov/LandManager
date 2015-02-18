package com.land.client.place;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class TaxesPlace extends AbstractPlace {
	private static final String VIEW_HISTORY_TOKEN = "taxes";

	private Long page = null;

	public TaxesPlace(Long page) {
		this.page = page;
	}

	public Long getPage() {
		return page;
	}

	public void setPage(Long page) {
		this.page = page;
	}

	@Prefix(value = VIEW_HISTORY_TOKEN)
	public static class Tokenizer implements PlaceTokenizer<TaxesPlace> {

		public TaxesPlace getPlace(String token) {
			Long page = 1L;
			try {
				page = Long.valueOf(token);
			} catch (Exception e) {
			}
			return new TaxesPlace(page);
		}

		public String getToken(TaxesPlace place) {
			return place.getPage() == null ? "" : place.getPage() + "";
		}
	}

}
