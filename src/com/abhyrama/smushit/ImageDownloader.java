/*
 * A command line interface to yahoo!'s smush.it lossless image compression utility - http://www.smushit.com/ysmush.it/
 * http://bitbucket.org/abhirama/smushit
 *
 * Copyright 2010, Abhirama
 * http://abhirama.wordpress.com/
 * You can use this software the way you want as long as you keep this notice.
 */
package com.abhyrama.smushit;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
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

    BufferedInputStream bufferedInputStream = new BufferedInputStream(url.openStream());

    if (this.verbose) {
      System.out.println("Downloaded smushed image - " + stringUrl);
    }

    String imageName = Utils.getImageNameFromUrl(stringUrl);

    //for some reason, the response json has image names in the following
    //form - http:\/\/ysmushit.zenfs.com\/results\/5d4d714f%2Fsmush%2FdogTag.png
    imageName = Utils.replaceJunkCharacterInUrlImageName(imageName);

    String savedImage = this.downloadDirectory + File.separator + imageName;
    File outfile = new File(savedImage);
    FileOutputStream fileOutputStream = new FileOutputStream(savedImage);

    try {
      int byteRead = -1;

      while ((byteRead = bufferedInputStream.read()) != -1) {
        fileOutputStream.write(byteRead);
      }
    } finally {
      fileOutputStream.flush();
      fileOutputStream.close();
    }

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
