package renderer;

import java.io.BufferedWriter;
import java.io.IOException;

/**
 * 
 * Standard parts of output HTML file
 *
 */
public class DefaultHtmlTemplate {
	
	public static void writeHeaderStart(BufferedWriter bw) throws IOException {
		bw.write("<html>");
		bw.write("<head>");
		bw.write("<link rel=\"stylesheet\" href=\"../../conf/export.css\">");
		bw.write("</head>");
		bw.write("<body>");
	}
	
	public static void writeHeaderEnd(BufferedWriter bw) throws IOException {
		bw.write("</body>");
		bw.write("</html>");
	}

}
