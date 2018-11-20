package mapper.dao;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;

import parameters.DROParamManager;

/**
 * "Data request organizer" album list
 *
 */
public class DROAlbumList {

	private final Vector<DROAlbum> albums;
	
	public DROAlbumList() {
		albums = new Vector<DROAlbum>();
	}
	
	public void add(DROAlbum album) {
		albums.add(album);
	}
	
	public void renderAllAlbumsToHTML() {
		DROParamManager param = new DROParamManager();
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(
				new File(param.getExportLocation()+"export.html")));
			
			bw.write("<html>");
			bw.write("<head>");
			bw.write("<link rel=\"stylesheet\" href=\"../../conf/export.css\">");
			bw.write("</head>");
			bw.write("<body>");
			
			// Album list
			bw.write("<h1>Album list:</h1>");
			bw.write("<ol>");
			Iterator<DROAlbum> al = albums.iterator();
			while (al.hasNext()) {
				DROAlbum a = al.next();
				bw.write("<li><a href=\""+a.getAlbumId()+".html\">"+a.getAlbumTitle()+"</a></li>");
			}
			bw.write("</ol>");
			
			// Albums, with separate page per album
			al = albums.iterator();
			while (al.hasNext()) {
				DROAlbum a = al.next();
				a.renderAlbumToHTML(a.getAlbumId()+".html");
			}
			
			bw.write("</body>");
			bw.write("</html>");
			
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				bw.close();
			} catch (IOException e) {
				// nop
			}
		}
		
	}
	
}
