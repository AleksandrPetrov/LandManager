package com.land.client.ui;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.land.client.ui.form.StyledFlowPanel;

public class Pager extends Composite {

    private int MAX_PAGE_BUTTONS = 5;

    public interface IChangeHandler {
        public void onChange(Long newPage);
    }

    private FlowPanel panel = new StyledFlowPanel("pagination");
    private Element[] liList = null;
    private Element[] aList = null;

    private Long firstPage = 0L;
    private Long lastPage = 0L;

    private Long currentPage = 0L;
    private IChangeHandler changeHandler = null;

    private int pageButtonsCount = MAX_PAGE_BUTTONS;

    private int getNextButtonIndex() {
        return pageButtonsCount + 1;
    }

    private int getDefaultOffset() {
        return (pageButtonsCount + 1) / 2;
    }

    private int getOffsetAdd() {
        return (pageButtonsCount + 1) % 2;
    }

    public void setChangeHandler(IChangeHandler changeHandler) {
        this.changeHandler = changeHandler;
    }

    private EventListener listener = new EventListener() {
        public void onBrowserEvent(Event event) {
            event.preventDefault();
            event.stopPropagation();

            Element target = ((NativeEvent) event).getEventTarget().cast();
            String page = target.getAttribute("page");

            Long newPage = null;
            if (page.equals("-"))
                newPage = currentPage - 1;
            else if (page.equals("+"))
                newPage = currentPage + 1;
            else
                newPage = Long.valueOf(page);

            if (newPage.compareTo(firstPage) >= 0 && newPage.compareTo(lastPage) <= 0) {
                setCurrentPage(newPage);
                if (changeHandler != null) changeHandler.onChange(newPage);
            }

        }
    };

    public Pager(Long firstPage, Long lastPage) {
        super();
        initWidget(panel);
        setFirstAndLastPage(firstPage, lastPage);
    }

    public Pager(Long pageCount) {
        this(1L, pageCount);
    }

    private void renderButtons() {
        panel.clear();

        Element ul = createUL();
        panel.getElement().appendChild(ul);

        liList = new Element[pageButtonsCount + 2];
        aList = new Element[pageButtonsCount + 2];
        for (int i = 0; i <= getNextButtonIndex(); i++) {
            liList[i] = createLI();
            aList[i] = DOM.createAnchor();
            aList[i].setAttribute("href", "#");
            if (i == 0) {
                aList[i].setInnerText("«");
                aList[i].setAttribute("page", "-");
            }
            else if (i == getNextButtonIndex()) {
                aList[i].setInnerText("»");
                aList[i].setAttribute("page", "+");
            }
            else
                aList[i].setInnerText(i + "");

            DOM.sinkEvents(aList[i], Event.ONCLICK);
            DOM.setEventListener(aList[i], listener);

            ul.appendChild(liList[i]);
            liList[i].appendChild(aList[i]);
        }
    }

    public static Element createUL() {
        return Document.get().createULElement().cast();
    }

    public static Element createLI() {
        return Document.get().createLIElement().cast();
    }

    private void updateNumbers(Long currentPage) {
        Long offset = currentPage - getDefaultOffset();
        if (offset < 0) offset = 0L;
        if (currentPage + getDefaultOffset() + getOffsetAdd() > lastPage) offset = lastPage - pageButtonsCount;
        if (offset < firstPage) offset = firstPage - 1;

        for (int i = 1; i < getNextButtonIndex(); i++) {
            Long page = offset + i;
            aList[i].setInnerText(page + "");
            aList[i].setAttribute("page", page + "");
        }

        if (currentPage.compareTo(firstPage) <= 0)
            liList[0].addClassName("disabled");
        else
            liList[0].removeClassName("disabled");
        if (currentPage.compareTo(lastPage) >= 0)
            liList[getNextButtonIndex()].addClassName("disabled");
        else
            liList[getNextButtonIndex()].removeClassName("disabled");
    }

    public Long getPageCount() {
        return Math.max(0, lastPage - firstPage + 1);
    }

    public void setFirstAndLastPage(Long firstPage, Long lastPage) {
        if (firstPage == null) firstPage = 1L;
        if (lastPage == null) lastPage = 0L;
        this.firstPage = firstPage;
        this.lastPage = lastPage;

        pageButtonsCount = Math.min(getPageCount().intValue(), MAX_PAGE_BUTTONS);
        renderButtons();

        setCurrentPage(currentPage);
    }

    public void setPageCount(Long pageCount) {
        setFirstAndLastPage(1L, pageCount);
    }

    public void setCurrentPage(Long page) {
        if (page == null || page.compareTo(firstPage) < 0) page = firstPage;
        if (page.compareTo(lastPage) > 0) page = lastPage;

        updateNumbers(page);

        for (int i = 1; i < getNextButtonIndex(); i++) {
            liList[i].removeClassName("active");
            if (aList[i].getAttribute("page").equals(page + "")) liList[i].addClassName("active");
        }

        currentPage = page;
    }

}
