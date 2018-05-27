/**
* @author jiangyuzhao
* @readme 本java文件从pku官网作为第0层开始抓取URL，并且广搜2层。由于链接较多，且有链接到北大官方微博，故运行较慢。
* 程序逻辑就是从seed节点开始，读取网页内容，得到url，并加入队列，广搜2层即可。
*/
import java.io.*; 
import java.net.*;
import java.util.HashSet;
import java.util.*;
import java.util.regex.*;
import java.awt.*;
import java.awt.event.*;
import java.applet.*; 

public class Crawer {
	private static final int DEPTH = 2;
	private HashSet<MURL> urlHaveUsed = new HashSet<>();
	private LinkedList<MURL> urlNeedVisit = new LinkedList<>();
	
	public static void main(String[] args) {
		Crawer mCrawer = new Crawer();
		MURL seed = new MURL("http://www.pku.edu.cn/", 0);
		mCrawer.urlNeedVisit.add(seed);
		mCrawer.bfsForURL();
	}

	public static String getContentFromURL(String strurl){
		try{
			URL url = new URL(strurl);
			InputStream input = url.openStream();
			// InputStream stream = url.openStream();
			String content = readAll( input,"UTF-8" ); //常见的编码包括 GB2312, UTF-8
			// System.out.println(content);
			if ("http://www.pku.edu.cn/".equals(strurl)){
				System.out.println("done!");
			}
			return content;
		} catch ( MalformedURLException e) { 
			System.out.println("URL格式有错:" + strurl); 
		} catch (IOException ioe) {
			System.out.println("IO异常:" + strurl); 
		}
		return "";
	}

	public void bfsForURL(){
		int cnt = 0;
		while(!urlNeedVisit.isEmpty()){
			++cnt;
			MURL tmp = urlNeedVisit.poll();
			if (tmp != null){
				String text = getContentFromURL(tmp.getURL());
				// if ("http://www.pku.edu.cn/".equals(tmp.getURL())){
				// 	System.out.println("done!");
				// }
				getURL(text, tmp.getDepth());
				System.out.println(cnt);
			}
			// System.out.println(cnt);
		}
	}

	public void getURL(String text, int depth) {
		String patternString = 
				"\\s*(href)\\s*=\\s*(\"(http[^\"]*\")|(\'http[^\']*\')|(http[^\'\">\\s]+))";
		Pattern pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher( text );
		StringBuffer buffer = new StringBuffer();
		while (matcher.find()) {
			String str1 = (matcher.group(2).toString()).replaceAll("[\"'']", "");
			System.out.println(str1);
			if (depth + 1 <= DEPTH){
				MURL tmp = new MURL(str1, depth + 1);
				if (urlHaveUsed.add(tmp))
					urlNeedVisit.add(tmp);
			}
		}
		// if (depth == 0){
		// 	System.out.println("done!");
		// 	for (MURL tmp: urlNeedVisit){
		// 		System.out.println(tmp.getURL() + " " + tmp.getDepth());
		// 	}
		// }
	}

	public static String readAll( InputStream stream, String charcode ) throws IOException{
		BufferedReader reader = new BufferedReader(
			new InputStreamReader(stream, charcode)); 
		StringBuilder sb = new StringBuilder();
		String line; 
		while ((line = reader.readLine()) != null) { 
			// Pattern pattern = Pattern.compile(href, Pattern.CASE_INSENSITIVE);
			// Matcher matcher = pattern.matcher(line);
			// if(matcher.find()) {
			// 	href_num++;
			// }
			// pattern = Pattern.compile(scri, Pattern.CASE_INSENSITIVE);
			// matcher = pattern.matcher(line);
			// if(matcher.find()) {
			// 	script_num++;
			// }
			//练习使用正则表达式统计网页中超链接和script的数量
			sb.append(line+"\n"); 
		} 
		return sb.toString();
	}

	
}

class MURL{
	public String url;
	public int depth;
	
	public MURL(){
		this.url = "";
		this.depth = 0;
	}
		
	public MURL(String strurl, int depth){
		this.url = strurl;
		this.depth = depth;
	}
		
	public int getDepth(){
		return this.depth;
	}
		
	public void setDepth(int depth){
		this.depth = depth;
	}
		
	public String getURL(){
		return this.url;
	}
		
	public void setURL(String strurl){
		this.url = strurl;
	}
	
	@Override
	public boolean equals(Object obj){
		if (obj instanceof MURL){
			if (url == null && ((MURL)obj).url == null)
				return true;
			else if (url != null)
				return url.equals( ((MURL)obj).url );
			else
				return false;
		}
		return false;
	}
		
	@Override
	public int hashCode(){
		return url.hashCode();
	}
}