package com.deeplogictech.servlet;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;

import com.deeplogictech.net.HttpConnectionUtil;

public class ScrapServlet extends HttpServlet {

	@Override
	public void init(ServletConfig config) {

		try {

			super.init(config);
			System.out.println("Initialized");

		} catch (Exception e) {
			System.out.println("exception occured !!!!!!!" + e);
		}

	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		doPost(request, response);
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		final String htmlContent = getContent();
		final String extractedInfo = extractTitle(htmlContent);

		final List<String> extractedTitleList = extractName(extractedInfo);

		final List<String> extractedUrlList = extractUrl(extractedInfo);

		JSONArray finalResp = new JSONArray();

		for (int i = 0; i < extractedTitleList.size(); i++) {

			URL url = new URL("https://time.com" + extractedUrlList.get(i));
			Map map = new HashMap();
			map.put("title", extractedTitleList.get(i));
			map.put("link", url);
			finalResp.add(map);
		}

		System.out.println("Final Response:" + finalResp.toJSONString());
		response.setContentType("text/plain; charset=utf-8");
		response.getWriter().write(finalResp.toJSONString());
	}

	private String getContent() throws IOException {
		String url = "https://time.com";
		return new HttpConnectionUtil().getResponseForRequest(url);
	}

	private String extractTitle(String content) {

		StringBuffer response = new StringBuffer();
		final Pattern titleRegExp = Pattern
				.compile("<li class=" + '"' + "most-popular-feed__item" + '"' + ">(.*?)</li>", Pattern.DOTALL);
		final Matcher matcher = titleRegExp.matcher(content);
		int i = 1;

		while (matcher.find()) {
			response.append(matcher.group(i));
		}
		return response.toString();
	}

	private List<String> extractName(String content) {

		List<String> extractedTitleList = new ArrayList<>();
		final Pattern titleRegExp = Pattern
				.compile("<h3 class=" + '"' + "most-popular-feed__item-headline" + '"' + ">(.*?)</h3>", Pattern.DOTALL);
		final Matcher matcher = titleRegExp.matcher(content);
		int i = 1;

		while (matcher.find()) {
			extractedTitleList.add(matcher.group(i));
		}
		return extractedTitleList;
	}

	private List<String> extractUrl(String content) {

		List<String> extractedUrlList = new ArrayList<>();
		final Pattern titleRegExp = Pattern.compile("<a href=" + '"' + "(.*?)" + '"' + ">", Pattern.DOTALL);
		final Matcher matcher = titleRegExp.matcher(content);
		int i = 1;

		while (matcher.find()) {
			extractedUrlList.add(matcher.group(i));
		}
		return extractedUrlList;
	}
}
