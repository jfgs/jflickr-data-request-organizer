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
		
		final JSONParser parser = new JSONParser();
		JSONObject rawData;
		
		try {
			rawData = (JSONObject) parser.parse(
					new FileReader(
							params.getImportLocation()+"albums.json")); // FIXME
		} catch (IOException | ParseException e) {
			throw new RuntimeException(e);
		}		
		
		JSONArray albumList = (JSONArray) rawData.get("albums"); // FIXME
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
			
			//System.out.print(o.get("title"));
			
			DROAlbum album = new DROAlbum((String) o.get("id"), (String) o.get("title"));
			
			//Timestamp t = new Timestamp(Long.valueOf((String) o.get("created")));
			//System.out.println(" - "+t.toLocalDateTime().toString());
			
			JSONArray p = (JSONArray) o.get("photos");
			Iterator i = p.iterator();
			while(i.hasNext()) {
				String photoId = (String) i.next();
				File[] fs = findPhoto(photoId, new File(params.getImportLocation()));
				if (fs != null && fs.length != 0) {
					for (File f : fs) {
						album.add(new DROPhoto(photoId, f));
						mappedPhotos++;
					}
				} else {
					System.err.println("Photo "+photoId+" not found!");
				}
				
				if (mappedPhotos > 333) {
					break;
				}
			}
			
			albums.add(album);
			
			if (mappedPhotos > 333) {
				break;
			}
        }
		
		return albums;
	}
	
	/**
	 * Recursive file search
	 * 
	 * @param photoId filename pattern *_<photoId>_o*
	 * @param directory root directory
	 * @return 
	 */
	private File[] findPhoto(final String photoId, final File directory) {
		if ("0".equals(photoId)) {
			return null; 
		}
		for (File fd : directory.listFiles()) {
			if (fd.isDirectory()) {
				File[] r = findPhoto(photoId, fd);
				// First matching filename and exit
				if (r != null && r.length > 0) {
					return r; 
				}
			}
		}
		return directory.listFiles(new FilenameFilter() {
		    @Override
			public boolean accept(File dir, String name) {
		        return name.contains("_"+photoId+"_o");
		    }
		});
	}

}
