import java.io.*;
import java.net.URL;
import java.util.List;
//import java.util.LinkedList;
import java.util.ArrayList;

public class MapLoader {
	// MapLoader is a singleton class!
	private static final MapLoader mapLoader = new MapLoader();
	private FileReader fr = null;
	private BufferedReader br = null;
	private static final int SIZE = 32;
	int lineNumber = 0;
	int  columnNumber = 0;
	List<Location> thingsMap = new ArrayList<Location>();
	
	public void read(String fileName) {
		lineNumber = 0;    // important!
		URL url = MapLoader.class.getResource(fileName);
		try {
			br = new BufferedReader(new InputStreamReader(url.openStream())); // BufferedReader should read from URL
			
			while (true) {
				String line = br.readLine();
				if (line == null) {
					break;
				}
				char [] charArray = line.toCharArray();
				for (int i = 0; i < SIZE; i++) {
					if (charArray[i] == 'W') {
						thingsMap.add(new Location(i * 20, lineNumber * 20, "Wall"));
					} else if (charArray[i] == 'A') {
						thingsMap.add(new Location(i * 20, lineNumber * 20, "Block", 0));
						i++;
					} else if (charArray[i] == 'C') {
						thingsMap.add(new Location(i * 20, lineNumber * 20, "Block", 1));
						i++;
					} else if (charArray[i] == 'D') {
						thingsMap.add(new Location(i * 20, lineNumber * 20, "Block", 2));
						i++;
					} else if (charArray[i] == 'E') {
						thingsMap.add(new Location(i * 20, lineNumber * 20, "Block", 3));
						i++;
					} else if (charArray[i] == 'F') {
						thingsMap.add(new Location(i * 20, lineNumber * 20, "Block", 4));
						i++;
					} else if (charArray[i] == 'G') {
						thingsMap.add(new Location(i * 20, lineNumber * 20, "Block", 5));
						i++;
					} else if (charArray[i] == 'H') {
						thingsMap.add(new Location(i * 20, lineNumber * 20, "Block", 6));
						i++;
					} else if (charArray[i] == 'I') {
						thingsMap.add(new Location(i * 20, lineNumber * 20, "Block", 7));
						i++;
					} else if (charArray[i] == 'J') {
						thingsMap.add(new Location(i * 20, lineNumber * 20, "Block", 8));
						i++;
					} else if (charArray[i] == 'K') {
						thingsMap.add(new Location(i * 20, lineNumber * 20, "Block", 9));
						i++;
					} else if (charArray[i] == 'L') {
						thingsMap.add(new Location(i * 20, lineNumber * 20, "Block", 10));
						i++;
					}
				}
				
				lineNumber++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public List<Location> getThingsMap() {
		return thingsMap;
	}
	
	public static MapLoader getInstance() {
		return mapLoader;
	}
}
