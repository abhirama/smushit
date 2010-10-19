/*
 * A command line interface to yahoo!'s smush.it lossless image compression utility - http://www.smushit.com/ysmush.it/
 * http://bitbucket.org/abhirama/smushit
 *
 * Copyright 2010, Abhirama
 * Licensed under the MIT license.
 * http://abhirama.wordpress.com/
 */
package com.abhyrama.smushit;

public class Utils {
  protected static String getExtension(String fileName) {
    String ext = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
    return ext.toLowerCase();
  }

  protected static String getImageNameFromUrl(String url) {
    return url.substring(url.lastIndexOf("/") + 1, url.length());
  }
}
