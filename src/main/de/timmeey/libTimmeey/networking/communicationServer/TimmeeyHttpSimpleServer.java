package de.timmeey.libTimmeey.networking.communicationServer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.timmeey.libTimmeey.networking.NetSerializer;
import de.timmeey.libTimmeey.networking.communicationServer.HTTPResponse.ResponseCode;

public class TimmeeyHttpSimpleServer extends Thread {
	private static final Logger logger = LoggerFactory
			.getLogger(TimmeeyHttpSimpleServer.class);
	private final ServerSocket serverSocket;
	private final Map<String, HttpHandler> handlerList = new HashMap<String, HttpHandler>();
	private final LinkedList<HTTPFilter> filter = new LinkedList<HTTPFilter>();
	private final NetSerializer gson;

	public TimmeeyHttpSimpleServer(NetSerializer gson, ServerSocket server) {
		logger.debug("Create");
		this.gson = gson;
		logger.debug("Setting serverSocket for listening on {}:{}",
				server.getLocalSocketAddress(), server.getLocalPort());
		this.serverSocket = server;
	}

	public TimmeeyHttpSimpleServer startServer() {
		logger.debug("Starting Server");
		super.start();
		logger.info("Server Started");
		return this;
	}

	/**
	 * New Filter are appended at the end of the list. THe Filter chain goes
	 * down from first added -> last added If only one filter returnes false the
	 * request is aborted and send back with a error message
	 * 
	 * @param filter
	 *            the filter to user
	 * @return whether the filter lets the request pass
	 */
	public TimmeeyHttpSimpleServer registerFilter(HTTPFilter filter) {

		this.filter.addLast(filter);
		logger.info("Filter added");
		return this;
	}

	public TimmeeyHttpSimpleServer removeFilter(HTTPFilter filter) {
		this.filter.remove(filter);
		logger.info("Filter removed");
		return this;
	}

	public TimmeeyHttpSimpleServer registerHandler(String path,
			HttpHandler handler) {
		this.handlerList.put(path, handler);
		logger.info("Handler added for: {}", path);

		return this;
	}

	public void run() {
		while (true) {
			try {
				logger.debug("Waiting for Clients");
				Socket client = this.serverSocket.accept();
				logger.debug("Woho, we got a client: {} {}",
						client.getInetAddress(), client.getPort());
				handleClient(client);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private void handleClient(final Socket client) {

		Thread thread = new Thread() {

			@Override
			public void run() {
				try (BufferedReader bufRead = new BufferedReader(
						new InputStreamReader(client.getInputStream()));
						BufferedWriter bufWrite = new BufferedWriter(
								new OutputStreamWriter(client.getOutputStream()));) {

					client.setSoTimeout(1000 * 60 * 10);

					String line;
					logger.trace("Waiting for client {}  input",
							client.getInetAddress());
					while ((line = bufRead.readLine()) != null) {
						logger.trace("Got input. Inputline was: {}", line);
						HTTPMessage message = gson.fromJson(line,
								HTTPMessage.class);
						HttpContext ctx = new HttpContext(gson,
								message.getPayloadObject());
						HttpHandler handler = handlerList
								.get(message.getPath());
						logger.trace("Found handler {} for path {}", handler,
								message.getPath());
						if (handler != null) {
							// THERE HAPPENS THE MAGIC
							// Only calls the actual handler if every filter
							// said OK
							if (filterRequestShallPass(message.getPath(), ctx)) {
								logger.trace("Request passed filters, handling");
								handler.handle(ctx);
							} else {
								if (ctx.getResponseCode() == null) {
									logger.trace("Force setting the responsecode to 500");
									ctx.setResponseCode(ResponseCode.FAILURE);
								}
							}
							logger.trace("Responsecode: {}",
									ctx.getResponseCode());
							logger.trace("Writing response: {}",
									ctx.getResponse());
							bufWrite.write(ctx.getResponse() + "\n");
							bufWrite.flush();
							logger.trace("Wrote response");
						} else {
							logger.debug(
									"No handler for {} found. Discarding request and closing connection to client",
									message.getPath());
							client.close();
						}

					}

				} catch (IOException e) {
					logger.debug(
							"IOException {} while handling client {}, closing connection",
							e, client.getInetAddress());

					try {
						client.close();
					} catch (IOException e1) {
						logger.debug("IOException{} while closing socket", e);
					}
				}
			}
		};
		logger.debug("Created listening thread {} for client: {}",
				thread.getName(), client.getInetAddress());

		thread.start();
	}

	public void unregister(String path) {
		handlerList.remove(path);
		logger.info("Unregistered handler for {}", path);

	}

	private boolean filterRequestShallPass(String path, HttpContext ctx) {
		try {
			for (HTTPFilter httpFilter : filter) {
				if (!httpFilter.doFilter(path, ctx)) {
					logger.info(
							"Filter: {} prevented the request for {} from passing down",
							filter, path);
					return false;
				}
			}
			return true;
		} catch (Exception e) {
			logger.warn(
					"Exception {} while filtering for {}. abort request handling",
					e, path, e);

		}
		return false;
	}
}
