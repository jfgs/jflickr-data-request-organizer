package mapper.dao;

import java.io.File;

/**
 * "Data request organizer" photo
 *
 */
public class DROPhoto {
	
	private final String photoId;
	private final File fileLocation;
	
	public DROPhoto(final String photoId, final File fileLocation) {
		this.photoId = photoId;
		this.fileLocation = fileLocation;
	}
	
	public String getPhotoId() {
		return photoId;
	}
	
	public File getPhotoLocation() {
		return fileLocation;
	}

}
