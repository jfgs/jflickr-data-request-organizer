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
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private DROAlbumList mapAlbums() {
		
		final DROAlbumList albums = new DROAlbumList();
		
		int mappedPhotos = 0;
		int mappedAlbums = 0;
		int allAlbums = 0; 
		
		final JSONParser parser = new JSONParser();
		JSONObject rawData;
		
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
			rawData = (JSONObject) parser.parse(
					new FileReader(f[0].getAbsolutePath()));   					// FIXME
		} catch (IOException | ParseException e) {
			throw new RuntimeException(e);
		}		
		
		JSONArray albumList = (JSONArray) rawData.get("albums"); 				// FIXME
		
		allAlbums = albumList.size();
		
		Object[] obj = albumList.toArray();
		Arrays.sort(obj, new Comparator() {
			
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
		
		for (Object jsonObject : obj) {
			
			JSONObject o = ((JSONObject) jsonObject);
			
			mappedAlbums++;
			System.out.print("Mapping album ("+mappedAlbums+"/"+allAlbums+") ");
			
			DROAlbum album = new DROAlbum((String) o.get("id"), (String) o.get("title"));
			
			JSONArray p = (JSONArray) o.get("photos");
			Iterator i = p.iterator();
			while(i.hasNext()) {
				String photoId = (String) i.next();
				
				if (!"0".equals(photoId)) {
					File[] fs = findFile(photoId, new File(params.getImportLocation()));
					if (fs != null && fs.length != 0) {
						for (File f : fs) {
							album.add(new DROPhoto(photoId, f));
							
							mappedPhotos++;
							if(mappedPhotos%10 == 1) {
							    System.out.print("#");
							}
						}
					} else {
						System.err.println("Photo "+photoId+" not found!");
					}
				}
				
				//if (mappedPhotos > 1111) {
				//	break;
				//}
			}
			
			albums.add(album);
			System.out.println("");
			
			//if (mappedPhotos > 1111) {
			//	break;
			//}
        }
		
		return albums;
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
