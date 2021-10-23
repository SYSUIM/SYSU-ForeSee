package com.ForeSee.ForeSee.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;
import org.springframework.stereotype.Component;


/**
 * @author Zhongshsh
 * @ClassName HttpUtil
 * @Description 访问python flask的工具类
 */


@Component
public class HttpUtil {
	
	/**
	 * 使用Get方式获取数据
	 * 
	 * @param url
	 *            URL包括参数，http://HOST/XX?XX=XX&XXX=XXX
	 * @param charset
	 * @return
	 */
	public static String sendGet(String url) {
		String result = "";
		BufferedReader in = null;
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection connection = realUrl.openConnection();
			// 设置通用的请求属性
			connection.setRequestProperty("accept", "*/*");
			
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 建立实际的连接
			connection.connect();
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送GET请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;
	}
 
	/**
	 * POST请求，字符串形式数据
	 * @param url 请求地址
	 * @param param 请求数据
	 * @param charset 编码方式
	 */
	public static String sendPostUrl(String url, String param, String charset) {
 
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

			conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Charset", "utf-8");
			conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded; charset=utf-8");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(param);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(
					conn.getInputStream(), charset));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			//System.out.println("发送 POST 请求出现异常！" + e);
			e.printStackTrace();
		
			
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}
	
    /**
     * 向指定 URL 发送POST方法的请求
     * 
     * @param url
     *            发送请求的 URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("Content-Type", "text/plain; charset=UTF-8");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(),"UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }    
	/**
	 * POST请求，Map形式数据
	 * @param url 请求地址
	 * @param param 请求数据
	 * @param charset 编码方式
	 */
	public static String sendPost(String url, Map<String, String> param,
			String charset) {
 
		StringBuffer buffer = new StringBuffer();
		if (param != null && !param.isEmpty()) {
			for (Map.Entry<String, String> entry : param.entrySet()) {
				buffer.append(entry.getKey()).append("=")
						.append(URLEncoder.encode(entry.getValue()))
						.append("&");
 
			}
		}
		buffer.deleteCharAt(buffer.length() - 1);
 
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(buffer);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(
					conn.getInputStream(), charset));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送 POST 请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}
 
	public static void main(String[] args) {
		 //String httpUrl=jsionStr.replace(" ","");
		String httpUrl="{\"body\":{\"list\":[{\"conveyancename\":\"Y87971\",\"destzone\":\"021WG\",\"endtm\":\"01:20\",\"takeofftm\":\"02:30\",\"arraytm\":\"04:55\",\"volume\":\"14000\",\"isend\":\"是\"},{\"conveyancename\":\"Y87971\",\"destzone\":\"021\",\"endtm\":\"01:20\",\"takeofftm\":\"02:30\",\"arraytm\":\"04:55\",\"volume\":\"14000\",\"isend\":\"是\"},{\"conveyancename\":\"Y87971\",\"destzone\":\"021WG\",\"endtm\":\"01:20\",\"takeofftm\":\"02:30\",\"arraytm\":\"04:55\",\"volume\":\"14000\",\"isend\":\"是\"},{\"conveyancename\":\"Y87971\",\"destzone\":\"021WG\",\"endtm\":\"01:20\",\"takeofftm\":\"02:30\",\"arraytm\":\"04:55\",\"volume\":\"14000\",\"isend\":\"是\"},{\"conveyancename\":\"Y87971\",\"destzone\":\"021WG\",\"endtm\":\"01:20\",\"takeofftm\":\"02:30\",\"arraytm\":\"04:55\",\"volume\":\"14000\",\"isend\":\"是\"},{\"conveyancename\":\"Y87971\",\"destzone\":\"021WG\",\"endtm\":\"01:20\",\"takeofftm\":\"02:30\",\"arraytm\":\"04:55\",\"volume\":\"14000\",\"isend\":\"是\"},{\"conveyancename\":\"Y87971\",\"destzone\":\"021WG\",\"endtm\":\"01:20\",\"takeofftm\":\"02:30\",\"arraytm\":\"04:55\",\"volume\":\"14000\",\"isend\":\"是\"},{\"conveyancename\":\"Y87971\",\"destzone\":\"021WG\",\"endtm\":\"01:20\",\"takeofftm\":\"02:30\",\"arraytm\":\"04:55\",\"volume\":\"14000\",\"isend\":\"是\"},{\"conveyancename\":\"Y87971\",\"destzone\":\"021WG\",\"endtm\":\"01:20\",\"takeofftm\":\"02:30\",\"arraytm\":\"04:55\",\"volume\":\"14000\",\"isend\":\"是\"},{\"conveyancename\":\"Y87971\",\"destzone\":\"021WG\",\"endtm\":\"01:20\",\"takeofftm\":\"02:30\",\"arraytm\":\"04:55\",\"volume\":\"14000\",\"isend\":\"是\"},{\"conveyancename\":\"Y87971\",\"destzone\":\"021WG\",\"endtm\":\"01:20\",\"takeofftm\":\"02:30\",\"arraytm\":\"04:55\",\"volume\":\"14000\",\"isend\":\"是\"},{\"conveyancename\":\"Y87971\",\"destzone\":\"021WG\",\"endtm\":\"01:20\",\"takeofftm\":\"02:30\",\"arraytm\":\"04:55\",\"volume\":\"14000\",\"isend\":\"是\"},{\"conveyancename\":\"Y87971\",\"destzone\":\"021WG\",\"endtm\":\"01:20\",\"takeofftm\":\"02:30\",\"arraytm\":\"04:55\",\"volume\":\"14000\",\"isend\":\"是\"},{\"conveyancename\":\"Y87971\",\"destzone\":\"021WG\",\"endtm\":\"01:20\",\"takeofftm\":\"02:30\",\"arraytm\":\"04:55\",\"volume\":\"14000\",\"isend\":\"是\"},{\"conveyancename\":\"Y87971\",\"destzone\":\"021WG\",\"endtm\":\"01:20\",\"takeofftm\":\"02:30\",\"arraytm\":\"04:55\",\"volume\":\"14000\",\"isend\":\"是\"},{\"conveyancename\":\"Y87971\",\"destzone\":\"021WG\",\"endtm\":\"01:20\",\"takeofftm\":\"02:30\",\"arraytm\":\"04:55\",\"volume\":\"14000\",\"isend\":\"是\"},{\"conveyancename\":\"Y87971\",\"destzone\":\"021WG\",\"endtm\":\"01:20\",\"takeofftm\":\"02:30\",\"arraytm\":\"04:55\",\"volume\":\"14000\",\"isend\":\"是\"},{\"conveyancename\":\"Y87971\",\"destzone\":\"021WG\",\"endtm\":\"01:20\",\"takeofftm\":\"02:30\",\"arraytm\":\"04:55\",\"volume\":\"14000\",\"isend\":\"是\"},{\"conveyancename\":\"Y87971\",\"destzone\":\"021WG\",\"endtm\":\"01:20\",\"takeofftm\":\"02:30\",\"arraytm\":\"04:55\",\"volume\":\"14000\",\"isend\":\"是\"},{\"conveyancename\":\"Y87971\",\"destzone\":\"021WG\",\"endtm\":\"01:20\",\"takeofftm\":\"02:30\",\"arraytm\":\"04:55\",\"volume\":\"14000\",\"isend\":\"是\"},{\"conveyancename\":\"Y87971\",\"destzone\":\"021WG\",\"endtm\":\"01:20\",\"takeofftm\":\"02:30\",\"arraytm\":\"04:55\",\"volume\":\"14000\",\"isend\":\"是\"},{\"conveyancename\":\"Y87971\",\"destzone\":\"021WG\",\"endtm\":\"01:20\",\"takeofftm\":\"02:30\",\"arraytm\":\"04:55\",\"volume\":\"14000\",\"isend\":\"是\"},{\"conveyancename\":\"Y87971\",\"destzone\":\"021WG\",\"endtm\":\"01:20\",\"takeofftm\":\"02:30\",\"arraytm\":\"04:55\",\"volume\":\"14000\",\"isend\":\"是\"},{\"conveyancename\":\"Y87971\",\"destzone\":\"021WG\",\"endtm\":\"01:20\",\"takeofftm\":\"02:30\",\"arraytm\":\"04:55\",\"volume\":\"14000\",\"isend\":\"是\"},{\"conveyancename\":\"Y87971\",\"destzone\":\"021WG\",\"endtm\":\"01:20\",\"takeofftm\":\"02:30\",\"arraytm\":\"04:55\",\"volume\":\"14000\",\"isend\":\"是\"},{\"conveyancename\":\"Y87971\",\"destzone\":\"021WG\",\"endtm\":\"01:20\",\"takeofftm\":\"02:30\",\"arraytm\":\"04:55\",\"volume\":\"14000\",\"isend\":\"是\"},{\"conveyancename\":\"Y87971\",\"destzone\":\"021WG\",\"endtm\":\"01:20\",\"takeofftm\":\"02:30\",\"arraytm\":\"04:55\",\"volume\":\"14000\",\"isend\":\"是\"},{\"conveyancename\":\"Y87971\",\"destzone\":\"021WG\",\"endtm\":\"01:20\",\"takeofftm\":\"02:30\",\"arraytm\":\"04:55\",\"volume\":\"14000\",\"isend\":\"是\"},{\"conveyancename\":\"Y87971\",\"destzone\":\"021WG\",\"endtm\":\"01:20\",\"takeofftm\":\"02:30\",\"arraytm\":\"04:55\",\"volume\":\"14000\",\"isend\":\"是\"},{\"conveyancename\":\"Y87971\",\"destzone\":\"021WG\",\"endtm\":\"01:20\",\"takeofftm\":\"02:30\",\"arraytm\":\"04:55\",\"volume\":\"14000\",\"isend\":\"是\"},{\"conveyancename\":\"Y87971\",\"destzone\":\"021WG\",\"endtm\":\"01:20\",\"takeofftm\":\"02:30\",\"arraytm\":\"04:55\",\"volume\":\"14000\",\"isend\":\"是\"},{\"conveyancename\":\"Y87971\",\"destzone\":\"021WG\",\"endtm\":\"01:20\",\"takeofftm\":\"02:30\",\"arraytm\":\"04:55\",\"volume\":\"14000\",\"isend\":\"是\"},{\"conveyancename\":\"Y87971\",\"destzone\":\"021WG\",\"endtm\":\"01:20\",\"takeofftm\":\"02:30\",\"arraytm\":\"04:55\",\"volume\":\"14000\",\"isend\":\"是\"},{\"conveyancename\":\"Y87971\",\"destzone\":\"021WG\",\"endtm\":\"01:20\",\"takeofftm\":\"02:30\",\"arraytm\":\"04:55\",\"volume\":\"14000\",\"isend\":\"是\"},{\"conveyancename\":\"Y87971\",\"destzone\":\"021WG\",\"endtm\":\"01:20\",\"takeofftm\":\"02:30\",\"arraytm\":\"04:55\",\"volume\":\"14000\",\"isend\":\"是\"},{\"conveyancename\":\"Y87971\",\"destzone\":\"021WG\",\"endtm\":\"01:20\",\"takeofftm\":\"02:30\",\"arraytm\":\"04:55\",\"volume\":\"14000\",\"isend\":\"是\"},{\"conveyancename\":\"Y87971\",\"destzone\":\"021WG\",\"endtm\":\"01:20\",\"takeofftm\":\"02:30\",\"arraytm\":\"04:55\",\"volume\":\"14000\",\"isend\":\"是\"},{\"conveyancename\":\"Y87971\",\"destzone\":\"021WG\",\"endtm\":\"01:20\",\"takeofftm\":\"02:30\",\"arraytm\":\"04:55\",\"volume\":\"14000\",\"isend\":\"是\"},{\"conveyancename\":\"O36869\",\"destzone\":\"510\",\"endtm\":\"02:50\",\"takeofftm\":\"03:30\",\"arraytm\":\"05:25\",\"volume\":\"28000\",\"isend\":\"否\"},{\"conveyancename\":\"O36869\",\"destzone\":\"510X\",\"endtm\":\"02:50\",\"takeofftm\":\"03:30\",\"arraytm\":\"05:25\",\"volume\":\"28000\",\"isend\":\"否\"},{\"conveyancename\":\"O36888\",\"destzone\":\"571\",\"endtm\":\"02:40\",\"takeofftm\":\"03:35\",\"arraytm\":\"05:10\",\"volume\":\"28000\",\"isend\":\"否\"},{\"conveyancename\":\"O36861\",\"destzone\":\"574\",\"endtm\":\"02:40\",\"takeofftm\":\"03:55\",\"arraytm\":\"05:35\",\"volume\":\"14000\",\"isend\":\"否\"},{\"conveyancename\":\"O36861\",\"destzone\":\"574R\",\"endtm\":\"02:40\",\"takeofftm\":\"03:55\",\"arraytm\":\"05:35\",\"volume\":\"14000\",\"isend\":\"否\"},{\"conveyancename\":\"O36861\",\"destzone\":\"574W\",\"endtm\":\"02:40\",\"takeofftm\":\"03:55\",\"arraytm\":\"05:35\",\"volume\":\"14000\",\"isend\":\"否\"},{\"conveyancename\":\"DZ6216\",\"destzone\":\"010\",\"endtm\":\"05:25\",\"takeofftm\":\"06:55\",\"arraytm\":\"09:55\",\"volume\":\"14000\",\"isend\":\"否\"},{\"conveyancename\":\"O36867\",\"destzone\":\"510\",\"endtm\":\"20:50\",\"takeofftm\":\"21:40\",\"arraytm\":\"23:35\",\"volume\":\"28000\",\"isend\":\"否\"},{\"conveyancename\":\"O36853\",\"destzone\":\"021\",\"endtm\":\"22:00\",\"takeofftm\":\"23:00\",\"arraytm\":\"00:55\",\"volume\":\"28000\",\"isend\":\"否\"},{\"conveyancename\":\"O36896\",\"destzone\":\"571\",\"endtm\":\"22:40\",\"takeofftm\":\"23:40\",\"arraytm\":\"01:15\",\"volume\":\"28000\",\"isend\":\"否\"},{\"conveyancename\":\"O36880\",\"destzone\":\"577\",\"endtm\":\"22:20\",\"takeofftm\":\"23:50\",\"arraytm\":\"01:15\",\"volume\":\"16000\",\"isend\":\"否\"},{\"conveyancename\":\"O36861\",\"destzone\":\"571R\",\"endtm\":\"\",\"takeofftm\":\"\",\"arraytm\":\"\",\"volume\":\"\",\"isend\":\"否\"},{\"conveyancename\":\"O36869\",\"destzone\":\"021WF\",\"endtm\":\"\",\"takeofftm\":\"\",\"arraytm\":\"\",\"volume\":\"\",\"isend\":\"否\"},{\"conveyancename\":\"O36869\",\"destzone\":\"512W\",\"endtm\":\"\",\"takeofftm\":\"\",\"arraytm\":\"\",\"volume\":\"\",\"isend\":\"否\"},{\"conveyancename\":\"O36869\",\"destzone\":\"025WA\",\"endtm\":\"\",\"takeofftm\":\"\",\"arraytm\":\"\",\"volume\":\"\",\"isend\":\"否\"},{\"conveyancename\":\"O36888-O36858\",\"destzone\":\"536X\",\"endtm\":\"\",\"takeofftm\":\"\",\"arraytm\":\"\",\"volume\":\"\",\"isend\":\"否\"},{\"conveyancename\":\"Y87971\",\"destzone\":\"021WG\",\"endtm\":\"\",\"takeofftm\":\"\",\"arraytm\":\"\",\"volume\":\"\",\"isend\":\"否\"},{\"conveyancename\":\"Y87971\",\"destzone\":\"021WG\",\"endtm\":\"\",\"takeofftm\":\"\",\"arraytm\":\"\",\"volume\":\"\",\"isend\":\"否\"},{\"conveyancename\":\"Y87971\",\"destzone\":\"021WG\",\"endtm\":\"\",\"takeofftm\":\"\",\"arraytm\":\"\",\"volume\":\"\",\"isend\":\"否\"},{\"conveyancename\":\"Y87971\",\"destzone\":\"021WG\",\"endtm\":\"\",\"takeofftm\":\"\",\"arraytm\":\"\",\"volume\":\"\",\"isend\":\"否\"},{\"conveyancename\":\"Y87971\",\"destzone\":\"021WG\",\"endtm\":\"\",\"takeofftm\":\"\",\"arraytm\":\"\",\"volume\":\"\",\"isend\":\"否\"},{\"conveyancename\":\"Y87971\",\"destzone\":\"021WG\",\"endtm\":\"\",\"takeofftm\":\"\",\"arraytm\":\"\",\"volume\":\"\",\"isend\":\"否\"},{\"conveyancename\":\"Y87971\",\"destzone\":\"021WG\",\"endtm\":\"\",\"takeofftm\":\"\",\"arraytm\":\"\",\"volume\":\"\",\"isend\":\"否\"},{\"conveyancename\":\"Y87971\",\"destzone\":\"021WG\",\"endtm\":\"\",\"takeofftm\":\"\",\"arraytm\":\"\",\"volume\":\"\",\"isend\":\"否\"},{\"conveyancename\":\"Y87971\",\"destzone\":\"021WG\",\"endtm\":\"\",\"takeofftm\":\"\",\"arraytm\":\"\",\"volume\":\"\",\"isend\":\"否\"},{\"conveyancename\":\"Y87971\",\"destzone\":\"021WG\",\"endtm\":\"\",\"takeofftm\":\"\",\"arraytm\":\"\",\"volume\":\"\",\"isend\":\"否\"},{\"conveyancename\":\"Y87971\",\"destzone\":\"021WG\",\"endtm\":\"\",\"takeofftm\":\"\",\"arraytm\":\"\",\"volume\":\"\",\"isend\":\"否\"},{\"conveyancename\":\"Y87971\",\"destzone\":\"021WG\",\"endtm\":\"\",\"takeofftm\":\"\",\"arraytm\":\"\",\"volume\":\"\",\"isend\":\"否\"},{\"conveyancename\":\"Y87971\",\"destzone\":\"021WG\",\"endtm\":\"\",\"takeofftm\":\"\",\"arraytm\":\"\",\"volume\":\"\",\"isend\":\"否\"},{\"conveyancename\":\"Y87971\",\"destzone\":\"021WG\",\"endtm\":\"\",\"takeofftm\":\"\",\"arraytm\":\"\",\"volume\":\"\",\"isend\":\"否\"},{\"conveyancename\":\"Y87971\",\"destzone\":\"021WG\",\"endtm\":\"\",\"takeofftm\":\"\",\"arraytm\":\"\",\"volume\":\"\",\"isend\":\"否\"},{\"conveyancename\":\"Y87971\",\"destzone\":\"021WG\",\"endtm\":\"\",\"takeofftm\":\"\",\"arraytm\":\"\",\"volume\":\"\",\"isend\":\"否\"},{\"conveyancename\":\"Y87971\",\"destzone\":\"021WG\",\"endtm\":\"\",\"takeofftm\":\"\",\"arraytm\":\"\",\"volume\":\"\",\"isend\":\"否\"},{\"conveyancename\":\"Y87971\",\"destzone\":\"021WG\",\"endtm\":\"\",\"takeofftm\":\"\",\"arraytm\":\"\",\"volume\":\"\",\"isend\":\"否\"},{\"conveyancename\":\"Y87971\",\"destzone\":\"021WG\",\"endtm\":\"\",\"takeofftm\":\"\",\"arraytm\":\"\",\"volume\":\"\",\"isend\":\"否\"}]},\"head\":{\"template\":\"2\",\"id\":\"1\",\"ip\":\"10.108.83.180\"}}";
			//httpUrl=ledserviceIp+"af中国";
			 System.out.println("jsion==="+httpUrl);
			 String jsionResult=HttpUtil.sendPost("http://127.0.0.1:8081/ws/servlet", "jsonstr="+httpUrl);
	}
}