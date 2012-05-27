/*
 * A command line interface to yahoo!'s smush.it lossless image compression utility - http://www.smushit.com/ysmush.it/
 * http://bitbucket.org/abhirama/smushit
 *
 * Copyright 2010, Abhirama
 * http://abhirama.wordpress.com/
 * You can use this software the way you want as long as you keep this notice.
 */
package com.abhyrama.smushit;

import java.util.Map;

public class SmushItResultVo {
  public static final String JSON_RESPONSE_PARAM_SRC = "src";
  public static final String JSON_RESPONSE_PARAM_SRC_SIZE = "src_size";
  public static final String JSON_RESPONSE_PARAM_DEST = "dest";
  public static final String JSON_RESPONSE_PARAM_DEST_SIZE = "dest_size";
  public static final String JSON_RESPONSE_PARAM_PERCENT = "percent";

  protected String sourceImage;
  protected String sourceImageSize;
  protected String smushedImageUrl;
  protected String smushedImageSize;
  protected String savingPercentage;

  public String getSourceImage() {
    return sourceImage;
  }

  public void setSourceImage(String sourceImage) {
    this.sourceImage = sourceImage;
  }

  public String getSourceImageSize() {
    return sourceImageSize;
  }

  public void setSourceImageSize(String sourceImageSize) {
    this.sourceImageSize = sourceImageSize;
  }

  public String getSmushedImageUrl() {
    return smushedImageUrl;
  }

  public void setSmushedImageUrl(String smushedImageUrl) {
    this.smushedImageUrl = smushedImageUrl;
  }

  public String getSmushedImageSize() {
    return smushedImageSize;
  }

  public void setSmushedImageSize(String smushedImageSize) {
    this.smushedImageSize = smushedImageSize;
  }

  public String getSavingPercentage() {
    return savingPercentage;
  }

  public void setSavingPercentage(String savingPercentage) {
    this.savingPercentage = savingPercentage;
  }

  public static SmushItResultVo create(Map map) {
    SmushItResultVo smushItResultVo = new SmushItResultVo();
    //Image names in the response json are being appended with some strings, do not know why this is happening, like this
    // - b224eabc%2FGhostBustersCostume.png
    smushItResultVo.setSourceImage(Utils.replaceJunkCharacterInImageNames(String.valueOf(map.get(JSON_RESPONSE_PARAM_SRC))));
    smushItResultVo.setSourceImageSize((String.valueOf(map.get(JSON_RESPONSE_PARAM_SRC_SIZE))));

    String smushedImageUrl = String.valueOf(map.get(JSON_RESPONSE_PARAM_DEST));
    if ("null".equals(smushedImageUrl)) {
      smushItResultVo.setSmushedImageUrl(null);
    } else {
      smushItResultVo.setSmushedImageUrl(smushedImageUrl);
    }

    smushItResultVo.setSmushedImageSize((String.valueOf(map.get(JSON_RESPONSE_PARAM_DEST_SIZE))));
    smushItResultVo.setSavingPercentage((String.valueOf(map.get(JSON_RESPONSE_PARAM_PERCENT))));

    return smushItResultVo;
  }

  public String toString() {
    String str = "{source image=" + this.sourceImage + ", source image size=" + this.sourceImageSize
        + ", smushed images url=" + this.smushedImageUrl + ", smushed image size=" + this.smushedImageSize
        + ", percentage saving=" + this.savingPercentage + "}";

    return str;
  }
}
