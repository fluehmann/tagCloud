package tagcloud.webcrawler;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


/**
 * @deprecated - useless class
 * Class to represent a Webpage
 *
 */
public class webPage {

	final private String url;
	final private Document source;
	final private LocalDateTime dateCrawled;

	public webPage(String url, Document source) {
		this.url = url;
		this.source = source;
		dateCrawled = LocalDateTime.now();
		extractLinks();
	}
	
	/**
	 * will extract all Links out of a jsoup document that was passed as parameter
	 * returns a LinkedList of URLs
	 */
	public List<String> extractLinks() {
		final List<String> referringLinks = new LinkedList<String>();

		Elements links = source.select("a[href]");
		for (Element link : links) {
			String linkString = link.absUrl("href");
			if (linkString.startsWith("http")) {
				referringLinks.add(linkString);
			}
		}
		return referringLinks;
	}

}
