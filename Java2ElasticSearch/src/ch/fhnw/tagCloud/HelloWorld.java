package ch.fhnw.tagCloud;

import org.elasticsearch.client.Client;
import org.elasticsearch.node.Node;
import static org.elasticsearch.node.NodeBuilder.*;


public class HelloWorld {

	public static void main(String[] args) {
		
		// Testcommit Simon
		System.out.println("Hello World!");
		
		
		
		
		// on startup
		Node node = nodeBuilder().clusterName("Java2ElasticSearchCluster").node();
		Client client = node.client();
		
		// on shutdown
		node.close();
		

	}

}
