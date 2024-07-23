package com.apk.login.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.ayalait.response.ResponseResultado;
import com.ayalait.utils.Email;
import com.ayalait.utils.ErrorState;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class SendMail {

	public static String hostMail;
	public static String hostApp;

	@Autowired
	static RestTemplate restTemplate = new RestTemplate();

	 void cargarServer() throws IOException {
		Properties p = new Properties();

		try {
			URL url = this.getClass().getClassLoader().getResource("application.properties");
			if (url == null) {
				throw new IllegalArgumentException("application.properties" + " is not found 1");
			} else {
				InputStream propertiesStream = url.openStream();
				p.load(propertiesStream);
				propertiesStream.close();
				this.hostMail = p.getProperty("server.mail");
				this.hostApp = p.getProperty("server.app");
			}
		} catch (FileNotFoundException var3) {
			Logger.getLogger(SendMail.class.getName()).log(Level.SEVERE, (String) null, var3);
		}

	}

	public SendMail() {

		try {
			cargarServer();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static ResponseResultado enviarMailConfirmacion(Email email) {
		ResponseResultado responseResult = new ResponseResultado();
		try {
			HttpHeaders headers = new HttpHeaders();
			String url = hostMail + "/sendMailBody";
			//URI uri = new URI(url);

			HttpEntity<Email> requestEntity = new HttpEntity<>(email, headers);

			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

			if (response.getStatusCodeValue() == 200) {
				responseResult.setCode(response.getStatusCodeValue());
				responseResult.setStatus(true);
				responseResult.setResultado(response.getBody());

			}

		} catch (org.springframework.web.client.HttpServerErrorException e) {
			ErrorState data = new ErrorState();
			data.setCode(e.getStatusCode().value());
			data.setMenssage(e.getMessage());
			responseResult.setCode(data.getCode());
			responseResult.setError(data);

		} catch (org.springframework.web.client.HttpClientErrorException e) {

			JsonParser jsonParser = new JsonParser();
			int in = e.getLocalizedMessage().indexOf("{");
			int in2 = e.getLocalizedMessage().indexOf("}");
			String cadena = e.getMessage().substring(in, in2 + 1);
			JsonObject myJson = (JsonObject) jsonParser.parse(cadena);
			responseResult.setCode(myJson.get("code").getAsInt());
			ErrorState data = new ErrorState();
			data.setCode(myJson.get("code").getAsInt());
			data.setMenssage(myJson.get("menssage").getAsString());
			responseResult.setError(data);
		} 

		return responseResult;
	}

}
