import y.base.*;
import y.view.*;
import java.util.*;

import y.algo.*;



/**
 * @author Twointy
 *
 */
public class enhanced_Analyser_Graph extends Analyser_Graph {

	private LinkedList fixed = new LinkedList();
	/**
	 * 
	 */
	public enhanced_Analyser_Graph() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public enhanced_Analyser_Graph(Graph2D arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public enhanced_Analyser_Graph(Graph2D arg0, YCursor arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}
	
	public enhanced_Analyser_Graph(Analyser_Graph graph, LinkedList l) {
		this(graph);
		this.set_relation(graph.get_relation());
		this.setNodeMap(graph.getNodeMap());
		this.setFixed(l);
	}

	/**
	 * @param r
	 */
	public enhanced_Analyser_Graph(Relation r) {
		super(r);
		
		// TODO Auto-generated constructor stub
	}

	public enhanced_Analyser_Graph(LinkedList l) {
		super();
		this.fixed=l;
	}
	
	public enhanced_Analyser_Graph(EdgeList l) {
		super();
		LinkedList toput = new LinkedList();
		toput.add(l);
		this.fixed=toput;
	}
	
	public enhanced_Analyser_Graph(Relation r, EdgeList l) {
		super(r);
		LinkedList toput = new LinkedList();
		toput.add(l);
		this.fixed = toput;
		
	}
	
	public void setFixed(LinkedList l) {
		this.fixed=l;
	}
	
	public LinkedList getFixed() {
		return fixed;
	}
	
	public boolean notAllowedToEliminate(Edge e) {
		
		Iterator edgelists = this.fixed.iterator();
		while(edgelists.hasNext()) {
			EdgeList akt = (EdgeList)edgelists.next();
			if(akt.contains(e)) {
				return true;
			}
		}
		return false;
	}
	
	public EdgeList enhancedEdgeEliminationAlgorithm(int m, EdgeList l) {
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
					if(!this.notAllowedToEliminate(connection)) {
						result.add(connection);
						this.removeEdge(connection);
						//this.get_relation().delete_entry(this.get_relation().getIndex((Element)this.getNodeMap().get(connection.source())), this.get_relation().getIndex((Element)this.getNodeMap().get(connection.target())));
						System.out.println("Kante von "+this.getNodeMap().get(connection.source()).toString()+" nach "+this.getNodeMap().get(connection.target()).toString());
						break;
					}
				}
			}
			if(connection!=null) {
				if(!this.notAllowedToEliminate(connection)) {
					break;
				}
			}
		}
		if(connection==null) {
			System.out.println("Keine weitere Kantenelimination möglich - Graph ist nun azyklisch:" + GraphChecker.isAcyclic(this));
			return null;
		}
		
		return this.enhancedEdgeEliminationAlgorithm(m+1, result);
	}
	
	public LinkedList enhancedEliminationAlgorithm_getAll(int i, int j, LinkedList l) {
		
		if(i==j) {
			return l;
		}
		enhanced_Analyser_Graph copy = new enhanced_Analyser_Graph(this.copy(), fixed);
		
		EdgeList toadd=new EdgeList();
		System.out.println("enhancedEliminationAlgorithmgetAll Durchlauf: "+i);
		toadd = this.enhancedEdgeEliminationAlgorithm(0, new EdgeList());
		if(toadd==null) {
			return l;
		}
		l.add(toadd);
		
		i++;
		l.addAll(copy.enhancedEliminationAlgorithm_getAll(i,j,l));
		return l;
	
	}
}
