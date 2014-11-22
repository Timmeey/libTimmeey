package timmeeyLib.networking.communicationServer;

import timmeeyLib.networking.HTTPRequest;
import timmeeyLib.networking.HTTPResponse;
import timmeeyLib.networking.NetSerializer;

public class HttpContext {
	private final NetSerializer gson;
	private String payload;
	private HTTPResponse response = new HTTPResponse();

	private int responseCode;

	public HttpContext(NetSerializer gson, String payloadObject) {
		this.payload = payloadObject;
		this.gson = gson;
	}

	public void setResponse(HTTPResponse response) {
		this.response = response;
	}

	public <T extends HTTPRequest<?>> T getPayload(Class<T> clazz) {
		return gson.fromJson(payload, clazz);
	}

	public String getResponse() {
		response.setResponseCode(getResponseCode());
		return gson.toJson(response);
	}

	public HttpContext setResponseCode(int responseCode) {
		this.responseCode = responseCode;
		return this;
	}

	public int getResponseCode() {
		return this.responseCode;
	}
}
