/**
 * Created by IntelliJ IDEA.
 * User: abhirama
 * Date: Oct 18, 2010
 * Time: 2:17:12 PM
 * To change this template use File | Settings | File Templates.
 */
package com.abhyrama.smushit;

import java.io.File;

public class Utils {
  protected static String getExtension(String fileName) {
    String ext = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
    return ext.toLowerCase();
  }

  protected static String getImageNameFromUrl(String url) {
    return url.substring(url.lastIndexOf("/") + 1, url.length());
  }
}
