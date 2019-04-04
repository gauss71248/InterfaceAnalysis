import y.base.*;
import y.algo.*;
import y.util.*;

import java.io.FileWriter;
import java.util.*;

import y.io.*;
import y.view.*;



/**
 * @author gauss
 *
 */
public class Analyser_Graph extends Graph2D {
	
	private Relation relation;
	private NodeMap nodemap;
	
	public Analyser_Graph() {
		relation = new Relation();
	}

	/**
	 * @param arg0
	 */
	public Analyser_Graph(Graph2D arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public Analyser_Graph(Graph2D arg0, YCursor arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}
	
	public Analyser_Graph(Relation r) {
		relation=r;
		this.build_up_graph();
	}
	
	public Analyser_Graph(Relation r, Element e) {
		relation=r;
		this.build_up_graph();
		this.only_direct_neighbours_allowed(this.getNode(e));
	}
	
	public void set_relation(Relation r) {
		relation=r;
		this.clear();
		this.build_up_graph();
	}
	
	public Relation get_relation() {
		return relation;
	}
	
	public NodeMap getNodeMap() {
		return nodemap;
	}
	
	public void setNodeMap(NodeMap nm) {
		nodemap=nm;
	}
	
	protected void build_up_graph() {
		this.clear();
		TreeMap elements = this.relation.get_elements();
		TreeMap entries = this.relation.get_entries();
		
		//Erstmal die Knoten erzeugen...
		TreeMap nodes =new TreeMap();
		Iterator keys = elements.keySet().iterator();
		while(keys.hasNext()) {
			nodes.put((Integer)keys.next(), this.createNode());
		}
		//Nun die Knoten labeln...
		NodeMap nodeMap = this.createNodeMap();
		
		keys = elements.keySet().iterator();
		while(keys.hasNext()) {
			Integer aktKey = (Integer)keys.next();
			Element akt = (Element)elements.get(aktKey);
			nodeMap.set((Node)nodes.get(aktKey), akt);
			
		}
		this.nodemap=nodeMap;
		//Nun noch die Verbindungen herstellen...
		keys = elements.keySet().iterator();
		
		while(keys.hasNext()) {
			Integer aktKey = (Integer)keys.next();
			Iterator connections = ((HashSet)entries.get(aktKey)).iterator();
			while(connections.hasNext()) {
				Integer akt = (Integer)connections.next();
				this.createEdge((Node)nodes.get(aktKey), (Node)nodes.get(akt));
			}
		}
	}
	
	
	protected NodeList belongsToStronglyConnectedComponent(Node n) {
		NodeList[] comps = GraphConnectivity.stronglyConnectedComponents(this);
		for(int i=0;i<comps.length;i++) {
			if(comps[i].contains(n)) {
				return comps[i];
			}
		}
		return null;
	}
	
	protected int outDegree_stronglyConnectedComponent(Node n) {
		NodeList strConComp = this.belongsToStronglyConnectedComponent(n);
		int result=0;
		for(EdgeCursor edges = n.outEdges();edges.ok();edges.next()) {
			if(strConComp.contains(edges.edge().target())) {
				result++;
			}
		}
		return result;
	}
	
	protected int inDegree_stronglyConnectedComponent(Node n) {
		NodeList strConComp = this.belongsToStronglyConnectedComponent(n);
		int result=0;
		for(EdgeCursor edges = n.inEdges();edges.ok();edges.next()) {
			if(strConComp.contains(edges.edge().source())) {
				result++;
			}
		}
		return result;
	}
	
	protected TreeMap inDegree_stronglyConnectedComponents(NodeList l) {
		TreeMap result = new TreeMap();
		Node[] nodes = l.toNodeArray();
		for(int i=0;i<nodes.length;i++) {
			NodeList akt;
			Integer aktDegree = new Integer(this.inDegree_stronglyConnectedComponent(nodes[i]));
			
			if(result.keySet().contains(aktDegree)) {
				akt = (NodeList)result.get(aktDegree);
				
			}else {
				akt = new NodeList();
			}
			akt.add(nodes[i]);
			result.put(aktDegree, akt);
		}
		return result;
	}
	
	protected TreeMap outDegree_stronglyConnectedComponents(NodeList l) {
		
		TreeMap result = new TreeMap();
		Node[] nodes = l.toNodeArray();
		for(int i=0;i<nodes.length;i++) {
			NodeList akt;
			Integer aktDegree = new Integer(this.outDegree_stronglyConnectedComponent(nodes[i]));
			if(result.keySet().contains(aktDegree)) {
				akt = (NodeList)result.get(aktDegree);
			}else {
				akt = new NodeList();
			}
			akt.add(nodes[i]);
			result.put(aktDegree, akt);
		}
		return result;
	}
	
		
	protected NodeList to_investigate() {
		NodeList[] comps = GraphConnectivity.stronglyConnectedComponents(this);
		for(int i=0;i<comps.length;i++) {
			if(comps[i].size()>1) {
				return comps[i];
			}
		}
		return null;
	}
	
	protected Edge getConnected(NodeList l1,NodeList l2) {
		Node[] nodes1=l1.toNodeArray();
		Node[] nodes2=l2.toNodeArray();
		for(int i=0;i<nodes1.length;i++) {
			for(int j=0;j<nodes2.length;j++) {
				Edge connection=nodes1[i].getEdgeTo(nodes2[j]);
				if(connection!=null) {
					return connection;
				}
			}
			
		}
		return null;
	}
	public EdgeList edgeEliminationAlgorithm(int m, EdgeList l) {
		EdgeList result = l;
		if(GraphChecker.isAcyclic(this)) {
			System.out.println("Graph ist azyklisch - Algorithmus fertig.");
			return result;
		}
		if(m==100) {
			this.generate_tgf("C:/temp2/test/Zwischenergebnis.tgf");
		}
		System.out.println("Durchlauf "+m);
		NodeList akt_investigation = this.to_investigate();
		
		NodeMap temp = this.createNodeMap();
		System.out.println("Anzahl starke Zusammenhangskomponenten gesamt: "+GraphConnectivity.stronglyConnectedComponents(this,temp));
		this.disposeNodeMap(temp);
		System.out.println("Aktuelle starke Zusammenhangskomponente verfügt über "+akt_investigation.size()+" Knoten.");
		
		TreeMap ins = this.inDegree_stronglyConnectedComponents(akt_investigation);
		
		
		TreeMap outs = this.outDegree_stronglyConnectedComponents(akt_investigation);
		
			
		Object[] keys_outs = outs.keySet().toArray();
		
		Object[] keys_ins = ins.keySet().toArray();
		
		
		
		Edge connection=null;
		for(int i=0;i<keys_outs.length;i++) {
			NodeList akt_outs = (NodeList)outs.get(keys_outs[i]);
			for(int j=keys_ins.length-1;j>=0;j--) {
				NodeList akt_ins = (NodeList)ins.get(keys_ins[j]);
				connection=this.getConnected(akt_outs, akt_ins);
				if(connection!=null) {
					result.add(connection);
					this.removeEdge(connection);
					this.relation.delete_entry(this.relation.getIndex((Element)this.nodemap.get(connection.source())), this.relation.getIndex((Element)this.nodemap.get(connection.target())));
					System.out.println("Kante von "+this.nodemap.get(connection.source()).toString()+" nach "+this.nodemap.get(connection.target()).toString());
					break;
				}
			}
			if(connection!=null) {
				break;
			}
		}
		if(connection==null) {
			System.out.println("Keine weitere Kantenelimination möglich - Graph ist nun azyklisch:" + GraphChecker.isAcyclic(this));
			return result;
		}
		
		return this.edgeEliminationAlgorithm(m+1, result);
		
		
	}
	
	public TreeMap groupElementMappings(EdgeList l) {
		TreeMap result = new TreeMap();
		for(EdgeCursor edges = l.edges();edges.ok();edges.next()) {
			Edge akt = edges.edge();
			Element origin = (Element)this.nodemap.get(akt.source());
			Element target = (Element)this.nodemap.get(akt.target());
			HashSet aktNodes;
			if(result.keySet().contains(origin)) {
				aktNodes = (HashSet)result.get(origin);
				
			}else {
				aktNodes = new HashSet();
			}
			aktNodes.add(target);
			result.put(origin, aktNodes);
		}
		return result;
				
	}
	

	
	public void generate_tgf(String path) {
		/**
		 * Der <code>String output</code> enthält am Ende die Zeichenkette, die in der Datei abgelegt werden soll.
		 */
		String output="";
		//Zunächst die Knoten definieren, also die Zeilennummern den IDNr. der Verfahren
		//zuordnen.
		Node[] nodes=this.getNodeArray();
		for(int i=0;i<nodes.length;i++)  {
			output+=i+" "+((Element)nodemap.get(nodes[i])).toString()+"\n";
		}
		output+="# \n";
				
		for(EdgeCursor ec=this.edges();ec.ok();ec.next()) {
			Edge akt = ec.edge();
			output+=akt.source().index()+" "+akt.target().index()+"\n";
		}
		
		
		
	
		try{
			FileWriter stream=new FileWriter(path);
			stream.write(output);
			stream.close();
		}
		catch(Exception e){
			System.out.println("Schreiben der Datei nicht möglich!");
		}
		
	}
	

	
	public void only_direct_neighbours_allowed(Node n) {
		NodeList neighbours = new NodeList();
		for(NodeCursor nc=n.neighbors();nc.ok();nc.next()) {
			neighbours.add(nc.node());
		}
		Node[] nodes = this.getNodeArray();
		for(int i = 0;i<nodes.length;i++) {
			if(!nodes[i].equals(n)&&!neighbours.contains(nodes[i])) {
				this.removeNode(nodes[i]);
			}
		}
	}
	
	public Node getNode(Element e) {
		Node[] nodes=this.getNodeArray();
		for(int i = 0;i<nodes.length;i++) {
			Element toTest = (Element)this.nodemap.get(nodes[i]);
			if(e.equals(toTest)) {
				return nodes[i];
			}
		}
		return null;
	}
	
	public Analyser_Graph copy() {
		GraphCopier gc = new GraphCopier(this.getGraphCopyFactory());
		Graph2D copy = (Graph2D)gc.copy(this);
		Relation rel = (Relation)this.relation.clone();
		Analyser_Graph result = new Analyser_Graph(copy);
		result.set_relation(rel);
		result.setNodeMap(nodemap);
		return result;
		
	}
	

}
