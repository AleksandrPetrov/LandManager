package com.land.server.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.land.shared.dto.UserDto;

@Entity
@Table(name = "T_USER")
public class User extends AbstractEntity {

    @Column(nullable = false, unique = true, length = 255)
    private String login = "";

    @Column(nullable = false, length = 255)
    private String password = "";

    /** ФИО */
    @Column(nullable = false, length = 1024)
    private String name;

    /** Почта */
    @Column(nullable = false, length = 1024)
    private String email;

    @Column(nullable = false)
    private Boolean admin = false;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    public UserDto toDto() {
        return new UserDto(id, login, name, email, admin, null, null);
    }

}
