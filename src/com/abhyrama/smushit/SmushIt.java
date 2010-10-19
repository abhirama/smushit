/*
 * A command line interface to yahoo!'s smush.it lossless image compression utility - http://www.smushit.com/ysmush.it/
 * http://bitbucket.org/abhirama/smushit
 *
 * Copyright 2010, Abhirama
 * Licensed under the MIT license.
 * http://abhirama.wordpress.com/
 */
package com.abhyrama.smushit;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.content.FileBody;

import java.io.IOException;
import java.io.File;
import java.util.*;

import flexjson.JSONDeserializer;


public class SmushIt {
  public static final String FILE_PARAM_NAME = "files[]";
  public static final String SMUSHIT_URL = "http://www.smushit.com/ysmush.it/ws.php";
  public static final int MAX_FILE_SIZE = 1000000; //corresponds to 1mb as smushit cannot smush images of size greater than 1 mb
  public static final String SMUSHIT_RESPONSE_ARRAY_START = "[";
  public static final String SMUSHIT_RESPONSE_ARRAY_END = "]";

  //we need the batch size as a huge image upload results in chunked response which the http library cannot handle well
  //or I do not know how to handle chunked responses :)
  public static int SMUSH_BATCH_SIZE = 10;

  protected List<String> files = new LinkedList<String>();

  protected boolean verbose;

  public boolean isVerbose() {
    return verbose;
  }

  public void setVerbose(boolean verbose) {
    this.verbose = verbose;
  }

  public void addFile(String file) {
    this.files.add(file);
  }

  public void addFiles(List<String> files) {
    this.files.addAll(files);
  }

  public List<SmushItResultVo> smush() throws IOException {
    List<SmushItResultVo> smushItResultVos = new LinkedList<SmushItResultVo>();

    int count = this.files.size();

    int startIndex = 0;
    int endIndex = SMUSH_BATCH_SIZE;

    List<String> subList = null;
    if (this.files.size() <= SMUSH_BATCH_SIZE) {
      subList = this.files;
      smushItResultVos.addAll(this.smushHelper(subList));
    } else {
      subList = this.files.subList(startIndex, endIndex);

      while (subList.size() != 0) {
        smushItResultVos.addAll(this.smushHelper(subList));
        startIndex = endIndex;

        endIndex = endIndex + SMUSH_BATCH_SIZE;
        if (endIndex > count) {
          endIndex = count;
        }

        subList = this.files.subList(startIndex, endIndex);
      }
    }

    return smushItResultVos;
  }

  protected void printFileNames(List<String> files) {
    for (String file : files) {
      System.out.println(file);
    }

  }

  protected List<SmushItResultVo> smushHelper(List<String> files) throws IOException {
    if (this.verbose) {
      System.out.println("Smushing files:");
      this.printFileNames(files);
    }

    HttpClient httpClient = new DefaultHttpClient();

    HttpPost httpPost = new HttpPost(SMUSHIT_URL);

    MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
    this.addFilesToRequest(multipartEntity, files);

    httpPost.setEntity(multipartEntity);

    ResponseHandler<String> responseHandler = new BasicResponseHandler();

    String responseBody = httpClient.execute(httpPost, responseHandler);

    List<SmushItResultVo> smushItResultVos = this.transformToResultVo(responseBody);

    if (this.verbose) {
      for (SmushItResultVo smushItResultVo : smushItResultVos) {
        System.out.println("Source Image:" + smushItResultVo.getSourceImage()
            + ", Source image size:" + smushItResultVo.getSourceImageSize()
            + ", Smushed image size:" + smushItResultVo.getSmushedImageSize()
            + ", Percentage saving:" + smushItResultVo.getSavingPercentage()
        );
      }
    }

    httpClient.getConnectionManager().shutdown();

    return smushItResultVos;
  }

  protected List<SmushItResultVo> transformToResultVo(String jsonResponse) {
    List<SmushItResultVo> smushItResultVos = new LinkedList<SmushItResultVo>();

    if (this.isResponseArray(jsonResponse)) {
      List<Map> result = new JSONDeserializer<List<Map>>().deserialize(jsonResponse);
      for (Map map : result) {
        smushItResultVos.add(SmushItResultVo.create(map));
      }
    } else {
      Map result = new JSONDeserializer<Map>().deserialize(jsonResponse);
      smushItResultVos.add(SmushItResultVo.create(result));
    }

    return smushItResultVos;
  }

  protected boolean isResponseArray(String jsonResponse) {
    return jsonResponse.startsWith(SMUSHIT_RESPONSE_ARRAY_START) && jsonResponse.endsWith(SMUSHIT_RESPONSE_ARRAY_END);
  }

  protected void addFilesToRequest(MultipartEntity multipartEntity, List<String> files) {
    for (String file : files) {
      multipartEntity.addPart(FILE_PARAM_NAME, new FileBody(new File(file)));
    }
  }
}
