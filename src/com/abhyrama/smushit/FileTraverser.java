/**
 * Created by IntelliJ IDEA.
 * User: abhirama
 * Date: Oct 17, 2010
 * Time: 1:22:41 PM
 * To change this template use File | Settings | File Templates.
 */
package com.abhyrama.smushit;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class FileTraverser {
  protected final String rootDirectory;
  protected List<String> files = new LinkedList<String>();

  public FileTraverser(String rootDirectory) {
    this.rootDirectory = rootDirectory;
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
        this.files.add(file.getAbsolutePath());
      }
    }
  }

  public static void main(String[] args) {
    FileTraverser fileTraverser = new FileTraverser("D:\\projects\\personal\\smushit");
    System.out.println(fileTraverser.getFiles());
  }
}
