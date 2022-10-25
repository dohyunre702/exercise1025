package dao;

import likelion01.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(SpringExtension.class) //스프링 테스트
@ContextConfiguration(classes = UserDaoFactory.class)

class UserDaoTest {

    @Autowired
    ApplicationContext context; //ApplicationContext 인터페이스 선언

    @Test
    void addAndSelect() {
        UserDao3 userDao = context.getBean("awsUserDao", UserDao3.class);
        String id = "12";
        User user = new User(id, "DH", "1122333");
        userDao.add(user);
        assertEquals("DH", user.getName());
        assertEquals("DH", user.getName());
    }
}