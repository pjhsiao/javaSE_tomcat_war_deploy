package com.deploy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

public class UnDeploy extends DeployProperties implements Runnable{
	
	final static String undeployAppName ="warName";
	
	/**
	 * 2011/8/4
	 * main
	 * @param args
	 */
	public static void main(String[] args){
		 Thread t = new Thread(new UnDeploy());
		 t.run();
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		String[] ips = getProps().getProperty(undeployAppName).split(",");
		for(String ip: ips){
//			System.out.println("http://"+ip+"/");
			unDeploy("http://"+ip+"/");
		}
	}
	
	@SuppressWarnings("unused")
	private void unDeploy(String url){
		try {
            DefaultHttpClient httpclient = new DefaultHttpClient();

            httpclient.getCredentialsProvider().setCredentials(
                    new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT), 
                    new UsernamePasswordCredentials("tomcat_account", "tomcat_password"));
            
            HttpPost httppost = new HttpPost(url);

            System.out.println("executing request " + httppost.getRequestLine());
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity resEntity = response.getEntity();

            System.out.println("----------------login-----------------------");
            InputStream is = resEntity.getContent();
			InputStreamReader inputStreamReader =  new InputStreamReader(is,"Big5"); 
			BufferedReader br = new BufferedReader(inputStreamReader);
			String contextLine  = "";
			 while((contextLine=br.readLine())!=null)
				 System.out.println(contextLine);
			 
			 
			System.out.println("-----------------undeploy-----------------------"); 
			httppost = new HttpPost(url+"/manager/html/undeploy?path=/"+undeployAppName);
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
