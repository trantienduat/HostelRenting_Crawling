package Crawler;

import org.jsoup.nodes.Element;

public class CrawlerUtils {
	public static void checkElement(String name, Element elem) {
	    if (elem == null) {
	        throw new RuntimeException("Unable to find " + name);
	    }
	}
}
