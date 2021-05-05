import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class IDS {
  
  private static void replace(String file, HashMap<String, String> ids) throws IOException {
    BufferedReader input = new BufferedReader(new FileReader(file));
    FileWriter w = new FileWriter(file + "_replaced", false);
    String line = null;
    while ((line = input.readLine()) != null) {
        String[] token = line.split("\t");
        String new_id = null;
        new_id = ids.get(token[0]);
        w.write(new_id);
        for (int i = 1; i < token.length; i++)
          w.write("\t" + token[i]);
        w.write("\n");
    }
    input.close();
    w.close();
  }
  
  public static void main(String[] args) throws IOException {
    HashMap<String, String> ids = new HashMap<>();
    BufferedReader input = new BufferedReader(new FileReader("dblp_authors_ids"));
    FileWriter w = new FileWriter("dblp_graph_replaced", false);
    String line = null;
    int count = 0;
    while ((line = input.readLine()) != null) {
    String[] token = line.split("\t");
    String author = token[0];
    ids.put(author, "" + count++);
    }
    input.close();
    input = new BufferedReader(new FileReader("dblp_graph"));
    while ((line = input.readLine()) != null) {
        String[] token = line.split("\t");
        String src = token[0];
        String tgt = token[1];
        if (ids.containsKey(src))
          src = ids.get(src);
        else {
          System.out.println("ERROR: " + src);
        }
        
        if (ids.containsKey(tgt))
          tgt = ids.get(tgt);
        else {
          System.out.println("ERROR: " + tgt);
        }
        w.write(src + "\t" + tgt + "\n");
    }
    input.close();
    w.close();
   
    replace("dblp_authors_ids", ids);
    replace("dblp_authors_conf", ids);
    replace("dblp_authors_pubs", ids);

    
  }
}