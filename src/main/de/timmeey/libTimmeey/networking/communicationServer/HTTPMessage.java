package de.timmeey.libTimmeey.networking.communicationServer;

public class HTTPMessage {
	String path;
	String payloadObject;

	public HTTPMessage(String path, String payload) {
		this.path = path;
		this.payloadObject = payload;
	}

	public String getPath() {
		return path;
	}

	public String getPayloadObject() {
		return payloadObject;
	}

}