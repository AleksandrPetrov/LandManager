package com.land.shared.dto;

@SuppressWarnings("serial")
public class UserDto extends EntityDto {

    private String login;

    private String email;

    private String name;

    private String passwordOld;

    private String passwordNew;

    private Boolean admin;

    public UserDto() {
        this(null, null, null, null, null, null, null);
    }

    public UserDto(Long id, String login, String name, String email, Boolean admin, String passwordOld,
            String passwordNew) {
        super(id);
        this.login = login;
        this.name = name;
        this.email = email;
        this.admin = admin;
        this.passwordOld = passwordOld;
        this.passwordNew = passwordNew;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordOld() {
        return passwordOld;
    }

    public void setPasswordOld(String passwordOld) {
        this.passwordOld = passwordOld;
    }

    public String getPasswordNew() {
        return passwordNew;
    }

    public void setPasswordNew(String passwordNew) {
        this.passwordNew = passwordNew;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    @Override
    public String toString() {
        return getId() + " " + getName();
    }

    public UserDto clone() {
        return new UserDto(id, login, name, email, admin, passwordOld, passwordNew);
    }
}
