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

  public SmushStatsVo getSmushStats() {
    int noOfConvertedFiles = 0;
    float totalImageSize = 0;
    float convertedImageSize = 0;

    for (SmushItResultVo smushItResultVo : this.smushItResultVos) {
      float originalImageSize = Float.parseFloat(smushItResultVo.getSourceImageSize());
      totalImageSize = totalImageSize + originalImageSize;

      if (!smushItResultVo.getSmushedImageUrl().equals("null")) {
        noOfConvertedFiles = noOfConvertedFiles + 1;
        convertedImageSize = convertedImageSize + Float.parseFloat(smushItResultVo.getSmushedImageSize());
      } else {
        convertedImageSize = convertedImageSize + originalImageSize; 
      }
    }
    
    SmushStatsVo smushStatsVo = new SmushStatsVo();
    smushStatsVo.setTotalUploadedImagesCount(this.smushItResultVos.size());
    smushStatsVo.setSmushedImagesCount(noOfConvertedFiles);
    smushStatsVo.setTotalUploadedImagesSize(totalImageSize);
    smushStatsVo.setTotalSmushedImagesSize(convertedImageSize);

    return smushStatsVo;
  }
}
