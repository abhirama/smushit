/**
 * Created by IntelliJ IDEA.
 * User: abhirama
 * Date: Oct 18, 2010
 * Time: 10:34:31 PM
 * To change this template use File | Settings | File Templates.
 */
package com.abhyrama.smushit;

import java.io.FileFilter;
import java.io.File;

public class DirectoryFilter implements FileFilter {
  public boolean accept(File file) {
    return file.isDirectory();
  }
}
