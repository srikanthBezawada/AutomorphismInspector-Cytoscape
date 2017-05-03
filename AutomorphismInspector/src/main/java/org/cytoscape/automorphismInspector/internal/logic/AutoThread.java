package org.cytoscape.automorphismInspector.internal.logic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import org.cytoscape.automorphismInspector.internal.results.ResultsGUI;
import org.cytoscape.automorphismInspector.internal.view.AutoUI;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.jgrapht.GraphMapping;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.alg.isomorphism.VF2SubgraphIsomorphismInspector;
import org.jgrapht.graph.SimpleGraph;

/**
 * @author SrikanthB
 *
 */

public class AutoThread extends Thread{
    public CyNetwork network1;
    public CyNetwork network2;
    
    private boolean stop;
    private int mappingcount=0;
    
    AutoUI panel;    
    
    public AutoThread(AutoUI menu, CyNetwork network1, CyNetwork network2){
        this.panel = menu;
        this.network1 = network1;
        this.network2 = network2;
    }
   
    @Override
    public void run(){
        stop = false;
        panel.startComputation();
        long startTime = System.currentTimeMillis();
    
        if(stop) {
            return;
        }
        
        UndirectedGraph<CyNode, CyEdge> g1 = new SimpleGraph<CyNode, CyEdge>(CyEdge.class);   
        List<CyNode> nodeList1 = network1.getNodeList();
        List<CyEdge> edgeList1 = network1.getEdgeList();
        for(CyNode n : nodeList1){
            g1.addVertex(n);
        }
        for(CyEdge e : edgeList1){
            if(e.getSource().equals(e.getTarget())){
                continue; // removing self-loops
            }
            g1.addEdge(e.getSource(), e.getTarget(),e);
            if(stop) {
                return;
            }
        }
        
        UndirectedGraph<CyNode, CyEdge> g2 = new SimpleGraph<CyNode, CyEdge>(CyEdge.class);
        List<CyNode> nodeList2 = network2.getNodeList();
        List<CyEdge> edgeList2 = network2.getEdgeList();
        for(CyNode n : nodeList2){
            g2.addVertex(n);
        }
        for(CyEdge e : edgeList2){
            if(e.getSource().equals(e.getTarget())){
                continue; // removing self-loops
            }
            g2.addEdge(e.getSource(), e.getTarget(), e);
            if(stop) {
                return;
            }
        }
        
        VF2SubgraphIsomorphismInspector<CyNode, CyEdge> vf2 = new VF2SubgraphIsomorphismInspector<CyNode, CyEdge>(g1, g2, null, null);
 
        System.out.println("------------------Graph Automorphism------------------");
        if (vf2.isomorphismExists()) {
            
            if(stop) {
                return;
            }
            
            panel.calculatingresult("Counting number of automorphic mappings..."); 
            //System.out.println("Graphs are automorphic.");
            
            mappingcount = 1;
            Iterator<GraphMapping<CyNode, CyEdge>> iter = vf2.getMappings();
            //            System.out.println("Priting an isomorphic mapping of the graphs");
            GraphMapping<CyNode, CyEdge> mapping = iter.next();
            ResultsGUI resultsPanel = panel.createResultsPanel(mapping, network1, network2);
            //            System.out.println(mapping);
            
            CountMappings countThread = new CountMappings(iter, network1, network2, resultsPanel);
            countThread.start();
            
            
            try {
                // take attribute from user
                //                String timeToWait = JOptionPane.showInputDialog(null, 
                //                        "Enter time to wait in secnds to compute total number of mappings");
                // waiting 5 seconds to return from the counting thread
                if(stop) {
                    countThread.stopalgo();
                try {
                    countThread.join();
                } catch (InterruptedException ex) {
                    Logger.getLogger(AutoThread.class.getName()).log(Level.SEVERE, null, ex);
                }
                return;
                }
                
                countThread.join(10000);
                if(countThread.isAlive()){
                    countThread.stopalgo();
                    resultsPanel.setResult("There are ATLEAST "+mappingcount+" number of automorphic mappings");
                    System.out.println("There are ATLEAST ["+mappingcount+"] number of automorphic mappings");
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(AutoThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            panel.endComputation();// "<html>Graphs are NOT isomorphic.<br><html>"
            //System.out.println("Graphs are NOT isomorphic.");
        }
        
        
        
        long endTime = System.currentTimeMillis();
        long difference = endTime - startTime;
        System.out.println("Execution time for CyAutomorphism " + difference +" milli seconds");
        panel.endComputation();
    }
    
    
    
    
    public void end() {
        stop = true;
    }
    
    
    
    
    private class CountMappings extends Thread{
        private volatile boolean stopthr = false;
        Iterator<GraphMapping<CyNode, CyEdge>> iter;
        ResultsGUI resultsPanel;
        CyNetwork net1;
        CyNetwork net2;
        
        public CountMappings(Iterator<GraphMapping<CyNode, CyEdge>> iter, CyNetwork net1, 
                CyNetwork net2, ResultsGUI resultsPanel) {
            this.iter = iter;
            this.resultsPanel = resultsPanel;
            this.net1 = net1;
            this.net2 = net2;
        }
        
        @Override
        public void run(){
            while(iter.hasNext()){
                mappingcount++;
                GraphMapping<CyNode, CyEdge> mapping = iter.next();
                //populate table
                DefaultTableModel dtm = (DefaultTableModel) resultsPanel.getTable().getModel();

                // add column dynamically into the table
                List<CyNode> net1NodeList = net1.getNodeList();
                List<ResultsGUI.cellData> data = new ArrayList<ResultsGUI.cellData>();
                for (CyNode n1: net1NodeList) {
                    CyNode n2 = mapping.getVertexCorrespondence(n1, true);
                    if(n2 != null){
                        data.add(new ResultsGUI.cellData(net1, net2, n1, n2));
                    }
                }
                dtm.addColumn("Mapping "+ mappingcount, data.toArray());
                
                if(stopthr == true)
                    return;
            }

            resultsPanel.setResult("Number of automorphisms found = "+mappingcount);
            //System.out.println("There are ["+mappingcount+"] number of isomorphic mappings");
            //System.out.println("------------------Graph Isomorphism------------------");
        }
            
        
        public void stopalgo(){
            stopthr = true;
        }
    }
}
