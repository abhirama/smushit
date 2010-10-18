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
    int totalImageSize = 0;
    int convertedImageSize = 0;

    for (SmushItResultVo smushItResultVo : this.smushItResultVos) {
      int originalImageSize = Integer.parseInt(smushItResultVo.getSourceImageSize());
      totalImageSize = totalImageSize + originalImageSize;

      if (smushItResultVo.getSmushedImageUrl() != null) {
        noOfConvertedFiles = noOfConvertedFiles + 1;
        convertedImageSize = convertedImageSize + Integer.parseInt(smushItResultVo.getSmushedImageSize());
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
