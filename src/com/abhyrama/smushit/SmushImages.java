/**
 * Created by IntelliJ IDEA.
 * User: abhirama
 * Date: Oct 18, 2010
 * Time: 10:23:46 PM
 * To change this template use File | Settings | File Templates.
 */
package com.abhyrama.smushit;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.*;

public class SmushImages {
  protected final String rootDirectory;
  protected final Set<String> acceptedFileExtensions;

  public SmushImages(String rootDirectory, Set<String> acceptedFileExtensions) {
    this.rootDirectory = rootDirectory;
    this.acceptedFileExtensions = acceptedFileExtensions;
  }

  protected final FileFilter myFileFilter = new MyFileFilter(this.acceptedFileExtensions, SmushIt.MAX_FILE_SIZE);
  protected final FileFilter directoryFilter = new DirectoryFilter();

  public void smush() throws IOException {
    this.smushHelper(new File(this.rootDirectory));
  }

  protected void smushHelper(File directory) throws IOException {
    File[] images = directory.listFiles(this.myFileFilter);

    if (images.length > 0) {
      SmushIt smushIt = new SmushIt();
      smushIt.addFiles(this.arrayToList(images));
      List<SmushItResultVo> smushItResultVos = smushIt.smush();

      this.replaceWithSmushedImages(directory, smushItResultVos);
    }

    File[] directories = directory.listFiles(this.directoryFilter);

    if (directories.length > 0) {
      for (File ddirectory : directories) {
        this.smushHelper(ddirectory);
      }
    }
  }

  protected List<String> arrayToList(File[] files) {
    List<String> fileNames = new LinkedList<String>();
    for (File file : files) {
      fileNames.add(file.toString());
    }

    return fileNames;
  }

  protected void replaceWithSmushedImages(File directory, List<SmushItResultVo> smushItResultVos) throws IOException {
    ImageDownloader imageDownloader = new ImageDownloader(directory.toString());

    //local copy is made because we do not want to tamper the original list
    List<SmushItResultVo> smushedImages = new LinkedList<SmushItResultVo>();

    for (SmushItResultVo smushItResultVo : smushItResultVos) {
      if (smushItResultVo.getSmushedImageUrl() != null) {
        smushedImages.add(smushItResultVo);
      }
    }
    
    imageDownloader.download(smushedImages);
  }

  public static void main(String[] args) throws IOException {
    Set<String> validFiles = new HashSet<String>();
    validFiles.add("gif");
    validFiles.add("png");
    validFiles.add("jpg");
    validFiles.add("jpeg");

    SmushImages smushImages = new SmushImages("D:\\icons", validFiles);
    smushImages.smush();
  }
}
