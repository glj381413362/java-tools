package com.common.tools.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 测试
 * </p>
 *
 * @author gongliangjun 2020/03/18 11:41 AM
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration // 由于是Web项目，Junit需要模拟ServletContext，因此我们需要给我们的测试类加上@WebAppConfiguration。
public abstract class BaseJUnit4Test {
/*

	@Before
	public void before() {

	}

	@After
	public void after() {
		context.destroy();
	}

	@Test
	public abstract void test();

	protected <T extends Object> T getBean(String beanId) {
		return (T) context.getBean(beanId);
	}

	protected <T extends Object> T getbean(Class<T> clazz) {
		return context.getBean(clazz);
	}*/

}
