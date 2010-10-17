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
import java.util.Map;

import flexjson.JSONDeserializer;


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

  public List<SmushItResultVo> smush() throws IOException {
    HttpClient httpClient = new DefaultHttpClient();

    HttpPost httpPost = new HttpPost(SMUSHIT_URL);

    MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
    this.addFilesToRequest(multipartEntity);

    httpPost.setEntity(multipartEntity);

    ResponseHandler<String> responseHandler = new BasicResponseHandler();

    String responseBody = httpClient.execute(httpPost, responseHandler);

    List<SmushItResultVo> smushItResultVos = this.transformToResultVo(responseBody);

    return smushItResultVos;
  }

  protected List<SmushItResultVo> transformToResultVo(String jsonResponse) {
    List<SmushItResultVo> smushItResultVos = new LinkedList<SmushItResultVo>();

    if (this.files.size() > 1) {
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
  
  protected void addFilesToRequest(MultipartEntity multipartEntity) {
    for (String file : this.files) {
      multipartEntity.addPart(FILE_PARAM_NAME, new FileBody(new File(file)));
    }
  }

  public static void main(String[] args) throws IOException {
    SmushIt smushIt = new SmushIt();
/*    smushIt.addFile("D:\\projects\\personal\\30x30.PNG");
    smushIt.addFile("D:\\projects\\personal\\30x30.PNG");*/
    smushIt.addFile("D:\\projects\\burrp\\tv\\Production1.1.12\\web\\images\\dancing_banana.gif");

    List<SmushItResultVo> smushItResultVos = smushIt.smush();
    System.out.println(smushItResultVos);
  }
}
