package de.timmeey.libTimmeey.networking.communicationClient;

import java.util.concurrent.Future;

import de.timmeey.libTimmeey.networking.communicationServer.HTTPRequest;
import de.timmeey.libTimmeey.networking.communicationServer.HTTPResponse;

public interface HTTPRequestService {

	public <T extends HTTPResponse> Future<T> send(HTTPRequest<?> request,
			Class<T> clazz, int port);

}