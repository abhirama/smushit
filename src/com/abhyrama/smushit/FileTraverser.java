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
  protected final Set<String> validFileExtensions;

  protected final boolean isFileExtensionListSupplied;

  protected List<String> files = new LinkedList<String>();

  public FileTraverser(String rootDirectory, Set<String> validFileExtension) {
    this.rootDirectory = rootDirectory;
    //if this is empty we assume all files are valid
    this.validFileExtensions = validFileExtension;

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
    if (this.isFileExtensionListSupplied) {
      return this.validFileExtensions.contains(this.getExtension(file));
    }

    //if the extension while list is not defined it means all files have to be listed
    return true;
  }

  protected String getExtension(File file) {
    String filename = file.toString();
    String ext = filename.substring(filename.lastIndexOf(".") + 1, filename.length());
    return ext;
  }

  public static void main(String[] args) {
    Set<String> validFiles = new HashSet<String>();
    validFiles.add("java");
    FileTraverser fileTraverser = new FileTraverser("D:\\projects\\personal\\smushit", validFiles);
    System.out.println(fileTraverser.getFiles());
  }
}
