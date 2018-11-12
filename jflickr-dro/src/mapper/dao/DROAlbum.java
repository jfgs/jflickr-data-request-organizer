package mapper.dao;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;

import parameters.DROParamManager;

/**
 * "Data request organizer" album
 *
 */
public class DROAlbum {
	
	private final String albumId; 
	private final String albumTitle;
	private final Vector<DROPhoto> photos;
	
	public DROAlbum(final String albumId, final String albumTitle) {
		this.albumId = albumId;
		this.albumTitle = albumTitle;
		this.photos = new Vector<DROPhoto>();
	}
	
	public String getAlbumId() {
		return albumId;
	}
	
	public String getAlbumTitle() {
		return albumTitle;
	}
	
	public void add(DROPhoto p) {
		photos.add(p);
	}
	
	public Iterator<DROPhoto> getPhotos() {
		return photos.iterator();
	}
	
	public void renderAlbumToHTML(String filename) {
		DROParamManager param = new DROParamManager();
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(
				new File(param.getExportLocation()+filename)));
			
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
			
			// Album
			bw.write("\n");
			bw.write("<h2 id=\""+getAlbumId()+"\">"+getAlbumTitle()+"</h2>");
			bw.write("<ul>");
			
			// Photos
			Iterator<DROPhoto> pl = getPhotos();
			while (pl.hasNext()) {
				DROPhoto p = pl.next();
				
				bw.write("<li>");
				bw.write(
					"<a href=\"file://"+p.getPhotoLocation().getAbsolutePath()+"\">"
					+ "<img src=\""+p.getPhotoLocation().getAbsolutePath()
					+"\" title=\""+p.getPhotoId() + "\""
					+">"
					+"</a>"
					+ "<div>"
					+ "<h3>"+p.getName()+"</h3>"
					+ "<p><small>"+p.getDescription()+"</small></p>"
					+ "</div>");
				bw.write("</li>");
				bw.write("\n");
			}
			bw.write("</ul>");
			bw.write("\n");			
			
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
