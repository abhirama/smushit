/*
 * A command line interface to yahoo!'s smush.it lossless image compression utility - http://www.smushit.com/ysmush.it/
 * http://bitbucket.org/abhirama/smushit
 *
 * Copyright 2010, Abhirama
 * Licensed under the MIT license.
 * http://abhirama.wordpress.com/
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
