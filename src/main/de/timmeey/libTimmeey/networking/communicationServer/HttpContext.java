package de.timmeey.libTimmeey.networking.communicationServer;

import de.timmeey.libTimmeey.exceptions.checked.SerializerException;
import de.timmeey.libTimmeey.networking.communicationServer.HTTPResponse.ResponseCode;
import de.timmeey.libTimmeey.networking.NetSerializer;

public class HttpContext {
	private final NetSerializer gson;
	private String payload;
	private HTTPResponse response = new HTTPResponse();

	private ResponseCode responseCode;

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

	public String getResponse() throws SerializerException {
		response.setResponseCode(getResponseCode());
		return gson.toJson(response);
	}

	public HttpContext setResponseCode(ResponseCode responseCode) {
		this.responseCode = responseCode;
		return this;
	}

	public ResponseCode getResponseCode() {
		return this.responseCode;
	}
}
