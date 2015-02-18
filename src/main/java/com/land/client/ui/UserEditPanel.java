package com.land.client.ui;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.land.client.ui.ModalPanel.EModalResult;
import com.land.client.ui.form.CheckBoxWidget;
import com.land.client.ui.form.LabelWidget;
import com.land.client.ui.form.StyledFlowPanel;
import com.land.shared.dto.UserDto;

public class UserEditPanel extends Composite {

    private static final int MIN_PASSWORD_LENGTH = 3;
    //@formatter:off

            private LabelWidget loginHelp = new LabelWidget("help-inline", null, "");
            private TextBox loginInput = new TextBox();
        private FlowPanel loginControl = new StyledFlowPanel("controls", loginInput, loginHelp);
        private LabelWidget loginLabel = new LabelWidget("control-label", "loginInput", "Логин");
    private FlowPanel loginPanel = new StyledFlowPanel("control-group", loginLabel, loginControl);

            private LabelWidget nameHelp = new LabelWidget("help-inline", null, "");
            private TextBox nameInput = new TextBox();
        private FlowPanel nameControl = new StyledFlowPanel("controls", nameInput, nameHelp);
        private LabelWidget nameLabel = new LabelWidget("control-label", "nameInput", "Имя");
    private FlowPanel namePanel = new StyledFlowPanel("control-group", nameLabel, nameControl);
    
            private LabelWidget emailHelp = new LabelWidget("help-inline", null, "");
            private TextBox emailInput = new TextBox();
        private FlowPanel emailControl = new StyledFlowPanel("controls", emailInput, emailHelp);
        private LabelWidget emailLabel = new LabelWidget("control-label", "emailInput", "Email");
    @SuppressWarnings("unused")
    private FlowPanel emailPanel = new StyledFlowPanel("control-group", emailLabel, emailControl);

            private CheckBoxWidget passwordChangeInput = new CheckBoxWidget("checkbox", "Поменять пароль");
        private FlowPanel passwordChangeControl = new StyledFlowPanel("controls", passwordChangeInput);
    private FlowPanel passwordChangePanel = new StyledFlowPanel("control-group", passwordChangeControl);

            private LabelWidget passwordOldHelp = new LabelWidget("help-inline", null, "");
            private PasswordTextBox passwordOldInput = new PasswordTextBox();
        private FlowPanel passwordOldControl = new StyledFlowPanel("controls", passwordOldInput, passwordOldHelp);
        private LabelWidget passwordOldLabel = new LabelWidget("control-label", "passwordOld", "Старый пароль");
    private FlowPanel passwordOldPanel = new StyledFlowPanel("control-group", passwordOldLabel, passwordOldControl);

            private LabelWidget passwordNewHelp = new LabelWidget("help-inline", null, "");
            private PasswordTextBox passwordNewInput = new PasswordTextBox();
        private FlowPanel passwordNewControl = new StyledFlowPanel("controls", passwordNewInput, passwordNewHelp);
        private LabelWidget passwordNewLabel = new LabelWidget("control-label", "passwordNew", "Новый пароль");
    private FlowPanel passwordNewPanel = new StyledFlowPanel("control-group", passwordNewLabel, passwordNewControl);
    //@formatter:on

    private StyledFlowPanel panel = new StyledFlowPanel("ГыукEditPanel form-horizontal", loginPanel, namePanel, passwordChangePanel, passwordOldPanel, passwordNewPanel);

    private UserDto object = null;

    public UserEditPanel() {
        super();
        initWidget(panel);
        ChangeHandler validateHandler = new ChangeHandler() {
            public void onChange(ChangeEvent event) {
                validate();
            }
        };

        loginInput.getElement().setId("loginInput");
        loginInput.setEnabled(false);
        loginInput.getElement().setAttribute("placeholder", "Логин");
        loginInput.addChangeHandler(validateHandler);

        nameInput.getElement().setId("nameInput");
        nameInput.getElement().setAttribute("placeholder", "Имя");
        nameInput.addChangeHandler(validateHandler);

        emailInput.getElement().setId("emailInput");
        emailInput.getElement().setAttribute("placeholder", "Email");
        emailInput.addChangeHandler(validateHandler);

        emailInput.getElement().setId("emailInput");
        emailInput.getElement().setAttribute("placeholder", "Email");
        emailInput.addChangeHandler(validateHandler);

        passwordChangeInput.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                passwordOldPanel.setVisible(event.getValue());
                passwordNewPanel.setVisible(event.getValue());

            }
        });
        passwordChangeInput.setValue(false);

        passwordOldPanel.setVisible(false);
        passwordOldInput.getElement().setId("passwordOldInput");
        passwordOldInput.getElement().setAttribute("placeholder", "Старый пароль");
        passwordOldInput.addChangeHandler(validateHandler);

        passwordNewPanel.setVisible(false);
        passwordNewInput.getElement().setId("passwordNewInput");
        passwordNewInput.getElement().setAttribute("placeholder", "Новый пароль");
        passwordNewInput.addChangeHandler(validateHandler);
    }

    private void setObject(UserDto user) {
        if (user == null) user = new UserDto();
        user = user.clone();
        this.object = user;

        loginInput.setText(user.getLogin());
        nameInput.setText(user.getName());
        emailInput.setText(user.getEmail());
    }

    public void setPasswordOldHelpText(String text) {
        if (!text.isEmpty()) {
            passwordOldPanel.addStyleName("error");
            passwordOldHelp.setText(text);
        }
        else {
            passwordOldPanel.removeStyleName("error");
            passwordOldHelp.setText("");
        }
    }

    public boolean validate() {
        boolean valid = true;

        String login = loginInput.getValue();
        if (login.isEmpty()) {
            loginPanel.addStyleName("error");
            loginHelp.setText("Login не может быть пустым");
            valid = false;
        }
        else {
            loginPanel.removeStyleName("error");
            loginHelp.setText("");
        }

        String name = nameInput.getValue();
        if (name.isEmpty()) {
            namePanel.addStyleName("error");
            nameHelp.setText("Имя не может быть пустым");
            valid = false;
        }
        else {
            namePanel.removeStyleName("error");
            nameHelp.setText("");
        }

        if (passwordChangeInput.getValue()) {
            String passwordNew = passwordNewInput.getText();
            if (passwordNew.isEmpty()) {
                passwordNewPanel.addStyleName("error");
                passwordNewHelp.setText("Пароль не может быть пустым");
                valid = false;
            }
            else {
                if (passwordNew.length() > 255) {
                    passwordNewPanel.addStyleName("error");
                    passwordNewHelp.setText("Пароль не может быть длиннее 255 символов");
                    valid = false;
                }
                else {
                    if (passwordNew.length() < MIN_PASSWORD_LENGTH) {
                        passwordNewPanel.addStyleName("error");
                        passwordNewHelp.setText("Пароль должен содержать как минимум 3 символа");
                        valid = false;
                    }
                    else {
                        passwordNewPanel.removeStyleName("error");
                        passwordNewHelp.setText("");
                    }
                }
            }

        }
        return valid;
    }

    public UserDto getObject() {
        object.setLogin(loginInput.getValue());
        object.setName(nameInput.getValue());
        object.setEmail(emailInput.getValue());
        if (passwordChangeInput.getValue()) {
            object.setPasswordOld(passwordOldInput.getText());
            object.setPasswordNew(passwordNewInput.getText());
        }
        else {
            object.setPasswordOld(null);
            object.setPasswordNew(null);
        }
        return object;
    }

    public static void showInPopup(UserDto user, final IPopupHandler<UserDto> callback) {
        final UserEditPanel editPanel = new UserEditPanel();
        editPanel.setObject(user);

        final ModalPanel mp = new ModalPanel(false);
        mp.setButtonVisible(EModalResult.DELETE, false);
        if (user == null || user.getId() == null) {
            mp.setHeader("Добавить учётную запись");
            mp.setButtonText(EModalResult.SAVE, "Добавить");
        }
        else {
            mp.setHeader("Изменить учётную запись");
            mp.setButtonText(EModalResult.SAVE, "Сохранить");
        }
        mp.setContent(editPanel);
        mp.setModalListener(new ModalPanel.IModalListener() {
            public boolean onClose(ModalPanel.EModalResult result) {
                if (result == EModalResult.SAVE) {
                    if (editPanel.validate()) {
                        callback.onSave(mp, editPanel.getObject());
                        return true;
                    }
                    else
                        return false;
                }
                else {
                    callback.onCancel(mp);
                    return true;
                }
            }
        });
        mp.show();
    }

}
