package com.land.client.place;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class DocumentsPlace extends AbstractPlace {
	private static final String VIEW_HISTORY_TOKEN = "documents";

	private Long page = null;

	public DocumentsPlace(Long page) {
		this.page = page;
	}

	public Long getPage() {
		return page;
	}

	public void setPage(Long page) {
		this.page = page;
	}

	@Prefix(value = VIEW_HISTORY_TOKEN)
	public static class Tokenizer implements PlaceTokenizer<DocumentsPlace> {

		public DocumentsPlace getPlace(String token) {
			Long page = 1L;
			try {
				page = Long.valueOf(token);
			} catch (Exception e) {
			}
			return new DocumentsPlace(page);
		}

		public String getToken(DocumentsPlace place) {
			return place.getPage() == null ? "" : place.getPage() + "";
		}
	}

}
