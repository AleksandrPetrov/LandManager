package com.land.client.ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.land.client.utils.JSUtils;
import com.land.shared.dto.FileDto;

public abstract class FileUploadButton extends AbstractFileUploadButton {

	public FileUploadButton(Long objectId) {
		super(GWT.getHostPageBaseURL() + "service/addfile" + (objectId == null ? "" : ("?objectId=" + objectId)));
	}

	@Override
	protected void onCompleteSubmit(int submitId, List<SubmitFile> files, String result) {
		// System.out.println("PhotoEditWidget.onSubmitComplete " + submitId +
		// "   " + files + "   " + result);
		ArrayList<FileDto> out = new ArrayList<FileDto>();
		if (result.contains("<h2>HTTP ERROR: 500</h2>") || result.contains("Не удалось сохранить файл")) {
			onSubmitError(result);
		} else {
			for (String fileParamsStr : result.split(":fsep;")) {
				String[] fileParamsArr = fileParamsStr.split(":psep;");
				Long id = Long.parseLong(fileParamsArr[0]);
				String name = fileParamsArr[1];
				Long size = Long.parseLong(fileParamsArr[2]);
				if (id != null) {
					FileDto file = new FileDto();
					file.setId(id);
					file.setSize(size);
					file.setName(name);
					file.setCreateDate(new Date());
					file.setOwner(JSUtils.getString("userName"));
					out.add(file);
				}
			}

			onSubmitComplete(submitId, out);
		}
	}

	@Override
	protected boolean onBeforeSubmit(int submitId, List<SubmitFile> files) {
		// System.out.println("PhotoEditWidget.onBeforeSubmit " + submitId +
		// "   " + files);
		return true;
	}

	abstract protected void onSubmitComplete(int submitId, List<FileDto> files);

	abstract protected void onSubmitError(String error);

}
