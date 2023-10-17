package com.Crawling.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;

@Service
public class CrawService {

	public ArrayList<HashMap<String,String>> getOliveRankItem() throws IOException  {
		System.out.println("SERVICE-getOliveRankItem");
		
		// DB 저장 
		
		String oliveUrl = "https://www.oliveyoung.co.kr/store/main/getBestList.do";
		// 랭킹 페이지 문서 리턴 <html> ~ </html>
		
		Document oliveRankDoc = Jsoup.connect(oliveUrl).get();
//		System.out.println(oliveRankDoc);
		// 필요한 정보가 있는 부분 선택 (css 선택자)
		Elements itemDiv =  oliveRankDoc.select("div.TabsConts");
//		System.out.println(itemDiv);
//		System.out.println(itemDiv.size());
		
		Elements items = itemDiv.get(0).select("ul.cate_prd_list li");
		//System.out.println(items.get(1));
		//System.out.println(items.size());
		// 데이터 수집
		// 브랜드 명, 상품이름, 상품가격, 브랜드 이미지, 배송비 

		ArrayList<HashMap<String,String>> prdList = new ArrayList<HashMap<String, String>>();
		
		
		for(Element item : items) {
			HashMap<String,String> prd_map = new HashMap<String,String>();
			
			String imgUrl = item.select("div.prd_info>a>img").attr("src");
		//	System.out.println("상품이미지: "+imgUrl);
			prd_map.put("prdImg", imgUrl);
			
			//System.out.println(item.select("div.prd_info>a>img"));
			
			String brandName = item.select("span.tx_brand").text();
			//System.out.println("브랜드명: "+brandName);
			prd_map.put("prdBrd", brandName);
			
			String prdName= item.select("p.tx_name").text();
		//	System.out.println("상품 이름: "+prdName);
//			String[] prdName_split = prdName.split("]");
//			System.out.println(prdName_split.length);
//			System.out.println(prdName_split[0]+"  "+prdName_split[1]);
			prd_map.put("prdName", prdName);
			
			String prdPrice = item.select("span.tx_cur > span.tx_num").text();
		//	System.out.println("상품 가격: "+prdPrice);
			prd_map.put("prdPrice", prdPrice);
			
			// 상세페이지 URL
			String detailURL = item.select("div.prd_info>a").attr("href");
			
			Document detailDoc = Jsoup.connect(detailURL).get();
			String reviewCount = detailDoc.select("#repReview > em").text();
			reviewCount=reviewCount.replace("(", "").replace(")", "").replace(",", "").replace("건", "");;
			//System.out.println("리뷰 수: "+reviewCount.replace("(", "").replace(")", "").replace(",", ""));
			prd_map.put("prdRev", reviewCount);
			prdList.add(prd_map);
			int idx = -1;
			for( int i = 0; i < prdList.size(); i++ ) {
				int temp_prdRev = Integer.parseInt(prdList.get(i).get("prdRev"));
			System.out.println("temp_prdRev: "+temp_prdRev);
			System.out.println("reviewCount: "+reviewCount); 
				if( Integer.parseInt(reviewCount) > temp_prdRev) {
					idx = i;
					break;
			}
		}
			if( idx > -1) {
				prdList.add(idx,prd_map);
			} else {
	}
			
	}

	    System.out.println(prdList);
	
		return prdList;

		/* 
		for(int i =0; i<items.size(); i++) {
			System.out.println(items.get(i).select("div.prd_info>a>img"));
			
			String imgUrl = items.get(i).select("div.prd_info>a>img").attr("src");
			System.out.println("상품이미지: "+imgUrl);
			
			String brandName = items.get(i).select("span.tx_brand").text();
			System.out.println("브랜드명: "+brandName);
			
			String prdName= items.get(i).select("p.tx_name").text();
			System.out.println("상품 이름: "+prdName);
//			String[] prdName_split = prdName.split("]");
//			System.out.println(prdName_split.length);
//			System.out.println(prdName_split[0]+"  "+prdName_split[1]);
			
			String prdPrice = items.get(i).select("span.tx_cur > span.tx_num").text();
			System.out.println("상품 가격: "+prdPrice);
		}
		 * */
		
		
		// 상품 리뷰 수 -> 상품 상세페이지
		
}

	public ArrayList<HashMap<String, String>> getPrdList_coupang(String searchText) throws IOException {
		System.out.println("SERVICE-getPrdList_coupang");
		String search_coupang_Url = "https://www.coupang.com/np/search";
		HashMap<String, String> paramList = new HashMap<String, String>();
		paramList.put("component", "");
		paramList.put("q", searchText);
		paramList.put("channel", "user");
		Document search_coupang_Doc = Jsoup.connect(search_coupang_Url).data(paramList).cookie("auth", "token").get();
		//System.out.println(search_coopang_Doc.select("li.search-product"));
		//System.out.println(search_coopang_Doc.select("li.search-product").size());

		Elements items = search_coupang_Doc.select("li.search-product");
		//System.out.println(items);
		//System.out.println(items.size());
		
		ArrayList<HashMap<String,String>> prdList_coupang = new ArrayList<HashMap<String,String>>();
		
		for(Element item : items) {
			// 상품이름, 상품가격, 상세페이지Url 
			HashMap<String, String> prdInfo = new HashMap<String,String>();
			
			String prdName = item.select("div.descriptions-inner>div.name").text();
			System.out.println("상품이름: "+prdName);
			prdInfo.put("prdName", prdName);
			String prdPrice = item.select("div.descriptions-inner>div.price-area strong.price-value").text();
			// > : 직계 자식,   "  ": 자식
			prdPrice = prdPrice.replace(",", "");
			System.out.println("상품가격: "+prdPrice);
			prdInfo.put("prdPrice", prdPrice);
			
			String detailUrl = item.select("a").attr("href");
			detailUrl = "https://www.coupang.com"+detailUrl;
			System.out.println("상세페이지 URL: "+detailUrl);
			prdInfo.put("detailUrl", detailUrl);
			
			prdInfo.put("prdSite", "coupang");
			
			prdList_coupang.add(prdInfo);
			// 상품 가격 순 정렬 (높은 순 -> 낮은 순)
			String sortOption = "PRICE_DESC";
			int idx = -1; // prdList_coopang에 추가 할 인덱스 번호
			for( int i = 0; i < prdList_coupang.size(); i++) {
				int prdPrice_int = Integer.parseInt(prdPrice);
				int listPrice = Integer.parseInt( prdList_coupang.get(i).get("prdPrice") );
				boolean sortType = false;
				switch (sortOption) {
				case "PRICE_DESC":
					sortType = prdPrice_int > listPrice;
					break;
				case "PRICE_ACS":
					sortType = prdPrice_int < listPrice;
					break;
				}
				if( sortType ) {
					idx = i;
					break;
				}
			}
			
			if(idx > -1) {
				prdList_coupang.add(idx,prdInfo);
			} else {
				prdList_coupang.add(prdInfo);
			}
		}
		
		return prdList_coupang;
	}

	public ArrayList<HashMap<String, String>> getPrdList_gmarket(String searchText) throws IOException {
		System.out.println("SERVICE-getPrdList_gmarket");
		//https://browse.gmarket.co.kr/search?keyword=keyboard
		HashMap<String, String> paramList = new HashMap<String,String>();
		
		paramList.put("keyword", searchText);
		String search_gmarket_Url = "https://browse.gmarket.co.kr/search";
		
		Document search_gmarket_Doc = Jsoup.connect(search_gmarket_Url).data(paramList).get();
		
		//System.out.println(search_gmarket_Doc);
	//	System.out.println(search_gmarket_Doc.select("div.box__component"));
	//	System.out.println(search_gmarket_Doc.select("div.box__component").size());
		
		Elements items = search_gmarket_Doc.select("div.box__item-container");
		
		
		return null;
	}

	public ArrayList<HashMap<String, String>> getPrdList_11st(String searchText) throws IOException {
		System.out.println("SERVICE-getPrdList_11st");

	// selenium
		ChromeOptions chopt = new ChromeOptions();
		chopt.setPageLoadStrategy(PageLoadStrategy.NORMAL);
		chopt.addArguments("headless");
		WebDriver dr = new ChromeDriver(chopt);
		 String connectUrl 
	       = "https://search.11st.co.kr/Search.tmall?kwd="+searchText;
		    dr.get(connectUrl);
		    
		 List<WebElement> items = dr.findElements(By.cssSelector("section.search_section>ul.c_listing>li>div.c_card"));
	
		 ArrayList<HashMap<String,String>> prdList_11st = new ArrayList<HashMap<String,String>>();
		 
		 for(WebElement item : items) {
			 HashMap<String, String> prdInfo = new HashMap<String, String>();
			 try {
				String prdName = item.findElement(By.cssSelector("div.c_prd_name strong")).getText();
			prdInfo.put("prdName", prdName);	
			String prdUrl = item.findElement(By.cssSelector("div.c_prd_name>a")).getAttribute("href");
			prdInfo.put("prdUrl", prdUrl);	
			String prdPrice = item.findElement(By.cssSelector("div.c_prd_price span.value")).getText();
			prdPrice = prdPrice.replace(",", "");
			prdInfo.put("prdPrice", prdPrice);	
			
			} catch (Exception e) {
				continue;
			}
	        	prdList_11st.add(prdInfo);
		 }
		 dr.quit();
		 return prdList_11st;
	}

	public ArrayList<HashMap<String, String>> getCrawlingTest() {
		System.out.println("SERVICE-getCrawlingTest");

		// selenium
			ChromeOptions chopt = new ChromeOptions();
			chopt.setPageLoadStrategy(PageLoadStrategy.NORMAL);
			chopt.addArguments("headless");
			WebDriver dr = new ChromeDriver(chopt);
			 String connectUrl 
		       = "https://www.lego.com/ko-kr/categories/new-sets-and-products?icmp=HP-SHQL-EG-NO-new-116";
			    dr.get(connectUrl);
			    
			 List<WebElement> items = dr.findElements(By.cssSelector("h3>a"));
		
			 ArrayList<HashMap<String,String>> crawlingList = new ArrayList<HashMap<String,String>>();
			 
			 for(WebElement item : items) {
				 HashMap<String, String> prdInfo = new HashMap<String, String>();
				 try {
					 String pd = item.findElement(By.cssSelector("span")).getText();
//				String prdName = item.findElement(By.cssSelector("div.c_prd_name strong")).getText();
//				prdInfo.put("prdName", prdName);	
//				String prdUrl = item.findElement(By.cssSelector("div.c_prd_name>a")).getAttribute("href");
//				prdInfo.put("prdUrl", prdUrl);	
//				String prdPrice = item.findElement(By.cssSelector("div.c_prd_price span.value")).getText();
//				prdPrice = prdPrice.replace(",", "");
//				prdInfo.put("prdPrice", prdPrice);	
					 prdInfo.put("pd", pd);
				} catch (Exception e) {
					continue;
				}
				 crawlingList.add(prdInfo);
			 }
			 dr.quit();
			 return crawlingList;
	}
		
	

}
