/*
 * A command line interface to yahoo!'s smush.it lossless image compression utility - http://www.smushit.com/ysmush.it/
 * http://bitbucket.org/abhirama/smushit
 *
 * Copyright 2010, Abhirama
 * Licensed under the MIT license.
 * http://abhirama.wordpress.com/
 */
package com.abhyrama.smushit;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public class ImageDownloader {
  protected final String downloadDirectory;

  public ImageDownloader(String downloadDirectory) {
    this.downloadDirectory = downloadDirectory;
  }

  protected boolean verbose;

  public boolean isVerbose() {
    return verbose;
  }

  public void setVerbose(boolean verbose) {
    this.verbose = verbose;
  }

  public File download(SmushItResultVo smushItResultVo) throws IOException {
    String stringUrl = smushItResultVo.getSmushedImageUrl();

    URL url = new URL(stringUrl);
    BufferedImage image = ImageIO.read(url);

    if (this.verbose) {
      System.out.println("Downloaded smushed image - " + stringUrl);
    }

    String imageExtension = Utils.getExtension(stringUrl);
    String imageName = Utils.getImageNameFromUrl(stringUrl);

    String savedImage = this.downloadDirectory + File.separator + imageName;
    File outfile = new File(savedImage);
    //here we are assuming there is an image writer for the corresponding image extenion/type. Do not know how it will
    //behave if passed an image type for which there is no writer. todo check this
    //overwrites an image if it already exists
    ImageIO.write(image, imageExtension, outfile);

    if (this.verbose) {
      System.out.println("Saved image - " + savedImage);
    }

    return outfile;
  }

  public void download(List<SmushItResultVo> smushItResultVos) throws IOException {
    for (SmushItResultVo smushItResultVo : smushItResultVos) {
      this.download(smushItResultVo);
    }
  }
}
