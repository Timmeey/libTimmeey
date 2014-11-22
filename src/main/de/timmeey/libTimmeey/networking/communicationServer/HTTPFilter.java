package de.timmeey.libTimmeey.networking.communicationServer;

public interface HTTPFilter {

	public boolean doFilter(String path, HttpContext ctx);

}
