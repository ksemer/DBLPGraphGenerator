# DBLP_Graph_Generator
Java code for extracting dblp graph from http://dblp.uni-trier.de/xml/

Steps for generating dblp graph

1) Download http://dblp.uni-trier.de/xml/dblp.xml.gz
2) Place dblp.xml to DBLP_Graph_Generator
3) Run src/dblpXMLParser.java
4) Run src/create_dblp_graph.java

Output:
	dblp_graph > id \t id \t year
	dblp_authors_ids > id \t author_name
	dblp_authors_conf > id \t conference
