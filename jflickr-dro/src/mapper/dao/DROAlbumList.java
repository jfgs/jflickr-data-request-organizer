package mapper.dao;

import java.util.Vector;

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
	
	public Vector<DROAlbum> getAlbumbs() {
		return albums;
	}
}
