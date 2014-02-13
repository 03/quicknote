package com.wind.quicknote.system;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Date;

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
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.wind.quicknote.model.NoteUserDto;

public class RestClientTest {

	public static void testGet() {

		System.out.println("---------------starts-------");
		String WS_ADDR = "http://localhost:8000/qnote/services/rest/json/note/echo/mymsg";

		try {
			HttpClient c = new DefaultHttpClient();

			HttpGet g = new HttpGet(WS_ADDR);
			g.setHeader("Accept", "application/json");

			HttpParams params = new BasicHttpParams();
			params.setParameter("msg", "{\"msg\":\"" + "pop\"}");
			g.setParams(params);
			HttpResponse r = c.execute(g);

			BufferedReader rd = new BufferedReader(new InputStreamReader(r
					.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				System.out.println("Line: " + line);
			}
		} catch (ParseException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		}

		System.out.println("---------------ends-------");
	}

	public static void testPost() {
		System.out.println("---------------starts-------");

		String WS_ADDR = "http://localhost:8000/qnote/services/rest/json/note/adduser";

		try {
			HttpClient c = new DefaultHttpClient();

			// Post
			HttpPost p = new HttpPost(WS_ADDR);
			// p.addHeader("content-type", "application/x-www-form-urlencoded");

			NoteUserDto dto = new NoteUserDto();
			dto.setLoginName("BruceLee2");
			dto.setEmail("kk@kkk.com");
			dto.setPassword("kkk");

			Gson gson = new Gson();
			String jsonStr = gson.toJson(dto);

			p.setEntity(new StringEntity(jsonStr, ContentType
					.create("application/json")));
			HttpResponse r = c.execute(p);

			BufferedReader rd = new BufferedReader(new InputStreamReader(r
					.getEntity().getContent()));
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

		System.out.println("---------------ends-------");
	}

	public static void main(String[] args) throws JAXBException, SOAPException {

		// testGet();
		testPost();
		//tryGSonCustomizeDeserializer();
	}

	/**
	 * https://sites.google.com/site/gson/gson-user-guide
	 */
	private static void tryGSonCustomizeDeserializer() {

		NoteUserDto dto = new NoteUserDto();
		dto.setLoginName("Bruce1");
		dto.setEmail("kk@kkk.com");
		dto.setPassword("kkk");

		// Gson gson = new Gson();
		GsonBuilder gb = new GsonBuilder();
		gb.setPrettyPrinting();
		gb.registerTypeAdapter(NoteUserDto.class,
				new JsonDeserializer<NoteUserDto>() {
					public NoteUserDto deserialize(JsonElement json,
							Type typeOfT, JsonDeserializationContext context)
							throws JsonParseException {
						JsonObject obj = json.getAsJsonObject();
						NoteUserDto model = new NoteUserDto();
						model.setCreated(new Date(obj.get("created").getAsLong()));
						System.out.println("Created: " + model.getCreated());
						return model;
					}
				});
		
		// Gson gson = new GsonBuilder()
		// .setDateFormat(DateFormat.FULL, DateFormat.FULL).create();
		Gson gson = gb.create();

		String jsonStr = gson.toJson(dto);
		System.out.println("jsonStr: " + jsonStr);
		jsonStr = "{\"id\":528,\"loginName\":\"Bruce1\",\"firstName\":null,\"lastName\":null,\"role\":null,\"password\":\"kkk\",\"email\":\"kk@kkk.com\",\"desc\":null,\"icon\":null,\"status\":null,\"created\":1392182995318,\"updated\":null}";

		NoteUserDto dtoR = gson.fromJson(jsonStr, NoteUserDto.class);
		System.out.println(dtoR.getEmail());
	}

}
