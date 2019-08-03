package ir.atsignsina.bookshelf.concerns.utils;

import java.util.Collection;
import java.util.Map;

public class NilUtils {
  /**
   * Check object is null or not
   *
   * @param object object for check
   * @return true if object is null. Otherwise return false
   */
  public static boolean checkNull(Object object) {
    return object == null;
  }

  /**
   * Check String is null or is empty
   *
   * @param string string for check
   * @return true if string is null or string is object but empty. Otherwise return false
   */
  public static boolean checkNullOrEmpty(String string) {
    return checkNull(string) || string.isEmpty();
  }

  /**
   * Check Collection is null or is empty
   *
   * @param collection collection for check
   * @return true if collection is null or collection is object but empty. Otherwise return false
   */
  public static boolean checkNullOrEmpty(Collection collection) {
    return checkNull(collection) || collection.isEmpty();
  }

  /**
   * Check map is null or is empty
   *
   * @param map map for check
   * @return true if map is null or map is object but empty. Otherwise return false
   */
  public static boolean checkNullOrEmpty(Map map) {
    return checkNull(map) || map.isEmpty();
  }

  /**
   * Check Number is null or is zero
   *
   * @param number number for check
   * @return true if number is null or number is object but zero. In case of double and float this
   *     method use their integer value. Otherwise return false
   */
  public static boolean checkNullOrZero(Number number) {
    return checkNull(number) || number.intValue() == 0;
  }
}
