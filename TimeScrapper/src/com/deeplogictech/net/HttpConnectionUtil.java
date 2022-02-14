package com.deeplogictech.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpConnectionUtil {

	public String getResponseForRequest(String urlToSendRequestStr) {

		System.out.println("Request to RGW ::" + urlToSendRequestStr);

		HttpURLConnection conn = null;
		String responseStr = null;
		try {
			conn = createConnection(urlToSendRequestStr);
			if (conn == null) {
				System.out.println("The HTTP Connection object is null.");
			} else {
				responseStr = sendRequestAndReceiveResponse(conn);
			}

		} catch (Exception e) {
			System.out.println("An exception occurred::" + e);
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
		// System.out.println("Response from RGW::" + responseStr);

		return responseStr;
	}

	/**
	 * This method creates the connection to the specified URL.
	 * 
	 * @param urlToSendRequestStr
	 *            The HTTP URL to connect to.
	 * @return Returns the HTTPURLConnection object.
	 */

	private static HttpURLConnection createConnection(String urlToSendRequestStr) {

		System.out.println("Inside createConnection method.");

		HttpURLConnection conn = null;

		try {
			URL url = new URL(urlToSendRequestStr);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(20000);
			conn.connect();
			return conn;
		} catch (MalformedURLException murle) {
			System.out.println("Malformed URL exception occurred::" + murle);

			return conn;
		} catch (IOException ioe) {
			System.out.println("An IO exception occurred during remote connection::" + ioe);

			return conn;
		} catch (Exception e) {
			System.out.println("An exception occurred during remote connection::" + e);
			return conn;
		}

	}

	/**
	 * Receives the response from the Regional Gateway for the request.
	 * 
	 * @param conn
	 *            The HttpURLConnection object pointing to the URL to which the
	 *            request was sent.
	 * @return The response received from the RGW for the request.
	 */

	private static String sendRequestAndReceiveResponse(HttpURLConnection conn) {

		StringBuffer response = new StringBuffer();
		try {

			String inputLine;
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			while ((inputLine = reader.readLine()) != null) {
				response.append(inputLine);
			}
		} catch (IOException ioe) {
			System.out.println("An IO exception occurred while communicating with remote server::" + ioe);

			return null;
		} catch (Exception e) {
			System.out.println("An Exception occurred while communicating with remote server::" + e);
			return null;
		}

		return response.toString();

	}

}
