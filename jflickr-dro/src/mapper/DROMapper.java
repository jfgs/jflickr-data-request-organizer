package mapper;

import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import mapper.dao.DROAlbum;
import mapper.dao.DROAlbumList;
import mapper.dao.DROPhoto;
import parameters.DROParamManager;

/**
 * Mapper of photos from raw data files. 
 *
 */
public class DROMapper {
	
	private final DROParamManager params;
	
	public DROMapper() {		
		params = new DROParamManager();
		
		DROAlbumList al = mapAlbums();
		al.renderAllAlbumsToHTML();
		
		System.out.println("ok!");
		
	}
	
	/**
	 * Map albums and photos on file system. FIXME 
	 * 
	 * @return
	 */
	private DROAlbumList mapAlbums() {
		
		final DROAlbumList albums = new DROAlbumList();
		
		int mappedPhotos = 0;
		int mappedAlbums = 0;
		int allAlbums = 0; 
		
		final JSONParser parser = new JSONParser();
		
		final JSONObject albumsRawData = parseAlbumsRawData(parser);		
		
		final JSONArray allAlbumList = (JSONArray) albumsRawData.get("albums"); // FIXME
		
		allAlbums = allAlbumList.size();
		
		final Object[] allAlbumsArray = allAlbumList.toArray();
		
		sortAlbumsByCreationDate(allAlbumsArray);
		
		for (Object ao : allAlbumsArray) {
			
			final JSONObject o = ((JSONObject) ao);
			
			mappedAlbums++;
			consoleDebugNewAlbumLine(mappedAlbums, allAlbums);
			
			final DROAlbum album 
			    = new DROAlbum(
			        (String) o.get("id"), 
			        (String) o.get("title"));
			
			final JSONArray p = (JSONArray) o.get("photos");
			
			@SuppressWarnings("rawtypes")
			Iterator i = p.iterator();
			while(i.hasNext()) {
				
				final String photoId = (String) i.next();
				
				if (!"0".equals(photoId)) {
					
					final File[] photoFiles 
						= findFile(
							photoId, 
							new File(params.getImportLocation()));
					
					// check if photo file was found...
					if (photoFiles != null && photoFiles.length != 0) {
						
						for (File photoFile : photoFiles) {
							
							final String descFilename = "photo_"+photoId+".json";
							
							final File[] descFiles 
							    = findFile(
							    	descFilename, 
							        new File(params.getImportLocation()));
							
							// check if description file was found...
							if (descFiles != null && descFiles.length != 0) {
								
								final JSONObject descRawData 
								    = parseDescriptionRawData(
								    	parser, descFiles);								
														
								album.add(
								    new DROPhoto(
								    	photoId, 
								    	photoFile, 
								    	(String) descRawData.get("name"), 
								    	(String) descRawData.get("description")));
								
								mappedPhotos++;
								if(mappedPhotos%10 == 1) {
								    consoleDebugProgressStep();
								}
								
							} else {
								// ... description file not found
								consoleDebugFileNotFound(descFilename);
							}
							
						} 
						
					} else {
						// ... photo file not found
						consoleDebugFileNotFound(photoId);
					}
				}
				
			} // photos loop
			
			albums.add(album);
			consoleDebugEndOfLine();
			
        } // albums loop
		
		return albums;
	}

	private JSONObject parseDescriptionRawData(final JSONParser parser, File[] descFiles) {
		JSONObject descRawData = null;
		try {
			descRawData 
			    = (JSONObject) parser.parse(
					new FileReader(descFiles[0].getAbsolutePath()));
		} catch (IOException | ParseException e) {
			throw new RuntimeException(e);
		}
		return descRawData;
	}

	private JSONObject parseAlbumsRawData(final JSONParser parser) {
		JSONObject albumsRawData = null;
		try {
			File[] f = findFile(
				"albums.json",
				new File(params.getImportLocation()));
			if (f == null || f[0] == null || "".equals(f[0])) {
				throw new RuntimeException("Can't open albums.json!");
			}
			System.out.println(
				"Mapping raw data from file: "
				+f[0].getAbsolutePath());
			albumsRawData = (JSONObject) parser.parse(
					new FileReader(f[0].getAbsolutePath()));   					// FIXME
		} catch (IOException | ParseException e) {
			throw new RuntimeException(e);
		}
		return albumsRawData;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void sortAlbumsByCreationDate(Object[] allAlbumsArray) {
		Arrays.sort(allAlbumsArray, new Comparator() {
			
			@Override
			public int compare(Object o1, Object o2) {
				if (o1 instanceof JSONObject && o2 instanceof JSONObject) {
					return ((String) ((JSONObject) o1).get("created")).compareTo(
							(String) ((JSONObject) o2).get("created"));
				} else {
					return 0;
				}
			}
		});
	}

	private void consoleDebugNewAlbumLine(int mappedAlbums, int allAlbums) {
		System.out.print("Mapping album ("+mappedAlbums+"/"+allAlbums+") ");
	}

	private void consoleDebugProgressStep() {
		System.out.print("#");
	}

	private void consoleDebugEndOfLine() {
		System.out.println("");
	}

	private void consoleDebugFileNotFound(String photoId) {
		System.err.println("Photo "+photoId+" not found!");
	}
	
	/**
	 * Recursive file search
	 * 
	 * @param filename filename pattern
	 * @param directory root directory
	 * @return 
	 */
	private File[] findFile(final String filename, final File directory) {
		if ("0".equals(filename)) {
			return null; 
		}
		for (File fd : directory.listFiles()) {
			if (fd.isDirectory()) {
				File[] r = findFile(filename, fd);
				// First matching filename and exit
				if (r != null && r.length > 0) {
					return r; 
				}
			}
		}
		return directory.listFiles(new FilenameFilter() {
		    @Override
			public boolean accept(File dir, String name) {
		        return 
		        	// photo filename pattern *_id_*
		            name.contains("_"+filename+"_")
		            // photo filename pattern id_*
		            || name.startsWith(filename+"_")
		            // exact filename
		            || name.equals(filename);
		    }
		});
	}

}
