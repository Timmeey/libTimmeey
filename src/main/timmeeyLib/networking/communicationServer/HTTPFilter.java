package timmeeyLib.networking.communicationServer;

public interface HTTPFilter {

	public boolean doFilter(String path, HttpContext ctx);

}
