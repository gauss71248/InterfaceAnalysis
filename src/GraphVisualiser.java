import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import y.base.*;
import y.algo.*;
import y.util.*;
import y.io.*;
import y.view.*;
import y.view.hierarchy.*;
import y.layout.*;
import y.layout.hierarchic.*;


/**
 * @author Twointy
 *
 */
public class GraphVisualiser extends ScrollPane {

	private Analyser_Graph graph;
	private Layouter layouter;
	/**
	 * 
	 */
	
	public GraphVisualiser() {
		super();
	}
	
	public GraphVisualiser(Analyser_Graph g) {
		super();
		this.graph=g;
		setLayouter(new HierarchicLayouter());
		this.setUpLayout();
	}
	/**
	 * @param graph the graph to set
	 */
	public void setGraph(Analyser_Graph graph) {
		this.graph = graph;
	}
	/**
	 * @return the graph
	 */
	public Analyser_Graph getGraph() {
		return graph;
	}

	
	/**
	 * @param layouter the layouter to set
	 */
	public void setLayouter(Layouter layouter) {
		this.layouter = layouter;
	}

	/**
	 * @return the layouter
	 */
	public Layouter getLayouter() {
		return layouter;
	}

	/**
	 * @param layouter the layouter to set
	 */
	public void setLayout(Layouter layouter) {
		this.layouter = layouter;
	}

	

	private void setUpLayout() {
		this.setSize(1000, 1000);
		
		
	     
	    EdgeRealizer er = graph.getDefaultEdgeRealizer();
	    er.setArrow(Arrow.STANDARD); 
	    ShapeNodeRealizer sr = new ShapeNodeRealizer();
	    //set the image
	    sr.setShapeType(ShapeNodeRealizer.RECT);
	    sr.setLocation(60,200);
	    sr.setSize(100,30);
	    sr.setFillColor(Color.cyan);
	    
	    //use it as default node realizer
	    graph.setDefaultNodeRealizer(sr);
	    
	    layouter.doLayout(graph);
	    
	    
		Graph2DView graphtoview = new Graph2DView(graph);
		
		graphtoview.fitContent();
		graphtoview.updateView();
		graphtoview.doLayout();
		this.add(graphtoview);
		
	}
	
}
