package Crawler;

import java.io.IOException;
import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import entity.ProvinceEntity;
import hibernate.HibernateUtil;

public class KNTCrawler {

	/**
	 * Get provinces that have ad (province without ad will be removed)
	 * 
	 * @return Map<provinceUrl, cityName>
	 * @throws IOException
	 */
	public List<String> getProvinces() {
		List<String> provinces = new ArrayList<String>();

		String provinceBaseUrl = "https://kenhnhatro.com/cho-thue-phong-tro-nha-tro/";
		String htmlExtention = ".html";

		// * Connect to the website
		String url = "https://kenhnhatro.com";

		// * Find the form
		Document responseDoc = getDocument(url);
		Element formSearch = responseDoc.select("form#frmSearch").first();
		CrawlerUtils.checkElement("formSearch", formSearch);

		// * Find the province selector
		Element provinceSelector = formSearch.select("select#quicksearch_TinhThanh").first();
		CrawlerUtils.checkElement("provinceSelector", provinceSelector);
		// ** Get the urlFraction and city name
		Elements provinceEls = provinceSelector.select("option");
		for (Element element : provinceEls) {

			String provinceUrl = element.attr("value");

			// * Check to filter the default option and remove empty province (no ads)
			if (!provinceUrl.isEmpty()) {
				provinceUrl = provinceBaseUrl + provinceUrl + htmlExtention;
				responseDoc = getDocument(provinceUrl);
				Element notfoundElement = responseDoc.selectFirst("p.notfound");
				if (notfoundElement == null) {
					provinces.add(provinceUrl);
				}
			}
		}

		return provinces;
	}

	public List<String> getDetailUrl() {
		List<String> provinces = getProvinces();
		List<String> detailUrls = new ArrayList<String>();
		Document responseDoc = null;

		for (String provinceUrl : provinces) {
			// * Connect to the provinceUrl
			responseDoc = getDocument(provinceUrl);

			// * Find the ul element
			Elements liEls = responseDoc.select("li");
			for (Element element : liEls) {
				Element temp = element.selectFirst("div.arttitle > a");
				if (temp != null) {
					detailUrls.add(temp.selectFirst("[href]").attr("href"));
				}
			}
		}

		return detailUrls;
	}

	public void getDetail() throws SQLException {
		List<String> detailUrls = getDetailUrl();
//		java.sql.Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/prcproject", "root",
//				"123456");

		// ** Open connection to the detail page
		for (String detailUrl : detailUrls) {
			Document responseDoc = getDocument(detailUrl);
			// * Find city info
			String province = responseDoc.select("span.crust").get(2).selectFirst("span").text();
			//TODO: TEST
			System.out.println("Province: " + province);
			// * Find district info
			String district = responseDoc.select("span.crust").get(3).selectFirst("span").text();
			//TODO: TEST
			System.out.println("District: " + district);
			// * Get content element
			Element contentElement = responseDoc.selectFirst("div#dnoidung_content");

//			// ** Get title
//			String title = contentElement.selectFirst("h1").text();
			// ** Get published
			String publishedDateStr = contentElement.selectFirst("div.dttimepost").selectFirst("span").text();
			System.out.println("Date: " + publishedDateStr);
//			// ** Get publisher
//			String publisher = contentElement.selectFirst("div.dtnguoidang").selectFirst("span").text();
//			// ** Get email
//			String email = contentElement.selectFirst("div.dtemail").selectFirst("a").text();
			// ** Get detailed address
			String detailedAddress = contentElement.select("div.dtline").get(2).selectFirst("span.green").text();
			//TODO: TEST
			System.out.println("Detailed address:" + detailedAddress);
			// ** Get price
			String priceStr = contentElement.selectFirst("div.dgia").selectFirst("span").text();
			if (priceStr.contains("vn")) {
				priceStr = priceStr.substring(0, priceStr.indexOf("v") - 1);
				System.out.println("price: " + priceStr);
			}
			// ** Get superficiality
			String superficialityStr = contentElement.selectFirst("div.ddientich").selectFirst("span").text();
			superficialityStr = superficialityStr.substring(0, superficialityStr.indexOf("m"));
			System.out.println("Superficiality: " + superficialityStr);
			// ** Get content
//			String content = contentElement.selectFirst("div.dnoidung").text();
			// ** Get image
//			Element imageElement = contentElement.selectFirst("div.dnoidung").selectFirst("center").selectFirst("img");
//			String imageUrl;
//			if (imageElement == null) {
//				imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/a/ac/No_image_available.svg/1024px-No_image_available.svg.png";
//			} else {
//				imageUrl = imageElement.attr("src");
//			}

//			String query = "{CALL insertprovince(?)}";
//			CallableStatement stm = connection.prepareCall(query);
//			stm.setString(1	, province);
//			ResultSet rs = stm.executeQuery();
//			if (rs.next()) {
//				System.out.println("latest inserted ID: " + rs.getInt("province_id"));
//			}
		}
	}

	/**
	 * Get DOM document from specific url
	 * 
	 * @param url
	 * @return Document
	 */
	public Document getDocument(String url) {
		Document responseDocument = null;
		try {
			Connection.Response resp;
			resp = Jsoup.connect(url).execute();
			responseDocument = resp.parse();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return responseDocument;
	}
}
