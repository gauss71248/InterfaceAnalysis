import java.util.*;
import java.util.Iterator;
import y.algo.*;
import y.base.*;


public class run_analysis {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		
		Relation r = Parser.parse("C:/temp/Analyse_2010_02_02/Matrix.txt");
		
		String [][] matrix = Parser.getMatrix(Parser.readInput("C:/temp/Analyse_2010_02_02/Clustering.txt"));
		/****Beginn Tests
		 
		 */
		system_element e = new system_element("1260-00a","ZIBLog_2152_MatLenk_HOST_");
		Analyser_Graph graph = new Analyser_Graph(r,e);
		graph.generate_tgf("C:/temp3/partgraph.tgf");
		Analyser_Graph graphtrans = new Analyser_Graph(r);
		graphtrans.edgeEliminationAlgorithm(0, new EdgeList());
		graphtrans.only_direct_neighbours_allowed(graphtrans.getNode(e));
		graphtrans.generate_tgf("C:/temp3/partgraphtrans.tgf");
		
		
		
		
		
		/******************Beginn Analyse
		r=Parser.parse_clustering(r,"C:/temp/Analyse_2010_02_02/Clustering.txt" , 1);
		
		Analyser_Graph graph = new Analyser_Graph(r);
		graph.generate_tgf("C:/temp2/test/big_Clustering/Graph.tgf");
		System.out.println("Eigenschaften Graph:");
		System.out.println("zyklisch: "+GraphChecker.isCyclic(graph));
		System.out.println("bipartit: "+GraphChecker.isBipartite(graph));
		System.out.println("verbunden: "+GraphChecker.isConnected(graph));
		System.out.println("biconnected: "+GraphChecker.isBiconnected(graph));
		EdgeList edgesToRemove = graph.edgeEliminationAlgorithm(0, new EdgeList());
		
		
		 
		TreeMap edgesToRemoveGrouped =graph.groupElementMappings(edgesToRemove);
		System.out.println("Anzahl von Sorce-Knoten von zu entfernenden Kanten: "+edgesToRemoveGrouped.size());
		System.out.println("zu entfernende Kanten des edge-elimination Algorithmus: "+edgesToRemoveGrouped);
		graph.generate_tgf("C:/temp2/test/big_Clustering/Graph_azyklisch.tgf");
		
		EdgeList transitivereduction = new EdgeList();
		Transitivity.transitiveReduction(graph, transitivereduction);
		Transitivity.transitiveReduction(graph);
		graph.generate_tgf("C:/temp2/test/big_Clustering/Graph_azyklisch_transitiv_reduziert.tgf");
		TreeMap edgesToRemoveTransRed = graph.groupElementMappings(transitivereduction);
		System.out.println("Anzahl von Source-Knoten für transitive Reduktion zu entfernender Kanten: "+edgesToRemoveTransRed.size());
		System.out.println("Für transitive Reduktion zu eliminierende Kanten: "+edgesToRemoveTransRed);
		
		edgesToRemoveGrouped.putAll(edgesToRemoveTransRed);
		System.out.println("Insgesamt: Zahl von Source-Knoten von zu entfernenden Kanten: "+edgesToRemoveGrouped.size());
		System.out.println("Vollständige Liste: "+edgesToRemoveGrouped);
		
		//Relation transformed = graph.get_relation().transitive_hull().antisymmetric_reduction().transitive_reduction().reflexive_reduction();
		//transformed.generate_tgf("C:/temp2/test/mit_Clustering/Graph_azyklisch_trans.tgf");
		*********************************************************Ende Analyse/
		/********************************************************************************/
		
		
		/*
		enhanced_Analyser_Graph enhanced_graph = new enhanced_Analyser_Graph(r);
		System.out.println(enhanced_graph.enhancedEliminationAlgorithm_getAll(1,3,new LinkedList()));
		*/
		
		
		/*NodeList[] comps = GraphConnectivity.stronglyConnectedComponents(graph);
		System.out.println("Komponentenzahl: "+comps.length);
		for(int i=0;i<comps.length;i++) {
			NodeList akt=comps[i];
			Node[] nodes = akt.toNodeArray();
			System.out.println(i+". Komponente (Länge:"+nodes.length+"):");
			for(int j=0;j<nodes.length;j++) {
				System.out.print(graph.getNodeMap().get(nodes[j])+", ");
			}
			System.out.println("--------------");
		}*/
		/*Centrality.nodeBetweenness(graph, centrality, true, null);
		  Centrality.normalize(graph,centrality);
		for(NodeCursor nc=graph.nodes();nc.ok();nc.next()) {
			System.out.println(graph.getNodeMap().get(nc.node()).toString()+" "+centrality.getDouble(nc.node()));
		}
		Groups.edgeBetweennessClustering(graph, centrality, true, 0, graph.N(), null);
		for(NodeCursor nc=graph.nodes();nc.ok();nc.next()) {
			System.out.println(graph.getNodeMap().get(nc.node()).toString()+" "+centrality.getDouble(nc.node()));
		}
		
		Node ztbue=graph.getNodeArray()[25];
		double[] result=new double[graph.N()];
		double [] cost=new double[graph.E()];
		for(int i=0;i<cost.length;i++) {
			cost[i]=1;
		}
		ShortestPaths.bellmanFord(graph, ztbue, true, cost, result);
		for(int i=0;i<result.length;i++) {
			System.out.println(graph.getNodeMap().get(graph.getNodeArray()[i])+": "+result[i]);
		}
		
		*/
		
		
		//Relation r = Parser.parse("C:/temp2/Matrix.txt");
		/*
		r.generate_tgf("C:/temp2/Graph.tgf");
		Relation r_trans=r.transitive_hull();
		r_trans.generate_tgf("C:/temp2/test.tgf");
		Relation r_trans_detrans=r_trans.transitive_reduction();
		r_trans_detrans.generate_tgf("C:/temp2/test2.tgf");
		
		system_element e1=new system_element("4","4");
		system_element e2=new system_element("1","1");
		system_element e3=new system_element("2","2");
		system_element e4=new system_element("6","6");
		system_element e5=new system_element("3","3");
		system_element e6=new system_element("5","5");
		HashSet cluster1 = new HashSet();
		cluster1.add(e1);
		cluster1.add(e2);
		HashSet cluster2=new HashSet();
		cluster2.add(e3);
		cluster2.add(e4);
		Analyzer r_anal = new Analyzer(r);
		System.out.println("antisymmetrisch:" +r.is_antisymmetric());
		r_anal.get_order().generate_tgf("C:/temp2/Order.tgf");
		HashSet l =r_anal.edge_elimination_algorithm();
		System.out.println(l);
		r.delete_entries(l);
		System.out.println("antisymmetrisch:"+r.is_antisymmetric());
		System.out.println("Nächster Lauf!");
		
		Analyzer test = new Analyzer(r);
		l=test.edge_elimination_algorithm();
		System.out.println(l);
		r.delete_entries(l);
		System.out.println("antisymmetrisch:"+r.is_antisymmetric());
		
		
		r.generate_tgf("C:/temp2/test/Graph.tgf");
		r =r.transitive_hull();
		r.generate_tgf("C:/temp2/test/trans_hull.tgf");
		r=r.antisymmetric_reduction();
		r.generate_tgf("C:/temp2/test/Order.tgf");
		r_anal = new Analyzer(r);
		//System.out.println(r_anal.list_true_strong_connectivity_components());
		
		/*HashSet set1=new HashSet();
		HashSet toadd = new HashSet();
		toadd.add(new set_element(e1));
		
		toadd.add(new set_element(e2));
		set1.addAll(toadd);
		HashSet set2 = new HashSet();
		toadd=new HashSet();
		toadd.add(new set_element(e5));
		toadd.add(new set_element(e6));
		set2.addAll(toadd);
		
		System.out.println(r_anal.identify_connection(set1, set2));
		
		HashSet cluster = new HashSet();
		cluster.add(cluster1);
		cluster.add(cluster2);
		Relation clustered=r.clustering(cluster);
		
		
		clustered.generate_tgf("C:/temp2/cluster.tgf");
		*/
		
		/*
		
	
		Relation r = Parser.parse("C:/temp/Analyse_2009_10_29/Matrix.txt");
		r.generate_tgf("C:/temp/Analyse_2009_10_29/test/Graph.tgf");
		Relation r0=r.convert().antisymmetric_reduction();
		r0.generate_tgf("C:/temp/Analyse_2009_10_29/test/Graph0.tgf");
		Relation r1=r.convert().transitive_development(1);
		r1=r1.antisymmetric_reduction();
		r1.generate_tgf("C:/temp/Analyse_2009_10_29/test/Graph1.tgf");
		*/
		
	}

}
