package org.cytoscape.automorphismInspector.internal.view;

import java.awt.Component;
import java.util.Properties;
import java.util.Set;


import java.util.SortedSet;
import java.util.TreeSet;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.JOptionPane;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.application.swing.CytoPanel;
import org.cytoscape.application.swing.CytoPanelComponent;
import org.cytoscape.application.swing.CytoPanelName;
import org.cytoscape.application.swing.CytoPanelState;
import org.cytoscape.automorphismInspector.internal.logic.AutoThread;
import org.cytoscape.automorphismInspector.internal.results.ResultsGUI;

import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.events.NetworkAddedEvent;
import org.cytoscape.model.events.NetworkAddedListener;
import org.cytoscape.model.events.NetworkDestroyedEvent;
import org.cytoscape.model.events.NetworkDestroyedListener;

import org.cytoscape.service.util.CyServiceRegistrar;
import org.cytoscape.util.swing.OpenBrowser;

import org.cytoscape.view.model.CyNetworkView;
import org.jgrapht.GraphMapping;

/**
 * @author SrikanthB
 * GUI of the app, Control goes to logic from here
 */


public class AutoUI extends javax.swing.JPanel implements CytoPanelComponent, NetworkAddedListener, NetworkDestroyedListener {
    
    private CyServiceRegistrar cyRegistrar;
    private CyNetworkManager networkManager;
    private CyApplicationManager applicationManager;
    private OpenBrowser openBrowser;
    
    private AutoThread logicThread;
            
    public AutoUI(CyServiceRegistrar cyRegistrar) {
        initComponents();
        this.cyRegistrar = cyRegistrar;
        this.networkManager = cyRegistrar.getService(CyNetworkManager.class);
        this.applicationManager = cyRegistrar.getService(CyApplicationManager.class);
        this.openBrowser = cyRegistrar.getService(OpenBrowser.class);
        
        refreshNetworkList();
    }
    
    
    @Override
    public Component getComponent() {
        return this;
    }

    @Override
    public CytoPanelName getCytoPanelName() {
        return CytoPanelName.WEST;
    }

    @Override
    public String getTitle() {
        return "Automorphism";
    }

    @Override
    public Icon getIcon() {
        return null;
    }

    @Override
    public void handleEvent(NetworkAddedEvent nae) {
        CyNetwork net = nae.getNetwork();
        String title = net.getRow(net).get(CyNetwork.NAME, String.class);
        ((DefaultComboBoxModel)this.networkComboBox.getModel()).addElement(title);
    }

    @Override
    public void handleEvent(NetworkDestroyedEvent nde) {
        refreshNetworkList();
    }
    
    
    public ResultsGUI createResultsPanel(GraphMapping<CyNode, CyEdge> mapping, CyNetwork net1, CyNetwork net2) {
        ResultsGUI resultsPanel = new ResultsGUI(cyRegistrar);
        cyRegistrar.registerService(resultsPanel, CytoPanelComponent.class, new Properties());
        CytoPanel panelEast = cyRegistrar.getService(CySwingApplication .class).getCytoPanel(CytoPanelName.EAST);
        panelEast.setState(CytoPanelState.DOCK);
        panelEast.setSelectedIndex(panelEast.indexOfComponent(resultsPanel));
        //		visualizers.add(visualizer);
        resultsPanel.setEnabled(mapping, net1, net2);
        return resultsPanel;
    }
        
    public void startComputation(){
        
        startB.setEnabled(false);
        stopButton.setEnabled(true);
        statusBar.setIndeterminate(true);
        statusBar.setVisible(true);
        networkComboBox.setEnabled(false);
        
        statusLabel.setText("Finding symmetries ......");
    }
    
    public void endComputation(){
        
        statusBar.setIndeterminate(false);
        statusLabel.setText("<html>Completed! Check Results Panel <html>");
        startB.setEnabled(true);
        networkComboBox.setEnabled(true);
        
        stopButton.setEnabled(false);
       
    }
    
    public void calculatingresult(String msg){
        statusLabel.setText(msg);
    }
    
    public void stopcalculus(String message) {
        statusBar.setIndeterminate(false);
        if(message == null) {
            statusLabel.setText("Interrupted by user, click run to restart");
        }
        else {
            statusLabel.setText(message);
        }
    }
    
    private void refreshNetworkList() {
        final Set<CyNetwork> networks = networkManager.getNetworkSet();
        final SortedSet<String> networkNames = new TreeSet<String>();

        for (CyNetwork net : networks)
                networkNames.add(net.getRow(net).get("name", String.class));

        // Clear the comboBox
        networkComboBox.setModel(new DefaultComboBoxModel());

        for (String name : networkNames)
                networkComboBox.addItem(name);

        CyNetwork currNetwork = applicationManager.getCurrentNetwork();
        if (currNetwork != null) {
                String networkTitle = currNetwork.getRow(currNetwork).get("name", String.class);
                networkComboBox.setSelectedItem(networkTitle);			
        }
        
    }
    
    
    public CyNetwork getSelectedNetwork() {
        
        for (CyNetwork net : networkManager.getNetworkSet()) {
                String networkTitle = net.getRow(net).get("name", String.class);
                if (networkTitle.equals(networkComboBox.getSelectedItem()))
                        return net;
        }
        return null;
    }
    
    
    
    
    public void activate() {
        cyRegistrar.registerService(this, NetworkAddedListener.class, new Properties());
        cyRegistrar.registerService(this, NetworkDestroyedListener.class, new Properties());
        refreshNetworkList();
    }
    
    public void resetParams() {
        // TODO
    }
    
    public void deactivate() {
        cyRegistrar.unregisterService(this, NetworkAddedListener.class);
        cyRegistrar.unregisterService(this, NetworkDestroyedListener.class);
        cyRegistrar.unregisterService(this, CytoPanelComponent.class);
    }
    
    
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        mainPanel = new javax.swing.JPanel();
        headingLabel = new javax.swing.JLabel();
        mainUIPanel = new javax.swing.JPanel();
        networkLabel = new javax.swing.JLabel();
        networkComboBox = new javax.swing.JComboBox();
        startB = new javax.swing.JButton();
        statusPanel = new javax.swing.JPanel();
        statusBar = new javax.swing.JProgressBar();
        statusLabel = new javax.swing.JLabel();
        stopButton = new javax.swing.JButton();
        helpButton = new javax.swing.JButton();
        exitButton = new javax.swing.JButton();

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        mainPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        headingLabel.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        headingLabel.setForeground(new java.awt.Color(255, 0, 51));
        headingLabel.setText("CyAutomorphism");

        mainUIPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Select the network"));

        networkLabel.setText("Network");

        networkComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                networkComboBoxActionPerformed(evt);
            }
        });
        //networkComboBox.setEditable(false);

        startB.setText("Find automorphism group");
        startB.setToolTipText("");
        startB.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        startB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startBActionPerformed(evt);
            }
        });

        statusPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Status bar"));

        statusLabel.setText("status");

        stopButton.setBackground(new java.awt.Color(255, 102, 102));
        stopButton.setText("Stop");
        stopButton.setEnabled(false);
        stopButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(statusBar, javax.swing.GroupLayout.DEFAULT_SIZE, 297, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, statusPanelLayout.createSequentialGroup()
                        .addComponent(statusLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(stopButton)))
                .addContainerGap())
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addComponent(statusBar, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(statusLabel)
                    .addComponent(stopButton))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        helpButton.setFont(new java.awt.Font("Times New Roman", 1, 11)); // NOI18N
        helpButton.setForeground(new java.awt.Color(51, 51, 51));
        helpButton.setText("Help");
        helpButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                helpButtonActionPerformed(evt);
            }
        });

        exitButton.setFont(new java.awt.Font("Times New Roman", 1, 11)); // NOI18N
        exitButton.setForeground(new java.awt.Color(255, 0, 0));
        exitButton.setText("Exit");
        exitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout mainUIPanelLayout = new javax.swing.GroupLayout(mainUIPanel);
        mainUIPanel.setLayout(mainUIPanelLayout);
        mainUIPanelLayout.setHorizontalGroup(
            mainUIPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainUIPanelLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(mainUIPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(statusPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(startB, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(mainUIPanelLayout.createSequentialGroup()
                        .addComponent(networkLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(networkComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(mainUIPanelLayout.createSequentialGroup()
                        .addComponent(helpButton, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(exitButton, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(28, 28, 28))
        );
        mainUIPanelLayout.setVerticalGroup(
            mainUIPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainUIPanelLayout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(mainUIPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(networkLabel)
                    .addComponent(networkComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34)
                .addComponent(startB, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(53, 53, 53)
                .addComponent(statusPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(49, 49, 49)
                .addGroup(mainUIPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(helpButton)
                    .addComponent(exitButton))
                .addContainerGap(79, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(headingLabel)
                    .addComponent(mainUIPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(47, Short.MAX_VALUE))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(headingLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(mainUIPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(174, Short.MAX_VALUE))
        );

        jScrollPane1.setViewportView(mainPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 463, Short.MAX_VALUE)
                .addGap(11, 11, 11))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 562, Short.MAX_VALUE)
                .addGap(12, 12, 12))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void stopButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopButtonActionPerformed
        
        if(logicThread != null){
            logicThread.end();
        }
        //if(pewccapp.getPEWCClogic().isAlive()) {
        stopcalculus(null);
        networkComboBox.setEnabled(true);
        
        
        startB.setEnabled(true);
        stopButton.setEnabled(false);
    }//GEN-LAST:event_stopButtonActionPerformed

    private void startBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startBActionPerformed
        CyNetwork network = getSelectedNetwork();
        CyNetworkView networkview;

        if(network != null){
            networkview = applicationManager.getCurrentNetworkView();
            
            logicThread = new AutoThread(this, network, network);
            logicThread.start();

        } else{
            JOptionPane.showMessageDialog(null, "IMPORT a network first! ", "No Network found ", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_startBActionPerformed

    private void networkComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_networkComboBoxActionPerformed
        
        statusLabel.setText("status");
    }//GEN-LAST:event_networkComboBoxActionPerformed

    private void helpButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_helpButtonActionPerformed
        
        openBrowser.openURL("https://en.wikipedia.org/wiki/Automorphism");
    }//GEN-LAST:event_helpButtonActionPerformed

    private void exitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitButtonActionPerformed
        
        deactivate();
    }//GEN-LAST:event_exitButtonActionPerformed
 
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton exitButton;
    private javax.swing.JLabel headingLabel;
    private javax.swing.JButton helpButton;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JPanel mainUIPanel;
    protected javax.swing.JComboBox networkComboBox;
    private javax.swing.JLabel networkLabel;
    private javax.swing.JButton startB;
    private javax.swing.JProgressBar statusBar;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JPanel statusPanel;
    private javax.swing.JButton stopButton;
    // End of variables declaration//GEN-END:variables
 
    
    
    
    
         
}
