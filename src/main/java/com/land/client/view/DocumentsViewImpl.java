package com.land.client.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.land.client.ui.FileUploadButton;
import com.land.client.ui.FileWDelAnchor;
import com.land.client.ui.Pager;
import com.land.client.ui.WaitPanel;
import com.land.shared.dto.FileDto;
import com.land.shared.dto.ObjectDocumentsItemDto;

public class DocumentsViewImpl extends Composite implements DocumentsView {

	private DocumentsView.Presenter presenter = null;

	private FlowPanel viewPanel = new FlowPanel();
	private FlexTable table = new FlexTable();
	private Pager pager = new Pager(Long.valueOf(10));
	private Button addObjectButton = new Button("<i class='icon-plus'></i> Добавить объект");

	private Map<Long, FileWDelAnchor> mapFiles = new HashMap<Long, FileWDelAnchor>();

	public DocumentsViewImpl() {
		super();
		viewPanel.setStyleName("DocumentsView");
		initWidget(viewPanel);
		table.setStyleName("table table-striped table-bordered");
		viewPanel.add(table);
		pager.addStyleName("pull-right");
		viewPanel.add(pager);

		addObjectButton.addStyleName("btn btn-primary");
		viewPanel.add(addObjectButton);

		Element thead = DOM.createTHead();
		table.getElement().insertAfter(thead, table.getElement().getFirstChild());

		Element tr = DOM.createTR();
		thead.appendChild(tr);

		Element thObject = DOM.createTH();
		thObject.setInnerText("Объект");
		tr.appendChild(thObject);

		Element thDocuments = DOM.createTH();
		thDocuments.setInnerText("Документы");
		tr.appendChild(thDocuments);
		tr.getFirstChildElement().setClassName("col1");

		addObjectButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				presenter.onAddObjectClick();
				// presenter.goTo(new ObjectEditPlace());
			}
		});

		pager.setChangeHandler(new Pager.IChangeHandler() {
			public void onChange(Long newPage) {
				presenter.onPagerChange(newPage);
				// presenter.goTo(new DocumentsPlace(newPage));
			}
		});

	}

	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	public void setPageCount(Long pageCount) {
		pager.setPageCount(pageCount);
	}

	public void setCurrentPage(Long currentPage) {
		pager.setCurrentPage(currentPage);
	}

	public void setObjectDocuments(List<ObjectDocumentsItemDto> listObjectDocuments) {
		table.clear();
		mapFiles.clear();
		int i = 0;
		for (final ObjectDocumentsItemDto objectDocumentsItemDto : listObjectDocuments) {
			Anchor objectAnchor = new Anchor(objectDocumentsItemDto.getAddress());
			objectAnchor.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					presenter.onObjectClick(objectDocumentsItemDto);
				}
			});
			table.setWidget(i, 0, objectAnchor);
			table.getCellFormatter().setStyleName(i, 0, "col1");
			table.getRowFormatter().setStyleName(i, "row-begin");

			final FlowPanel filePanel = new FlowPanel();
			filePanel.setStyleName("documents");
			table.setWidget(i, 1, filePanel);

			FileUploadButton upload = new FileUploadButton(objectDocumentsItemDto.getId()) {
				protected void onStartSubmit(int submitId, List<SubmitFile> files) {
					this.setEnabled(false);
					WaitPanel.show();
				}

				protected void onSubmitError(String error) {
					this.setEnabled(true);
					WaitPanel.hide();
					Window.alert(error);
				}

				protected void onSubmitComplete(int submitId, List<FileDto> files) {
					this.setEnabled(true);
					WaitPanel.hide();
					for (FileDto fileDto : files)
						addFileAnchorToPanel(fileDto, filePanel);
				}

			};
			filePanel.add(upload);

			for (FileDto fileDto : objectDocumentsItemDto.getDocuments()) {
				addFileAnchorToPanel(fileDto, filePanel);
			}

			i++;
		}
	}

	private void addFileAnchorToPanel(final FileDto fileDto, final FlowPanel panel) {
		final FileWDelAnchor fa = new FileWDelAnchor(fileDto);
		fa.addDeleteClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				presenter.onFileDelete(fileDto);
			}
		});
		mapFiles.put(fileDto.getId(), fa);
		panel.insert(fa, panel.getWidgetCount() - 1);
	}

	public void removeFile(FileDto fileDto) {
		mapFiles.get(fileDto.getId()).removeFromParent();
		mapFiles.remove(fileDto.getId());
	}
}
