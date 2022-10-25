package dao;

import likelion01.dao.UserDao;
import likelion01.dao.UserDaoFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(SpringExtension.class) //스프링 테스트
@ContextConfiguration(classes = UserDaoFactory.class)

class UserDaoTest {

    @Autowired
    ApplicationContext context; //ApplicationContext 인터페이스 선언

    @Test
    void addAndSelect() {
        UserDao userDao = context.getBean("awsUserDao", UserDao.class);
        String id = "12";
        User user = new User(id, "DH", "1122333");
        userDao.add(user);
        assertEquals("DH", user.getName());
        assertEquals("DH", user.getName());
    }
}