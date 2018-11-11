package mapper.dao;

import java.util.Iterator;
import java.util.Vector;

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

}
