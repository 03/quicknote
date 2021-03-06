package com.wind.quicknote.system;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.xml.bind.JAXBException;
import javax.xml.soap.SOAPException;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.zkoss.json.JSONObject;
import org.zkoss.json.parser.JSONParser;

import com.google.gson.Gson;
import com.wind.quicknote.model.NoteUserDto;

public class RestClientTest {
	
	private static final String URL_PREFIX = "http://localhost:8000/qnote/services/rest";

	public static void testGet() {

		String WS_ADDR = URL_PREFIX + "/json/note/echo/mymsg";

		try {
			HttpClient c = new DefaultHttpClient();

			HttpGet g = new HttpGet(WS_ADDR);
			g.setHeader("Accept", "application/json");

			HttpParams params = new BasicHttpParams();
			params.setParameter("msg", "{\"msg\":\"" + "pop\"}");
			g.setParams(params);
			HttpResponse r = c.execute(g);

			BufferedReader rd = new BufferedReader(
					new InputStreamReader(r.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				System.out.println("Line: " + line);
			}
		} catch (ParseException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		}

	}

	public static void testPost() {

		String WS_ADDR = URL_PREFIX + "/json/note/adduser";

		try {
			HttpClient c = new DefaultHttpClient();

			// Post
			HttpPost p = new HttpPost(WS_ADDR);
			p.addHeader("content-type", "application/x-www-form-urlencoded");

			NoteUserDto dto = new NoteUserDto();
			dto.setLoginName("BruceLee2");
			dto.setEmail("kk@kkk.com");
			dto.setPassword("kkk");

			Gson gson = new Gson();
			String jsonStr = gson.toJson(dto);

			p.setEntity(new StringEntity(jsonStr, ContentType.create("application/json")));
			HttpResponse r = c.execute(p);

			BufferedReader rd = new BufferedReader(
					new InputStreamReader(r.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				// Parse our JSON response
				JSONParser j = new JSONParser();
				JSONObject o = (JSONObject) j.parse(line);
				System.out.println(o);
				dto = gson.fromJson(line, NoteUserDto.class);
				System.out.println("new user id: " + dto.getId());
				System.out.println("new user created: " + dto.getCreated());
				break;
			}

		} catch (ParseException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		}

	}

	public static void main(String[] args) throws JAXBException, SOAPException {
		testGet();
		testPost();
	}

}
