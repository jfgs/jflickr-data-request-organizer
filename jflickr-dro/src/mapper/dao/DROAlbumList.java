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
			bw.write(
				"<style>\n" + 
				"body { \n" + 
				"    background-color: lightgray; \n" + 
				"}\n" + 
				"img {\n" + 
				"    width:auto; \n" + 
				"    max-width:500px; \n" + 
				"    height=auto; \n" + 
				"    max-height:300px; \n" +
				"    min-height:300px; \n" +
				"    padding:16px; \n" + 
				"    margin: 8px; \n" + 
				"    background-color: white; \n" + 
				"}\n" + 
				"ul {\n" + 
				"    column-count: 3;\n" + 
				"    column-gap: 8px;\n" + 
				"    list-style-type: none;\n" + 
				"    text-align: center; \n" + 
				"}\n" + 
				"</style>");
			bw.write("</head>");
			bw.write("<body>");
			
			// Album list
			bw.write("<h1>Album list:</h1>");
			bw.write("<ol>");
			Iterator<DROAlbum> al = albums.iterator();
			while (al.hasNext()) {
				DROAlbum a = al.next();
				bw.write("<li><a href=\"#"+a.getAlbumId()+"\">"+a.getAlbumTitle()+"</a></li>");
			}
			bw.write("</ol>");
			
			// Albums
			bw.write("<h1>Albums:</h1>");
			al = albums.iterator();
			while (al.hasNext()) {
				DROAlbum a = al.next();
				bw.write("<h2 id=\""+a.getAlbumId()+"\">"+a.getAlbumTitle()+"</h2>");
				bw.write("<ul>");
				
				// Photos
				Iterator<DROPhoto> pl = a.getPhotos();
				while (pl.hasNext()) {
					DROPhoto p = pl.next();
					
					bw.write("<li>");
					bw.write(
						"<a href=\"file://"+p.getPhotoLocation().getAbsolutePath()+"\">"
						+ "<img src=\""+p.getPhotoLocation().getAbsolutePath()
						+"\" title=\""+p.getPhotoId() + "\""
						+">"
						+"</a>");
					bw.write("</li>");
				}
				bw.write("</ul>");
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
