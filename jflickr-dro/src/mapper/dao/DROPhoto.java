package mapper.dao;

import java.io.File;

/**
 * "Data request organizer" photo
 *
 */
public class DROPhoto {
	
	private final String photoId;
	private final File fileLocation;
	private final String name;
	private final String description;
		
	public DROPhoto(final String photoId, final File fileLocation, final String name, final String description) {
		this.photoId = photoId;
		this.fileLocation = fileLocation;
		this.name = name; 
		this.description = description;
	}
	
	public String getPhotoId() {
		return photoId;
	}
	
	public File getPhotoLocation() {
		return fileLocation;
	}
	
	public String getName() {
		return name; 
	}
	
	public String getDescription() {
		return description;
	}

}
