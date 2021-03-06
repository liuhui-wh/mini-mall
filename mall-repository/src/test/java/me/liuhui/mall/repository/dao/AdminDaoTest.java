package me.liuhui.mall.repository.dao;

import me.liuhui.mall.repository.model.Admin;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;

@TestPropertySource("classpath:application.properties")
@RunWith(SpringRunner.class)
@ComponentScan(basePackages = "me.liuhui.mall")
@SpringBootTest(classes = AdminDaoTest.class)
@EnableAutoConfiguration
class AdminDaoTest {

    @Resource
    private AdminDao adminDao;

    @Test
    void selectByUsername() {
        System.out.println(adminDao.selectByUsername("admin"));
    }

    @Test
    void insert() {
        Admin admin = new Admin();
        admin.setCreateAdmin("admin");
        admin.setCreateTime(new Date());
        admin.setLastLoginTime(new Date());
        admin.setPassword("123456");
        admin.setRealName("zhang3");
        admin.setStatus(1);
        admin.setUsername("ddfffsss");
        adminDao.insert(admin);
    }
}