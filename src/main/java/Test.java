
import java.sql.SQLException;

import Crawler.KNTCrawler;

public class Test {

	public static void main(String[] args) {
		KNTCrawler kntCrawler = new KNTCrawler();
//		kntCrawler.getProvinces().forEach((k,v) -> System.out.println("UrlFraction: " + k + "; City Name: " + v));
//		Map<String, String> provinces;
		try {
			kntCrawler.getDetail();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}