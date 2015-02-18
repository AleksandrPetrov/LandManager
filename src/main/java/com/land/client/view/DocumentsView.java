package com.land.client.view;

import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;
import com.land.shared.dto.FileDto;
import com.land.shared.dto.ObjectDocumentsItemDto;

public interface DocumentsView extends IsWidget {

    void setPageCount(Long pageCount);

    void setCurrentPage(Long currentPage);

    void setObjectDocuments(List<ObjectDocumentsItemDto> listObjectDocuments);

    void removeFile(FileDto fileDto);

    void setPresenter(Presenter presenter);

    public interface Presenter {

        void onAddObjectClick();

        void onPagerChange(Long newPage);

        void onObjectClick(ObjectDocumentsItemDto objectDocumentsItemDto);

        void onFileDelete(FileDto fileDto);
    }

}
