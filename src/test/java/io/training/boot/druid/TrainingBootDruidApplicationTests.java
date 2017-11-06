package io.training.boot.druid;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.util.JdbcConstants;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.DateUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.BatchUpdateUtils;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class TrainingBootDruidApplicationTests {

	@Autowired
	UserMapper userMapper;

	@Test
	public void contextLoads() {

	}

	@Test
	public void testGet() {
		int i = 0;
		while (i ++ < 20) {
			User user = userMapper.findUser(1);

			log.info("查询的用户：{}", user);
		}

	}

	@Test
	public void insert() {

		int i = 0;
		while (i++ < 10) {
			User u = new User();
			u.setName("AAA" + DateUtil.formatAsDatetime(new Date()));
			u.setAge(30);
			userMapper.create(u);
		}

	}

}

