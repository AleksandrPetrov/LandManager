package com.land.server;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.land.server.dao.UserDao;
import com.land.server.domain.User;
import com.land.shared.dto.UserDto;
import com.land.shared.dto.qbe.UserQBE;

public class DatabaseInitializer {
    public static final Logger log = Logger.getLogger(DatabaseInitializer.class);
    @Autowired
    UserDao userDao;

    @Transactional
    public void init() {
        try {
            List<User> list = userDao.getListUser(new UserQBE(0L, 1L));
            if (list.isEmpty()) {
                User u = userDao.save(new UserDto(null, "admin", "admin", "admin@admin.admin", true, "admin", "admin"), false);
                u.setAdmin(true);
            }
        }
        catch (Exception e) {
            log.error(e.getMessage());
        }

    }
}
