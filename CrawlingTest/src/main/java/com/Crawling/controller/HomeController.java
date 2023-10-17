package com.Crawling.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.Crawling.service.CrawService;


/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home() {
		return "home";
	}
	
	@Autowired
	private CrawService csvc;

	
	@RequestMapping(value="/crawling")
	public ModelAndView crawling() throws IOException {
		ModelAndView mav = new ModelAndView();
		System.out.println("olive");
		
		ArrayList<HashMap<String,String>> testList =  csvc.getCrawlingTest();
		System.out.println(testList);
		mav.addObject("testList",testList);
		mav.setViewName("OliveBest");
		return mav;
	}
	
	
	@RequestMapping(value="/olive")
	public ModelAndView olive() throws IOException {
		ModelAndView mav = new ModelAndView();
		System.out.println("olive");
		
		ArrayList<HashMap<String,String>> prdList =  csvc.getOliveRankItem();
		
		mav.addObject("prdList",prdList);
		mav.setViewName("OliveBest");
		return mav;
	}
	
	@RequestMapping(value="/prdSearch")
	public ModelAndView prdSearch(String searchText) throws IOException {
		System.out.println("prdSearch");
		ModelAndView mav = new ModelAndView();
		System.out.println("searchText: "+searchText);
		
		//ArrayList<HashMap<String, String>> prdList_11st = csvc.getPrdList_11st(searchText);
		//mav.addObject("prdList_11st", prdList_11st);
		//ArrayList<HashMap<String, String>> prdList_coupang = csvc.getPrdList_coupang(searchText);
		//mav.addObject("prdList_coupang", prdList_coupang);
		ArrayList<HashMap<String, String>> prdList_gmarket = csvc.getPrdList_gmarket(searchText);
		mav.addObject("prdList_gmarket", prdList_gmarket);
		mav.setViewName("PrdSearchResult"); // PrdSearchResult.jsp
		
		return mav;
		
	}
}
