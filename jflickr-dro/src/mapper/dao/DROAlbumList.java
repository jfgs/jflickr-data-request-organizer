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
	
	public Vector<DROAlbum> getAlbumbs() {
		return albums;
	}
}
