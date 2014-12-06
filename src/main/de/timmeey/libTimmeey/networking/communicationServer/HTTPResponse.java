package de.timmeey.libTimmeey.networking.communicationServer;

public class HTTPResponse {
	private ResponseCode responseCode;

	public enum ResponseCode {
		SUCCESS(200), FAILURE(500), AUTH_FAILURE(401);
		private int code;

		private ResponseCode(int code) {
			this.code = code;
		}
	}

	public ResponseCode getResponseCode() {
		return responseCode;
	}

	protected HTTPResponse setResponseCode(ResponseCode responseCode) {
		this.responseCode = responseCode;
		return this;
	}

}
