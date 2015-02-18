package com.land.shared.dto;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class ObjectDocumentsItemDto extends ObjectDto {

    private List<FileDto> documents = new ArrayList<FileDto>();

    public ObjectDocumentsItemDto() {
        super();
    }

    public List<FileDto> getDocuments() {
        return documents;
    }

    public void setDocuments(List<FileDto> documents) {
        this.documents = documents;
    }

}
