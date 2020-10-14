package com.example.mail.utils.task;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HttpClientUtil {
	
	public static long sessionCreateTime = System.currentTimeMillis();

	public static String request(Map<String,String> params,String url,int timeout,String method){
		String content = null;
		InputStream in = null;
		try {
			HttpParams params0 = new BasicHttpParams();
			List<NameValuePair> formparams = new ArrayList<NameValuePair>(); 
			//写入请求
			if(params!=null){
				Set<String> keys = params.keySet();
				for(String key : keys){
					formparams.add(new BasicNameValuePair(key, String.valueOf(params.get(key))));
				}
			}
			DefaultHttpClient client = new DefaultHttpClient(params0);
			client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, timeout); 
			client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, timeout);
			
			UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
			HttpResponse response = null;
			if("post".equals(method)){
				HttpPost post = new HttpPost(url);
				post.setEntity(uefEntity);
				response = client.execute(post);
			}else{
				HttpGet get = new HttpGet(url);
				response = client.execute(get);
			}
			
			BufferedHttpEntity bufferedHttpEntity = new BufferedHttpEntity(response.getEntity());
			in = bufferedHttpEntity.getContent();
			byte b[] = new byte[in.available()];
			int total = in.read(b);
			if(total==-1){
				content = "";
			}else{
				content = new String(b,0,total,"utf-8");
			}
			return content;
		} catch (Exception e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
//			content = "{\"rtState\":false,\"rtMsg\":\""+e.getMessage()+",网络未连接\",\"rtData\":{}}";
			content = null;
			return content;
		}
	}
	
	public static String requestPost(Map<String,String> params,String url){
		return request(params,url,1000*5,"post");
	}
	
	public static String requestGet(String url){
		return request(null,url,1000*5,"get");
	}
	
	
	public static void download(Map<String,String> cookie,Map<String,String> params,String url,String filePath){
		try {
			HttpParams params0 = new BasicHttpParams();
			List<NameValuePair> formparams = new ArrayList<NameValuePair>(); 
			//写入请求
			if(params!=null){
				Set<String> keys = params.keySet();
				for(String key : keys){
					formparams.add(new BasicNameValuePair(key, String.valueOf(params.get(key))));
				}
			}
			DefaultHttpClient client = new DefaultHttpClient(params0);
			

			UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
			HttpPost post = new HttpPost(url);
			post.setEntity(uefEntity);
			HttpResponse response = client.execute(post);
			BufferedHttpEntity bufferedHttpEntity = new BufferedHttpEntity(response.getEntity());
			InputStream in = bufferedHttpEntity.getContent();
			
			if(in.available()==13){//尝试判断一下失效
				byte smallByte[] = new byte[13];
				in.read(smallByte);
				in.close();
				String result = new String(smallByte);
				if("{\"timeout\":1}".equals(result)){//失效
				}

				uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
				post = new HttpPost(url);
				post.setEntity(uefEntity);
				response = client.execute(post);
				bufferedHttpEntity = new BufferedHttpEntity(response.getEntity());
				in = bufferedHttpEntity.getContent();
			}
			
			byte b[] = new byte[4096];
			int length = 0;
			File file = new File(filePath);
			if(!file.exists()){
				file.getParentFile().mkdirs();
				file.createNewFile();
			}
			OutputStream output = new FileOutputStream(file);
			while((length=in.read(b))!=-1){
				output.write(b,0,length);
			}
			output.close();
			in.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
