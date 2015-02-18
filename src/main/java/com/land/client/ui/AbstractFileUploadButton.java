package com.land.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;

/**
 * Чтобы заработало нужно задать размер для .uploadButton и .uploadButton BUTTON
 */
public abstract class AbstractFileUploadButton extends Composite {

    static int i = 0;

    public class SubmitFile {
        String name;
        long size; // в байах

        public SubmitFile(String name, long size) {
            super();
            this.name = name;
            this.size = size;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getSize() {
            return size;
        }

        public void setSize(long size) {
            this.size = size;
        }

        @Override
        public String toString() {
            return name + ":" + size;
        }

    }

    private class UploadForm extends FormPanel {
        private final FileUpload file = new FileUpload();
        final int id = i++;

        public UploadForm() {
            getElement().getStyle().setPosition(Position.ABSOLUTE);
            getElement().getStyle().setLeft(0, Unit.PX);
            getElement().getStyle().setTop(0, Unit.PX);
            getElement().getStyle().setRight(0, Unit.PX);
            getElement().getStyle().setBottom(0, Unit.PX);

            setAction(submitUrl);
            setEncoding(FormPanel.ENCODING_MULTIPART);
            setMethod(FormPanel.METHOD_POST);
            add(file);

            file.addChangeHandler(new ChangeHandler() {
                public void onChange(ChangeEvent event) {
                    if (file.getFilename() != null && !file.getFilename().isEmpty())
                        AbstractFileUploadButton.this.submit(id, getFileInfo());
                    else
                        Window.alert("Файл не выбран");
                }
            });

            addSubmitCompleteHandler(new SubmitCompleteHandler() {
                public void onSubmitComplete(SubmitCompleteEvent event) {
                    AbstractFileUploadButton.this.onCompleteSubmit(id, getFileInfo(), event.getResults());
                    UploadForm.this.setStyleName("used");
                    UploadForm.this.removeFromParent();
                }
            });

            file.setName("file");
            file.setStyleName("");
            file.getElement().getStyle().setPosition(Position.ABSOLUTE);
            file.getElement().setTabIndex(-1);
            file.getElement().getStyle().setProperty("filter", "alpha(opacity: 0)");
            file.getElement().getStyle().setOpacity(0);
            file.getElement().getStyle().setRight(0, Unit.PX);
            file.getElement().getStyle().setTop(-200, Unit.PX);
            file.getElement().getStyle().setFontSize(500, Unit.PX);
            file.getElement().getStyle().setBorderWidth(0, Unit.PX);
            file.getElement().getStyle().setProperty("height", "auto");
            file.getElement().getStyle().setProperty("lineHeight", "normal");

            file.getElement().getStyle().setCursor(Cursor.POINTER);
            setMultiple(multiple);
        }

        public void setMultiple(boolean multiple) {
            if (multiple)
                file.getElement().setPropertyString("multiple", "multiple");
            else
                file.getElement().removeAttribute("multiple");
        }

        public List<SubmitFile> getFileInfo() {
            List<SubmitFile> res = new ArrayList<SubmitFile>();
            String s = getFilesInfo(file.getElement());
            String[] fileAndSizeList = s.split("\\|");
            for (String string : fileAndSizeList) {
                if (string.length() > 0) {
                    res.add(new SubmitFile(string.split(":")[0], Long.parseLong(string.split(":")[1])));
                }
            }
            return res;
        }
    }

    private final String submitUrl;
    // private Button button = new Button();
    private FlowPanel panel = new FlowPanel();
    private UploadForm form;

    private boolean multiple = true;

    private boolean enabled = true;

    /**
     * Не прятать эту кнопку до тех пор пока загрузка не завершится (а то не
     * отработает перехватчик завершения)
     */
    public AbstractFileUploadButton(String submitUrl) {
        this.submitUrl = submitUrl;
        // panel.add(button);
        // button.getElement().getStyle().setPosition(Position.ABSOLUTE);
        // button.getElement().getStyle().setLeft(0, Unit.PX);
        // button.getElement().getStyle().setTop(0, Unit.PX);

        form = new UploadForm();
        form.file.setEnabled(enabled);
        panel.getElement().setInnerHTML("<i class='icon-plus'></i>Загрузить файл..");
        panel.add(form);

        panel.getElement().getStyle().setOverflow(Overflow.HIDDEN);
        // panel.getElement().getStyle().setFloat(Float.LEFT);
        initWidget(panel);

        // button.setText("");
        setStyleName("uploadButton");
    }

    public void setHtml(String html) {
        form.removeFromParent();
        panel.getElement().setInnerHTML(html);
        panel.add(form);
    }

    public void setMultiple(boolean multiple) {
        this.multiple = multiple;
        form.setMultiple(multiple);
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        form.file.setEnabled(enabled);
    }

    private void submit(int submitId, List<SubmitFile> files) {
        if (onBeforeSubmit(submitId, files)) {
            UploadForm oldForm = form;
            form.setVisible(false);
            form = new UploadForm();
            form.file.setEnabled(enabled);
            panel.add(form);

            onStartSubmit(submitId, files);

            oldForm.submit();
        }
    }

    protected abstract void onStartSubmit(int submitId, List<SubmitFile> files);

    /**
     * Данные поехали на сервер
     * 
     * @param submitId - идентефикатор отправления
     * @param files - файлы которые отправили на сервер
     * @return true - если начать отправку файлов, false - если отменить
     */
    protected abstract boolean onBeforeSubmit(int submitId, List<SubmitFile> files);

    /**
     * Завершилась обработка данных на сервере
     * 
     * @param submitId - идентефикатор отправления
     * @param files - файлы которые отправили на сервер
     * @param result - ответ сервера
     */
    protected abstract void onCompleteSubmit(int submitId, List<SubmitFile> files, String result);

    private native String getFilesInfo(Element input) /*-{
		var s = "";
		if (input.files != null)
			for ( var i = 0; i < input.files.length; i++) {
				s = s + input.files[i].name + ":" + input.files[i].size + "|";
			}
		return s;
    }-*/;

    // private static native void clickOnInputFile(Element elem) /*-{
    // elem.click();
    // }-*/;
}
