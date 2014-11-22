package timmeeyLib.networking.communicationClient;

import java.util.concurrent.Future;

import timmeeyLib.networking.HTTPRequest;
import timmeeyLib.networking.HTTPResponse;

public interface HTTPRequestService {

	public <T extends HTTPResponse> Future<T> send(HTTPRequest<?> request,
			Class<T> clazz, int port);

}