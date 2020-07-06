package com.common.tools.util;

import com.common.tools.util.exception.BaseException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.StringTokenizer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

/**
 * 字符串工具类
 *
 * @author gongliangjun 2019/12/28 5:36 PM
 */
@Slf4j
public class StringUtil {

  /**
   * 判断字符串是否相等，并且打印日志
   *
   * @param one
   * @param two
   * @param msgT
   * @param params
   * @return boolean
   * @author gongliangjun 2019-12-25 5:50 PM
   */
  public static boolean strEquals(
      Logger logger, Object one, Object two, String msgT, Object... params) {
    boolean res = Objects.equals(one, two);
    if (null == logger) {
      log.info(strFormat(msgT, params) + " :[{}]", res);
    } else {
      logger.info(strFormat(msgT, params) + " :[{}]", res);
    }
    return res;
  }

  /**
   * 判断字符串是否相等，并且打印日志
   *
   * @param one
   * @param two
   * @param msgT
   * @param params
   * @return boolean
   * @author gongliangjun 2019-12-25 5:50 PM
   */
  public static boolean strEquals(Object one, Object two, String msgT, Object... params) {
    return strEquals(null, one, two, msgT, params);
  }

  /**
   * 判断字符串是否为空，并且打印日志
   *
   * @param logger
   * @param str
   * @param msgT
   * @return boolean
   * @author gongliangjun 2019-12-25 5:50 PM
   */
  public static boolean strIsBlank(Logger logger, String str, String msgT, Object... params) {
    boolean res = StringUtils.isBlank(str);
    if (null == logger) {
      log.info(strFormat(msgT, params) + " :[{}]", res);
    } else {
      logger.info(strFormat(msgT, params) + " :[{}]", res);
    }
    return res;
  }

  /**
   * 判断字符串是否为空，并且打印日志
   *
   * @param str
   * @param msgT
   * @param params
   * @return boolean
   * @author gongliangjun 2019-12-25 5:50 PM
   */
  public static boolean strIsBlank(String str, String msgT, Object... params) {
    boolean res = StringUtils.isBlank(str);
    log.info(strFormat(msgT, params) + " :[{}]", res);
    return strIsBlank(null, str, msgT, params);
  }

  /**
   * 判断字符串是否不为空，并且打印日志
   *
   * @param str
   * @param msgT
   * @param params
   * @return boolean
   * @author gongliangjun 2019-12-25 5:50 PM
   */
  public static boolean strNotBlank(Logger logger, String str, String msgT, Object... params) {
    boolean res = StringUtils.isNotBlank(str);
    if (null == logger) {
      log.info(strFormat(msgT, params) + " :[{}]", res);
    } else {
      logger.info(strFormat(msgT, params) + " :[{}]", res);
    }
    return res;
  }

  /**
   * 判断字符串是否不为空，并且打印日志
   *
   * @param str
   * @param msgT
   * @param params
   * @return boolean
   * @author gongliangjun 2019-12-25 5:50 PM
   */
  public static boolean strNotBlank(String str, String msgT, Object... params) {
    boolean res = StringUtils.isNotBlank(str);
    log.info(strFormat(msgT, params) + " :[{}]", res);
    return res;
  }

  /**
   * url编码
   *
   * @param str
   * @return
   */
  public static String encodeUrl(String str) {
    String s = null;
    try {
      s = URLEncoder.encode(str, PluginConstants.UTF8);
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }

    return s;
  }

  /**
   * url解码
   *
   * @param str
   * @return
   */
  public static String decodeUrl(String str) {
    String s = null;
    try {
      s = URLDecoder.decode(str, PluginConstants.UTF8);
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return s;
  }

  /**
   * 字符串分隔 StringTokenizer效率是三种分隔方法中最快的
   *
   * @param str
   * @param sign
   * @return
   */
  public static String[] split(String str, String sign) {
    if (str == null) {
      return new String[] {};
    }
    StringTokenizer token = new StringTokenizer(str, sign);
    String[] strArr = new String[token.countTokens()];
    int i = 0;
    while (token.hasMoreElements()) {
      strArr[i] = token.nextElement().toString();
      i++;
    }
    return strArr;
  }

  /**
   * 字符串拼接
   *
   * @param sign
   * @param strArr
   * @return
   */
  public static String joinStr(String sign, String... strArr) {
    Optional<String> optional =
        Arrays.stream(strArr).filter(Objects::nonNull).reduce((a, b) -> a + sign + b);
    return optional.orElse("");
  }

  /**
   * @param e
   * @return exception-string
   * @description 将异常栈中的内容转化为String
   */
  public static String exceptionString(final Exception e) {
    try {
      final StringWriter sw = new StringWriter();
      final PrintWriter pw = new PrintWriter(sw);
      e.printStackTrace(pw);
      return sw.toString() + "\r\n";
    } catch (final Exception e2) {
      final String errorMsg = "Common exception util error occured :" + e2.getMessage();
      log.error(errorMsg, e);
      return errorMsg;
    }
  }

  /**
   * 获取格式化后的消息,格式如:my name is ${}, my age is ${}
   *
   * @param message
   * @param objects
   * @return
   */
  public static String strFormat0(String message, Object... objects) {
    return parse("${", "}", message, objects);
  }

  /**
   * 获取格式化后的消息,格式如:my name is {}, my age is {}
   *
   * @param message
   * @param objects
   * @return
   */
  public static String strFormat(String message, Object... objects) {
    return parse("{", "}", message, objects);
  }

  /**
   * 将字符串text中由openToken和closeToken组成的占位符依次替换为args数组中的值
   *
   * <p>参考：主要通过简单的改写myatis框架中的GenericTokenParser类
   *
   * @param openToken
   * @param closeToken
   * @param text
   * @param args
   * @return
   */
  public static String parse(String openToken, String closeToken, String text, Object... args) {
    if (args == null || args.length <= 0) {
      return text;
    }
    int argsIndex = 0;

    if (text == null || text.isEmpty()) {
      return "";
    }
    char[] src = text.toCharArray();
    int offset = 0;
    // search open token
    int start = text.indexOf(openToken, offset);
    if (start == -1) {
      return text;
    }
    final StringBuilder builder = new StringBuilder();
    StringBuilder expression = null;
    while (start > -1) {
      if (start > 0 && src[start - 1] == '\\') {
        // this open token is escaped. remove the backslash and continue.
        builder.append(src, offset, start - offset - 1).append(openToken);
        offset = start + openToken.length();
      } else {
        // found open token. let's search close token.
        if (expression == null) {
          expression = new StringBuilder();
        } else {
          expression.setLength(0);
        }
        builder.append(src, offset, start - offset);
        offset = start + openToken.length();
        int end = text.indexOf(closeToken, offset);
        while (end > -1) {
          if (end > offset && src[end - 1] == '\\') {
            // this close token is escaped. remove the backslash and continue.
            expression.append(src, offset, end - offset - 1).append(closeToken);
            offset = end + closeToken.length();
            end = text.indexOf(closeToken, offset);
          } else {
            expression.append(src, offset, end - offset);
            offset = end + closeToken.length();
            break;
          }
        }
        if (end == -1) {
          // close token was not found.
          builder.append(src, start, src.length - start);
          offset = src.length;
        } else {
          /////////////////////////////////////// 仅仅修改了该else分支下的个别行代码////////////////////////

          String value =
              (argsIndex <= args.length - 1)
                  ? (args[argsIndex] == null ? "" : args[argsIndex].toString())
                  : expression.toString();
          builder.append(value);
          offset = end + closeToken.length();
          argsIndex++;
          ////////////////////////////////////////////////////////////////////////////////////////////////
        }
      }
      start = text.indexOf(openToken, offset);
    }
    if (offset < src.length) {
      builder.append(src, offset, src.length - offset);
    }
    return builder.toString();
  }

  public static void main(String[] args) {

    String s = strFormat("aaaa{}aaa", "222", 344);

    boolean b = strIsBlank(log, "aa", "是否为空{}", 1);
    strFormat("", "");
    NullPointerException aaaaaaaaa = new NullPointerException("test");

    String s1 = exceptionString(aaaaaaaaa);

    System.out.println(s1);
		BaseException commonException =
        new BaseException(aaaaaaaaa, "id:{},error:{}", 1234, "我就是错了");
    String message = commonException.getMessage();
    System.out.println(message);
  }
}
