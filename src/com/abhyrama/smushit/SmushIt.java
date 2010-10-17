/**
 * Created by IntelliJ IDEA.
 * User: abhirama
 * Date: Oct 17, 2010
 * Time: 10:37:32 AM
 * To change this template use File | Settings | File Templates.
 */
package com.abhyrama.smushit;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.content.FileBody;

import java.io.IOException;
import java.io.File;
import java.util.LinkedList;
import java.util.List;


public class SmushIt {
  public static final String FILE_PARAM_NAME = "files[]";
  public static final String SMUSHIT_URL = "http://www.smushit.com/ysmush.it/ws.php";

  protected List<String> files = new LinkedList<String>();

  public void addFile(String file) {
    this.files.add(file);
  }

  public void addFiles(List<String> files) {
    this.files.addAll(files);
  }

  public void smush() throws IOException {
    HttpClient httpClient = new DefaultHttpClient();

    HttpPost httpPost = new HttpPost(SMUSHIT_URL);

    MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
    this.addFilesToRequest(multipartEntity);

    httpPost.setEntity(multipartEntity);

    ResponseHandler<String> responseHandler = new BasicResponseHandler();

    String responseBody = httpClient.execute(httpPost, responseHandler);

    System.out.println(responseBody);
  }
  
  protected void addFilesToRequest(MultipartEntity multipartEntity) {
    for (String file : this.files) {
      multipartEntity.addPart(FILE_PARAM_NAME, new FileBody(new File(file)));
    }
  }

  public static void main(String[] args) throws IOException {
    SmushIt smushIt = new SmushIt();
    smushIt.addFile("D:\\projects\\personal\\30x30.PNG");
    smushIt.smush();
  }
}
