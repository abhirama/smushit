/**
 * Created by IntelliJ IDEA.
 * User: abhirama
 * Date: Oct 17, 2010
 * Time: 1:22:41 PM
 * To change this template use File | Settings | File Templates.
 */
package com.abhyrama.smushit;

import java.io.File;
import java.io.FileFilter;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class FileTraverser {
  protected final String rootDirectory;
  protected final FileFilter fileFilter;

  protected List<String> files = new LinkedList<String>();

  public FileTraverser(String rootDirectory, FileFilter fileFilter) {
    this.rootDirectory = rootDirectory;
    this.fileFilter = fileFilter;
  }

  public List<String> getFiles() {
    this.getFilesHelper(new File(this.rootDirectory));
    return this.files;
  }

  protected void getFilesHelper(File directory) {
    File[] files;
    if (this.fileFilter != null) {
      files = directory.listFiles(this.fileFilter);
    } else {
      files = directory.listFiles();
    }

    for (File file : files) {
      if (file.isDirectory()) {
        this.getFilesHelper(file);
      } else {
        this.files.add(file.getAbsolutePath());
      }
    }
  }

  public static void main(String[] args) {
    Set<String> validFiles = new HashSet<String>();
    validFiles.add("jpg");
    validFiles.add("jpeg");
    FileTraverser fileTraverser = new FileTraverser("D:\\projects\\burrp\\local\\mobile-api-branch\\web\\images\\nye\\download", new MyFileFilter(validFiles, SmushIt.MAX_FILE_SIZE));
    System.out.println(fileTraverser.getFiles());
  }
}
