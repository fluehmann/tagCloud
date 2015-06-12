package tagcloud.webcrawler;

import java.util.Set;

public interface PageParser {

	Set<String> parsePageByUrlAndGetLinks(String startURL) throws Exception;

}
