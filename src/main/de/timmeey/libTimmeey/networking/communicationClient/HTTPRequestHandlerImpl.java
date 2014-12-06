package de.timmeey.libTimmeey.networking.communicationClient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.timmeey.libTimmeey.networking.NetSerializer;
import de.timmeey.libTimmeey.networking.SocketFactory;
import de.timmeey.libTimmeey.networking.communicationServer.AnonBitMessage;
import de.timmeey.libTimmeey.networking.communicationServer.HTTPRequest;
import de.timmeey.libTimmeey.networking.communicationServer.HTTPResponse;
import de.timmeey.libTimmeey.networking.communicationServer.HTTPResponse.ResponseCode;
import de.timmeey.libTimmeey.pooling.ObjectPool;

public class HTTPRequestHandlerImpl implements HTTPRequestService {
	private static final Logger logger = LoggerFactory
			.getLogger(HTTPRequestHandlerImpl.class);
	protected final SocketFactory anonSocketFactory;
	protected final NetSerializer gson;
	private final ExecutorService execPool;
	private final ObjectPool<String, Socket> socketPool;

	public HTTPRequestHandlerImpl(SocketFactory socketFactory,
			NetSerializer gson, ExecutorService execService,
			ObjectPool<String, Socket> socketPool) {

		this.execPool = execService;
		this.gson = gson;
		this.anonSocketFactory = socketFactory;
		this.socketPool = socketPool;

	}

	public <T extends HTTPResponse> Future<T> send(
			final HTTPRequest<?> request, final Class<T> clazz, final int port) {
		Callable<T> call = new Callable<T>() {

			public T call() throws Exception {
				// Thread.sleep(4000);
				T result = deserializeResponse(
						doPost(request.getHost(), request.getPath(),
								serializeHTTPRequest(request), port), clazz);
				if (result.getResponseCode() != ResponseCode.SUCCESS) {
					logger.debug("Warning, response code was: "
							+ result.getResponseCode());
				}
				return result;
			}
		};
		return execPool.submit(call);
	}

	private String doPost(String host, String path, String data, int port)
			throws UnknownHostException, IOException {
		logger.trace("Posting " + data + "to " + host + path);
		Socket server = socketPool.borrow(host + ":" + port,
				new Callable<Socket>() {

					@Override
					public Socket call() throws Exception {
						return anonSocketFactory.getSocket(host, port);
					}
				});
		logger.trace(server.toString());

		BufferedWriter bufW = new BufferedWriter(new OutputStreamWriter(
				server.getOutputStream()));
		BufferedReader bufR = new BufferedReader(new InputStreamReader(
				server.getInputStream()));
		AnonBitMessage msg = new AnonBitMessage(path, data);

		bufW.write(gson.toJson(msg) + "\n");
		bufW.flush();
		String answer = bufR.readLine();
		socketPool.giveBack(server);
		return answer;

	}

	private String serializeHTTPRequest(HTTPRequest<?> req) {
		return gson.toJson(req);
	}

	private <T extends HTTPResponse> T deserializeResponse(String string,
			Class<T> clazz) {
		return gson.fromJson(string, clazz);
	}

}
