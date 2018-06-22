import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Replace symbol & within author tags
 * 
 * This is a fix for newer dblp.xml versions
 * 
 * @author ksemer
 *
 */
public class Replace {

	private static String path = "C:/Users/ksemer/Desktop/dblp.xml";

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(path));
		FileWriter w = new FileWriter(path + "_");
		String line = null;

		while ((line = br.readLine()) != null) {

			if (line.contains("<author>") && line.contains("&")) {
				System.out.println("Before: " + line);
				line = line.replace("&", "and");
				System.out.println("After: " + line);
			}
			w.write(line + "\n");
		}
		br.close();
		w.close();
	}
}