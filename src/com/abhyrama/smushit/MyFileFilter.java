/**
 * Created by IntelliJ IDEA.
 * User: abhirama
 * Date: Oct 18, 2010
 * Time: 3:23:30 PM
 * To change this template use File | Settings | File Templates.
 */
package com.abhyrama.smushit;

import java.io.File;
import java.util.Set;

public class MyFileFilter implements java.io.FileFilter {
  protected final Set<String> acceptedFileExtensions;
  protected final int maximumFileSize;
  protected final boolean isFileExtensionListSupplied;

  public MyFileFilter(Set<String> acceptedFileExtensions, int maximumFileSize) {
    this.acceptedFileExtensions = acceptedFileExtensions;
    this.maximumFileSize = maximumFileSize;

    if (this.acceptedFileExtensions != null && this.acceptedFileExtensions.size() > 0) {
      this.isFileExtensionListSupplied = true;
    } else {
      this.isFileExtensionListSupplied = false;
    }
  }

  public boolean accept(File file) {
    return !file.isDirectory() && this.isAcceptedFileExtension(file) && this.isWithinFileSize(file);
  }

  protected boolean isAcceptedFileExtension(File file) {
    if (this.isFileExtensionListSupplied) {
      return this.acceptedFileExtensions.contains(Utils.getExtension(file.toString()));
    }

    //if the extension while list is not defined it means all files have to be listed
    return true;
  }

  protected boolean isWithinFileSize(File file) {
    return file.length() <= this.maximumFileSize;
  }
}
