import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;


class Counter {
  private int c = 0;
  
  
  public void increase() {
    c++;
  }
  
  public int get() {
    return c;
  }
}

/**
 * Creates the DBLP graph author1 \t author 2 \t time
 *
 * @author ksemer
 */
public class CreateDBLPGraph {

    // Create graph from specific conferences
    private Set<String> CONFERENCES = new HashSet<>(Arrays.asList("ICDE", "VLDB", "EDBT",
            "SIGMOD Conference", "KDD", "WSDM", "WWW", "SIGIR", "CIKM", "ICDM"));
    
    private Set<String> FOUND = new HashSet<>();
    
    private final Integer START_YEAR = 2011;
    private final Integer END_YEAR = 2020;

    // is used to replace authors names with a unique id.
    private HashMap<String, Integer> allAuthors = new HashMap<>();
    private HashMap<Integer, Integer> first_paper_year = new HashMap<>();
    private HashMap<Integer, Counter> author_num_conf = new HashMap<>();
    private Set<Integer> authors_f = new HashSet<>();
    private FileWriter w_graph;
    private FileWriter w_authors_conf;

    private void run() throws IOException {
        String PATH_DBLP_INPUT = "dblp.txt";
        BufferedReader input = new BufferedReader(new FileReader(PATH_DBLP_INPUT));
        int year = 0, id = 0;
        String line, booktitle = null, title = null;
        List<String> authors = new ArrayList<>();
        String PATH_DBLP_GRAPH = "dblp_graph";
        w_graph = new FileWriter(PATH_DBLP_GRAPH, false);
        String PATH_DBLP_AUTHORS_CONFERENCES = "dblp_authors_conf";
        w_authors_conf = new FileWriter(PATH_DBLP_AUTHORS_CONFERENCES, false);

            
        while ((line = input.readLine()) != null) {
            if (line.contains("Author: ")) {

                if (booktitle != null) {
                  
                  for (String a : authors) {
                    int id_ = allAuthors.get(a);
                    if (first_paper_year.get(id_) > year) {
                      first_paper_year.put(id_, year);
                    }
                    author_num_conf.get(id_).increase();
                  }             
                    writeF(booktitle, title, year, authors);
                    authors.clear();
                    title = null;
                    booktitle = null;
                }

                String author = line.replace("Author: ", "");
                authors.add(author);

                // map author to a unique id
                if (!allAuthors.containsKey(author)) {
                    allAuthors.put(author, id);
                    first_paper_year.put(id, Integer.MAX_VALUE);
                    author_num_conf.put(id, new Counter());
                    id++;
                }
            } else if (line.contains("Title: ")) {
                title = line.replace("Title: ", "");
                title = title.replaceAll("[^A-Za-z0-9]", "");
            } else if (line.contains("Year: ") && !line.contains("Title:")) {
                year = Integer.parseInt(line.replace("Year: ", ""));
            } else if (line.contains("Booktitle: ")) {
                booktitle = line.replace("Booktitle: ", "");
            }
        }
        input.close();

        // for the last publication
        writeF(booktitle, title, year, authors);

        String PATH_DBLP_AUTHORS_MAP = "dblp_authors_ids";
        FileWriter w_authors = new FileWriter(PATH_DBLP_AUTHORS_MAP, false);
        for (Entry<String, Integer> entry : allAuthors.entrySet()) {
            int authorID = entry.getValue();

            // write only authors that published a paper in CONFERENCES
            if (authors_f.contains(authorID))
                w_authors.write(authorID + "\t" + entry.getKey() + "\n");
        }
        
        String PATH_DBLP_AUTHORS_PUBS = "dblp_authors_pubs";
        FileWriter w_authors_pubs = new FileWriter(PATH_DBLP_AUTHORS_PUBS, false);
        for (Entry<String, Integer> entry : allAuthors.entrySet()) {
            int authorID = entry.getValue();

            // write only authors that published a paper in CONFERENCES
            if (authors_f.contains(authorID))
              w_authors_pubs.write(authorID + "\t" + first_paper_year.get(authorID) + "\t" + author_num_conf.get(authorID).get() + "\n");
        }

        w_graph.close();
        w_authors.close();
        w_authors_conf.close();
        w_authors_pubs.close();

        System.out.println("DBLP graph is generated!");
        CONFERENCES.removeAll(FOUND);
        System.out.println("Conferences that could not be found: " + CONFERENCES);
    }

    /**
     * Write dblp graph and authors conferences to files
     */
    private void writeF(String booktitle, String title, int year, List<String> authors) throws IOException {
        // Used to generate graph from specific conferences
        if (title != null && (START_YEAR == null || year >= START_YEAR) && (END_YEAR == null || year <= END_YEAR) 
            && (CONFERENCES.isEmpty() || CONFERENCES.contains(booktitle))){// || (CONFERENCES.contains("VLDB") && booktitle.contains("@VLDB")))) {
            FOUND.add(booktitle);

            // Write the authors
            for (int i = 0; i < authors.size(); i++) {
                int authorA = allAuthors.get(authors.get(i));

                authors_f.add(authorA);
                w_authors_conf.write(authorA + "\t" + booktitle + "\n");

                for (int j = i + 1; j < authors.size(); j++) {
                    int authorB = allAuthors.get(authors.get(j));
                    authors_f.add(authorB);

                    // write graph edge
                    w_graph.write(authorA + "\t" + authorB + "\t" + year + "\n");
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new CreateDBLPGraph().run();
    }
}
