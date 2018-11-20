package renderer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import mapper.dao.DROAlbum;
import mapper.dao.DROAlbumList;
import mapper.dao.DROPhoto;
import parameters.DROParamManager;

/**
 * 
 * HTML renderer
 *
 */
public class DROHtmlRenderer {
	
	public static void renderAlbum(final DROAlbum a, final String filename) {
		DROParamManager param = new DROParamManager();
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(
				new File(param.getExportLocation()+filename)));
			
			DefaultHtmlTemplate.writeHeaderStart(bw);
			
			// Album
			bw.write("\n");
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
			
			DefaultHtmlTemplate.writeHeaderEnd(bw);
			
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
	
	public static void renderAlbumList(DROAlbumList l) {
		DROParamManager param = new DROParamManager();
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(
				new File(param.getExportLocation()+"export.html")));
			
			DefaultHtmlTemplate.writeHeaderStart(bw);
			
			// Album list
			bw.write("<h1>Album list:</h1>");
			bw.write("<ol>");
			Iterator<DROAlbum> al = l.getAlbumbs().iterator();
			while (al.hasNext()) {
				DROAlbum a = al.next();
				bw.write("<li><a href=\""+a.getAlbumId()+".html\">"+a.getAlbumTitle()+"</a></li>");
			}
			bw.write("</ol>");
			
			// Albums, with separate page per album
			al = l.getAlbumbs().iterator();
			while (al.hasNext()) {
				DROAlbum a = al.next();
				renderAlbum(a, a.getAlbumId()+".html");
			}
			
			DefaultHtmlTemplate.writeHeaderEnd(bw);
			
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
