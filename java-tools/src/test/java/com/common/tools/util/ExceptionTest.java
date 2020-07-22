package com.common.tools.util;


import static com.common.tools.util.exception.BusinessExceptionAssert.USER_NOT_FOUND;

import com.common.tools.util.exception.BaseException;
import com.common.tools.util.exception.BusinessExceptionAssert;
import com.common.tools.util.exception.Msg;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Builder Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>Mar 19, 2020</pre>
 */
public class ExceptionTest {

	@Before
	public void before() throws Exception {
	}

	@After
	public void after() throws Exception {
	}

	/**
	 * Method: accept(T t, P1 p1, P2 p2, P3 p3)
	 */
	@Ignore
	@Test
	public void testAcceptForTP1P2P3() throws Exception {
		Object user = null;
		try {
//			BusinessExceptionAssert.USER_NOT_FOUND.assertNotNull(user,"根据code {}未查询到用户",user.getUserCode());
		} catch (BaseException e) {
			String trace = e.getTrace();
      System.out.println(trace);
		}
		if (1==1){
			throw new BaseException(Msg.of("测试{}"),"抛异常");
//			throw  new BaseException("aa","av");
		}

	}


} 
