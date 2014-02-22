package com.deploy;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;

public class Deploy extends DeployProperties implements Runnable{
	
	final static String filepath = "C:/Users/Hsiao/Desktop/";
	final static String deployAppName ="warName";
	
	/**
	 *2011/8/10 
	 * @param args
	 */
	public static void main(String[] args) {
		 Thread t = new Thread(new Deploy());
		 t.run();
		
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		String[] ips = getProps().getProperty(deployAppName).split(",");
		for(String ip: ips){
			deploy("http://"+ip+"/");
		}
	}
	
	
	private void deploy(String url){
		try {
			//建立 HttpClient 物件，來提供Http Client端功能
			DefaultHttpClient httpclient = new DefaultHttpClient();
			
			//提供連線時須提交的帳號密碼
            httpclient.getCredentialsProvider().setCredentials(
                    new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT), 
                    new UsernamePasswordCredentials("tomcat_account", "tomcat_password"));
            //建立一個post物件
            HttpPost httppost = new HttpPost(url);

            System.out.println("executing request " + httppost.getRequestLine());
            //使用httpclient物件做post物件提交，並返回一個rep(在此已經成功提交tomcat manager帳密給tomcat server side)
            HttpResponse response = httpclient.execute(httppost);
            //取得rep的實體，並讀取內文
            HttpEntity resEntity = response.getEntity();
            System.out.println("--------------------login successsful--------------------");
            InputStream is = resEntity.getContent();
			InputStreamReader inputStreamReader =  new InputStreamReader(is,"Big5"); 
			BufferedReader br = new BufferedReader(inputStreamReader);
			String contextLine  = "";
			 while((contextLine=br.readLine())!=null)
				 System.out.println(contextLine);
			 
			 System.out.println("-----------------deploy start-----------------------");
			 //建立一個httppost物件，參數為tomcat deploy頁面 位址
			 httppost = new HttpPost(url+"manager/html/upload");
			 
				
			 FileBody uploadFilePart = new FileBody(new File(filepath+deployAppName+".war"));
			 //建立httppost的夾帶檔，用來夾帶war檔
			 MultipartEntity warEntity = new MultipartEntity();
			 warEntity.addPart("deployWar", uploadFilePart);
			 httppost.setEntity(warEntity);
			 
			 response = httpclient.execute(httppost);
	         resEntity = response.getEntity();
	         
             is = resEntity.getContent();
			 inputStreamReader =  new InputStreamReader(is,"Big5"); 
			 br = new BufferedReader(inputStreamReader);
			 contextLine  = "";
			 while((contextLine=br.readLine())!=null)
				 System.out.println(contextLine);
				
			 httpclient.getConnectionManager().shutdown();
		} catch (ClientProtocolException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	}
}
