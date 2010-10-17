/**
 * Created by IntelliJ IDEA.
 * User: abhirama
 * Date: Oct 17, 2010
 * Time: 2:11:49 PM
 * To change this template use File | Settings | File Templates.
 */
package com.abhyrama.smushit;

import java.util.List;

public class SmushStats {
  protected List<SmushItResultVo> smushItResultVos;

  public SmushStats(List<SmushItResultVo> smushItResultVos) {
    this.smushItResultVos = smushItResultVos;
  }

  public void printStarts() {
    int noOfFiles = this.smushItResultVos.size();
    System.out.println("No of images processed:" + noOfFiles);

    int noOfConvertedFiles = 0;
    float totalFileSize = 0;
    float convertedFileSize = 0;

    for (SmushItResultVo smushItResultVo : this.smushItResultVos) {
      totalFileSize = totalFileSize + Float.parseFloat(smushItResultVo.getSourceImageSize());

      if (!smushItResultVo.getSmushedImageUrl().equals("null")) {
        noOfConvertedFiles = noOfConvertedFiles + 1;
        convertedFileSize = convertedFileSize + Float.parseFloat(smushItResultVo.getSmushedImageSize());
      }
    }

    System.out.println("No of images converted:" + noOfConvertedFiles);
    System.out.println("Total uploaded image size:" + totalFileSize);
    System.out.println("Converted image size:" + convertedFileSize);
  }
}
