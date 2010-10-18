/**
 * Created by IntelliJ IDEA.
 * User: abhirama
 * Date: Oct 18, 2010
 * Time: 1:54:01 PM
 * To change this template use File | Settings | File Templates.
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

  public File download(SmushItResultVo smushItResultVo) throws IOException {
    String stringUrl = smushItResultVo.getSmushedImageUrl();
    URL url = new URL(stringUrl);
    BufferedImage image = ImageIO.read(url);
    String imageExtension = Utils.getExtension(stringUrl);
    String imageName = Utils.getImageNameFromUrl(stringUrl);

    File outfile = new File(this.downloadDirectory + File.separator + imageName);
    ImageIO.write(image, imageExtension, outfile);

    return outfile;
  }

  public void download(List<SmushItResultVo> smushItResultVos) throws IOException {
    for (SmushItResultVo smushItResultVo : smushItResultVos) {
      this.download(smushItResultVo);
    }
  }
}
