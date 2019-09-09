package workspace;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import javax.swing.filechooser.*;
import java.util.*;

public class SE_Cluster_UI {
  
  SE_Cluster data;
  
  JFrame frame;

  JPanel buttonPanelLeft;
  JPanel buttonPanelRight;
  JPanel buttonPanelFrame;
  JButton addCoordClipboard;
  JButton calculate;
  JButton importFromFile;
  JButton clearText;
  JButton clearOutput;
  
  JTextArea coordList;
  JTextArea outputTextArea;
  
  
  // A method to get the text of coordList
  public String getCoordList() {
    return coordList.getText();
  }
  
  
  SE_Cluster_UI() {
    
    data = new SE_Cluster();
    
    // -------========== FRAME/LAYOUT SETUP ==========--------
    
    frame = new JFrame("Beyblade Space Radar");
    frame.setLayout(new BorderLayout());
    
    buttonPanelLeft = new JPanel();
    buttonPanelRight = new JPanel();
    buttonPanelFrame = new JPanel();
    buttonPanelFrame.setLayout(new BorderLayout());
    
    
    addCoordClipboard = new JButton("Add GPS from Clipboard");
    importFromFile = new JButton("Import Coords from File");
    clearText = new JButton("Clear GPS");
    buttonPanelLeft.add(addCoordClipboard);
    buttonPanelLeft.add(importFromFile);
    buttonPanelLeft.add(clearText);
    
    calculate = new JButton("Calculate Centroids");
    clearOutput = new JButton("Clear Output");
    buttonPanelRight.add(calculate);
    buttonPanelRight.add(clearOutput);
    
    buttonPanelLeft.setPreferredSize(new Dimension(350, 100));
    buttonPanelRight.setPreferredSize(new Dimension(350, 100));
    
    buttonPanelFrame.add(buttonPanelLeft, BorderLayout.WEST);
    buttonPanelFrame.add(buttonPanelRight, BorderLayout.EAST);
    frame.add(buttonPanelFrame, BorderLayout.SOUTH);
    
    
    coordList = new JTextArea(32, 32);
    JScrollPane scroller_left = new JScrollPane(coordList);
    coordList.setEditable(false);
    
    outputTextArea = new JTextArea(32, 32);
    JScrollPane scroller_right = new JScrollPane(outputTextArea);
    outputTextArea.setEditable(false);
    
    frame.add(scroller_left, BorderLayout.WEST);
    frame.add(scroller_right, BorderLayout.EAST);
    
    frame.pack();
    frame.setResizable(false);
    frame.setVisible(true);
    
    // -------========== BUTTON FUNCITONALITY SETUP ===========-------
    
    SE_Cluster_UI thisUI = this;
    
    addCoordClipboard.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        
        try {
        String GPSdata = (String) Toolkit.getDefaultToolkit()
          .getSystemClipboard().getData(DataFlavor.stringFlavor) + "\n";
        coordList.append(GPSdata);
        }
        catch (Exception f) {
        }    
      }
    });
    
    clearText.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        coordList.setText("");
      }
    });
    
    clearOutput.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        outputTextArea.setText("");
      }
    });
    
    importFromFile.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        
        JFileChooser chooser = new JFileChooser(System.getProperty("user.dir"));
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text File", "txt");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
          // Do the thing
          String fileName = chooser.getSelectedFile().getAbsolutePath();
          System.out.println(fileName);
          data.readPointsFromFile(fileName, thisUI);
        }
        
      }
    });
    
    
    calculate.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        data.allPoints = new LinkedList<SE_Cluster.GPS>();
        data.clusterSet = new LinkedList<SE_Cluster.Cluster>(); 
        
        data.createClustersButton(thisUI);
        data.createCentroids(thisUI);
        outputTextArea.append("\n");
      }
    });
    
  }
  
  public static void main(String[] args) {
    SE_Cluster_UI runUI = new SE_Cluster_UI();
  }
  
}