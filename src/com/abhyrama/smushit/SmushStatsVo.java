/**
 * Created by IntelliJ IDEA.
 * User: abhirama
 * Date: Oct 17, 2010
 * Time: 2:38:43 PM
 * To change this template use File | Settings | File Templates.
 */
package com.abhyrama.smushit;

public class SmushStatsVo {
  protected int totalUploadedImagesCount;
  protected int smushedImagesCount;
  protected int totalUploadedImagesSize;
  protected int totalSmushedImagesSize;

  public int getTotalUploadedImagesCount() {
    return totalUploadedImagesCount;
  }

  public void setTotalUploadedImagesCount(int totalUploadedImagesCount) {
    this.totalUploadedImagesCount = totalUploadedImagesCount;
  }

  public int getSmushedImagesCount() {
    return smushedImagesCount;
  }

  public void setSmushedImagesCount(int smushedImagesCount) {
    this.smushedImagesCount = smushedImagesCount;
  }

  public float getTotalUploadedImagesSize() {
    return totalUploadedImagesSize;
  }

  public void setTotalUploadedImagesSize(int totalUploadedImagesSize) {
    this.totalUploadedImagesSize = totalUploadedImagesSize;
  }

  public float getTotalSmushedImagesSize() {
    return totalSmushedImagesSize;
  }

  public void setTotalSmushedImagesSize(int totalSmushedImagesSize) {
    this.totalSmushedImagesSize = totalSmushedImagesSize;
  }

  public String toString() {
    return "Total images uploaded=" + this.totalUploadedImagesCount + ", Total smushed images=" + this.smushedImagesCount
        + ", Total uploaded images size=" + this.totalUploadedImagesSize + ", Total smushed image size" + this.totalSmushedImagesSize;
  }

}
