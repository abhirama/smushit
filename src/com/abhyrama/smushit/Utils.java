/*
 * A command line interface to yahoo!'s smush.it lossless image compression utility - http://www.smushit.com/ysmush.it/
 * http://bitbucket.org/abhirama/smushit
 *
 * Copyright 2010, Abhirama
 * http://abhirama.wordpress.com/
 * You can use this software the way you want as long as you keep this notice.
 */
package com.abhyrama.smushit;

public class Utils {
  private static final String IMAGE_NAME_JUNK_CHARACTER = ".*?%2F";
  private static final String IMAGE_URL_JUNK_CHARACTER = ".*?%2Fsmush%2F";

  protected static String getExtension(String fileName) {
    String ext = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
    return ext.toLowerCase();
  }

  protected static String getImageNameFromUrl(String url) {
    return url.substring(url.lastIndexOf("/") + 1, url.length());
  }

  protected static String replaceJunkCharacterInImageNames(String image) {
    return image.replaceAll(IMAGE_NAME_JUNK_CHARACTER, "");
  }

  protected static String replaceJunkCharacterInUrlImageName(String image) {
    return image.replaceAll(IMAGE_URL_JUNK_CHARACTER, "");
  }
}
