/**
 * Created by IntelliJ IDEA.
 * User: abhirama
 * Date: Oct 17, 2010
 * Time: 1:22:41 PM
 * To change this template use File | Settings | File Templates.
 */
package com.abhyrama.smushit;

import java.io.File;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class FileTraverser {
  protected final String rootDirectory;
  //this has to be in lower case
  protected final Set<String> validFileExtensions;

  protected final boolean isFileExtensionListSupplied;
  protected final int maximumFileSize;

  protected List<String> files = new LinkedList<String>();

  public FileTraverser(String rootDirectory, Set<String> validFileExtension, int maximumFileSize) {
    this.rootDirectory = rootDirectory;
    //if this is empty we assume all files are valid
    this.validFileExtensions = validFileExtension;
    this.maximumFileSize = maximumFileSize;

    if (this.validFileExtensions != null && this.validFileExtensions.size() > 0) {
      this.isFileExtensionListSupplied = true;
    } else {
      this.isFileExtensionListSupplied = false;
    }
  }

  public List<String> getFiles() {
    this.getFilesHelper(new File(this.rootDirectory));
    return this.files;
  }

  protected void getFilesHelper(File directory) {
    File[] files = directory.listFiles();
    for (File file : files) {
      if (file.isDirectory()) {
        this.getFilesHelper(file);
      } else {
        if (this.isAcceptedFile(file)) {
          this.files.add(file.getAbsolutePath());
        }
      }
    }
  }

  protected boolean isAcceptedFile(File file) {
    return this.isAcceptedFileExtension(file) && this.isWithinFileSize(file);
  }

  protected boolean isAcceptedFileExtension(File file) {
    if (this.isFileExtensionListSupplied) {
      return this.validFileExtensions.contains(Utils.getExtension(file.toString()));
    }

    //if the extension while list is not defined it means all files have to be listed
    return true;
  }

  protected boolean isWithinFileSize(File file) {
    return file.length() <= this.maximumFileSize;
  }

  public static void main(String[] args) {
    Set<String> validFiles = new HashSet<String>();
    validFiles.add("jpg");
    validFiles.add("jpeg");
    FileTraverser fileTraverser = new FileTraverser("D:\\projects\\burrp\\local\\mobile-api-branch\\web\\images\\nye\\download", validFiles, 1000000);
    System.out.println(fileTraverser.getFiles());
  }
}
