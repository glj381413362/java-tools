package com.common.tools.util.excel;

import com.common.tools.util.exception.BaseException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

/** @author gongliangjun 2020/01/12 3:42 PM */
public class ExcelReadHelper {

  private ExcelReadHelper() {}

  /**
   * @param file excel文件
   * @param clazz excel数据要转换的类型
   * @param excelReadConfig excel读取的配置项
   * @param <T> 类型
   * @throws IOException IOException
   * @throws IllegalAccessException IllegalAccessException
   * @throws InstantiationException InstantiationException
   * @throws InvocationTargetException InvocationTargetException
   * @return list
   */
  public static <T> List<T> read(File file, Class<T> clazz, ExcelReadConfig excelReadConfig)
      throws IOException, IllegalAccessException, InstantiationException,
          InvocationTargetException {
    String name = file.getName();
    Workbook workbook;
    if (ExcelUtil.isExcel2003(name)) {
      workbook = new HSSFWorkbook(new FileInputStream(file));
    } else if (ExcelUtil.isExcel2007(name)) {
      workbook = new XSSFWorkbook(new FileInputStream(file));
    } else {
      throw new BaseException("The file is not end with xls or xlsx");
    }
    if (excelReadConfig == null) {
      excelReadConfig = new ExcelReadConfig();
    }
    return ExcelUtil.processExcel(workbook, clazz, excelReadConfig);
  }

  /**
   * @param multipartFile excel文件
   * @param clazz excel数据要转换的类型
   * @param excelReadConfig excel读取的配置项
   * @param <T> 类型
   * @throws IOException IOException
   * @throws IllegalAccessException IllegalAccessException
   * @throws InstantiationException InstantiationException
   * @throws InvocationTargetException InvocationTargetException
   * @return list
   */
  public static <T> List<T> read(
      MultipartFile multipartFile, Class<T> clazz, ExcelReadConfig excelReadConfig)
      throws IOException, IllegalAccessException, InstantiationException,
          InvocationTargetException {
    String name = multipartFile.getOriginalFilename();
    Workbook workbook;
    if (ExcelUtil.isExcel2003(name)) {
      workbook = new HSSFWorkbook(multipartFile.getInputStream());
    } else if (ExcelUtil.isExcel2007(name)) {
      workbook = new XSSFWorkbook(multipartFile.getInputStream());
    } else {
      throw new BaseException("The file is not end with xls or xlsx");
    }
    if (excelReadConfig == null) {
      excelReadConfig = new ExcelReadConfig();
    }
    return ExcelUtil.processExcel(workbook, clazz, excelReadConfig);
  }
}
