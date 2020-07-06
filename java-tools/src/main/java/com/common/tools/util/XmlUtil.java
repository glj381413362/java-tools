package com.common.tools.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.ElementHandler;
import org.dom4j.ElementPath;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

/** xml解析工具类 */
@Slf4j
public class XmlUtil {

  /**
   * dom解析xml文件
   *
   * @param filePath
   */
  public static void parseXml(String filePath) {
    // 创建SAXReader对象
    SAXReader reader = new SAXReader();
    try {
      // 读取文件 转换成Document
      Document document = reader.read(new File(filePath));
      // 获取根节点元素对象
      Element root = document.getRootElement();
      long s = System.currentTimeMillis();
      getChildNodes(root);
      long e = System.currentTimeMillis();
      System.out.println(root.getName() + "节点元素遍历结束,耗时共:" + (e - s) + " ms");
    } catch (DocumentException e) {
      e.printStackTrace();
    }
  }

  /**
   * 遍历节点下的所有子节点和属性
   *
   * @param ele 元素节点
   */
  private static void getChildNodes(Element ele) {

    String eleName = ele.getName();
    System.out.println("开始遍历当前：" + eleName + "节点元素");
    List<Attribute> attributes = ele.attributes();
    for (Attribute attr : attributes) {
      System.out.println("name=" + attr.getName() + ",value=" + attr.getValue());
    }

    // 同时迭代当前节点下面的所有子节点
    // 使用递归
    Iterator<Element> iterator = ele.elementIterator();

    if (!iterator.hasNext()) {
      String text = ele.getTextTrim();
      System.out.println("text=" + text);
    }

    while (iterator.hasNext()) {
      Element e = iterator.next();
      getChildNodes(e);
    }
  }

  /**
   * sax解析xml配置文件
   *
   * @param filePath
   */
  public static void parseHandler(String filePath) {
    // 创建SAXReader对象
    SAXReader reader = new SAXReader();
    reader.setDefaultHandler(
        new ElementHandler() {

          @Override
          public void onStart(ElementPath elementPath) {}

          @Override
          public void onEnd(ElementPath elementPath) {
            // 获得当前节点
            Element e = elementPath.getCurrent();
            System.out.println(e.getName());
            // 记得从内存中移去
            e.detach();
          }
        });
    try {
      reader.read(new BufferedInputStream(new FileInputStream(new File(filePath))));
    } catch (DocumentException e) {
      e.printStackTrace();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  public static void writeXml(String filePath, Document doc) {
    // 设置XML文档格式
    OutputFormat outputFormat = OutputFormat.createPrettyPrint();
    // 设置XML编码方式,即是用指定的编码方式保存XML文档到字符串(String),这里也可以指定为GBK或是ISO8859-1
    outputFormat.setEncoding("UTF-8");
    // 是否生产xml头
    outputFormat.setSuppressDeclaration(true);
    // 设置是否缩进
    outputFormat.setIndent(true);
    // 以四个空格方式实现缩进
    outputFormat.setIndent("    ");
    // 设置是否换行
    outputFormat.setNewlines(true);
    XMLWriter out = null;
    try {
      out = new XMLWriter(new FileWriter(new File(filePath)), outputFormat);
      out.write(doc);
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        out.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * bean 转xml
   *
   * @param bean
   * @param ignoreNull
   * @param propertyNamingStrategy
   * @author gongliangjun 2020-01-09 8:53 PM
   * @return java.lang.String
   */
  public static String beanToXml(
      Object bean, boolean ignoreNull, PropertyNamingStrategy propertyNamingStrategy) {
    XmlMapper xmlMapper = new XmlMapper();
    xmlMapper.setDefaultUseWrapper(false);
    // 字段为null，自动忽略，不再序列化
    if (ignoreNull) {
      xmlMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }
    // XML标签名:使用骆驼命名的属性名，
    if (null != propertyNamingStrategy) {
      xmlMapper.setPropertyNamingStrategy(propertyNamingStrategy);
    }
    // 设置转换模式
    xmlMapper.enable(MapperFeature.USE_STD_BEAN_NAMING);
    try {
      return xmlMapper.writeValueAsString(bean);
    } catch (JsonProcessingException e) {
      log.error("beanToXml error:{}", e);
    }
    return StringUtils.EMPTY;
  }

  public static void main(String[] args) {

  }
}
