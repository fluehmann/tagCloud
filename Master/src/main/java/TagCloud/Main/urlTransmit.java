package TagCloud.Main;

import java.io.Serializable;

public class urlTransmit implements Serializable{
	
	private String url;
	
	public urlTransmit() {
		super();
		//default constructor
	}
	
	public void setUrl (String url){
		this.url = url;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void startCrawl() {
		System.out.println("starts crawling from " +url);
	}

}

