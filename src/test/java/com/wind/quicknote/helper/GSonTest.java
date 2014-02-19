package com.wind.quicknote.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Type;
import java.util.Date;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.wind.quicknote.model.NoteUserDto;

/**
 * https://sites.google.com/site/gson/gson-user-guide
 * 
 * online tools:
 * http://www.htmlescape.net/javaescape_tool.html
 * http://www.freeformatter.com/string-utilities.html
 * 
 */
public class GSonTest {

	private static final String EMAIL = "windew@bbs.xmu.edu.cn";
	
	@Test
	public void testGSonSerializer() {
		
		NoteUserDto dto = new NoteUserDto();
		dto.setLoginName("windew");
		dto.setEmail(EMAIL);
		dto.setPassword("dreamer");
		
		Gson gson = new Gson();
		String jsonStr = gson.toJson(dto);
		assertNotNull(jsonStr);
		assertEquals("{\"id\":0,\"loginName\":\"windew\",\"password\":\"dreamer\",\"email\":\"windew@bbs.xmu.edu.cn\"}", jsonStr);
		
		// Pretty Printing
		gson = new GsonBuilder().setPrettyPrinting().create();
		jsonStr = gson.toJson(dto);
		System.out.println("Pretty Printing: \n" + jsonStr);
		assertEquals("{\n  \"id\": 0,\n  \"loginName\": \"windew\",\n  \"password\": \"dreamer\",\n  \"email\": \"windew@bbs.xmu.edu.cn\"\n}", jsonStr);
		
	}

	@Test
	public void testGSonCustomizeDeserializer() {
		
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
						
						model.setEmail(obj.get("email").getAsString());
						System.out.println("Created: " + model.getCreated());
						assertEquals(EMAIL , model.getEmail());
						return model;
					}
				});
		
		// Gson gson = new GsonBuilder().setDateFormat(DateFormat.FULL, DateFormat.FULL).create();
		Gson gson = gb.create();

		String jsonStr = "{\"id\":528,\"loginName\":\"Bruce1\",\"firstName\":null,\"lastName\":null,\"role\":null,\"password\":\"dreamer\",\"email\":\"windew@bbs.xmu.edu.cn\",\"desc\":null,\"icon\":null,\"status\":null,\"created\":1392182995318,\"updated\":null}";
		NoteUserDto dtoR = gson.fromJson(jsonStr, NoteUserDto.class);
		assertEquals(EMAIL , dtoR.getEmail());
		assertNull(dtoR.getUpdated());
		assertNotNull(dtoR.getCreated());
		
	}

}
