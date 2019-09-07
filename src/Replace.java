import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Replace symbol & within author tags
 * <p>
 * This is a fix for newer dblp.xml versions
 *
 * @author ksemer
 */
public class Replace {
    private static final Logger _logger = Logger.getLogger(Replace.class.getName());

    public static void main(String[] args) {
        String path = "dblp.xml";

        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            FileWriter w = new FileWriter(path + "_");
            String line;

            while ((line = br.readLine()) != null) {

                if (line.contains("<author>") && line.contains("&")) {
                    _logger.log(Level.INFO, "Before: " + line);
                    line = line.replace("&", "and");
                    _logger.log(Level.INFO, "After: " + line);
                }
                w.write(line + "\n");
            }
            br.close();
            w.close();
        } catch (Exception e) {
            _logger.log(Level.SEVERE, e.getMessage());
        }
    }
}