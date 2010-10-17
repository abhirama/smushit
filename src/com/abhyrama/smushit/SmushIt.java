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


public class SmushIt {
  public static void main(String[] args) throws IOException {
    HttpClient httpClient = new DefaultHttpClient();

    HttpPost httpPost = new HttpPost("http://www.smushit.com/ysmush.it/ws.php");

    MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

    multipartEntity.addPart("files[]", new FileBody(new File("D:\\projects\\personal\\30x30.PNG")));

    httpPost.setEntity(multipartEntity);

    ResponseHandler<String> responseHandler = new BasicResponseHandler();

    String responseBody = httpClient.execute(httpPost, responseHandler);

    System.out.println(responseBody);
  }
}
