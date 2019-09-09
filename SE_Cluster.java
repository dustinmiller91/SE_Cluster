package workspace;

import java.util.*;
import java.lang.Math;
import java.io.*;
import java.text.DecimalFormat;

public class SE_Cluster {
  
  public LinkedList<Cluster> clusterSet;
  // I really don't want to instantiate this all the way up here, but it works for now.
  public LinkedList<GPS> allPoints = new LinkedList<GPS>();
  
  
  // An object that represents a SE GPS coordinate
  public class GPS {
    String title;
    double x;
    double y;
    double z;
    
    // Constructor: takes a SE formatted GPS string as a parameter
    GPS(String GPSstring) {
      // Trims and splits the string and assigns it's components to their respective variables.
      String[] splitArr = GPSstring.trim().split(":", 0);
      // Basic error for malformed strings
      if (splitArr.length < 4) {System.out.println("Error: Improperly formatted GPS");}
      else {
        title = splitArr[splitArr.length - 4];
        x = Double.parseDouble(splitArr[splitArr.length - 3]);
        y = Double.parseDouble(splitArr[splitArr.length - 2]);
        z = Double.parseDouble(splitArr[splitArr.length - 1]);
        // System.out.println("Reading " + title + ": " + x + ", " + y + ", " + z);
      }
    }
    
    // constructor taking XYZ coords and a title String
    GPS(int xCoord, int yCoord, int zCoord, String newTitle) {
      x = xCoord;
      y = yCoord;
      z = zCoord;
      title = newTitle;
    }
    
    // Calculates the distance between the point and another point taken as a parameter.
    public double euclideanDistance(GPS coord) {
      double xDiff = coord.x - x;
      double yDiff = coord.y - y;
      double zDiff = coord.z - z;
      return Math.sqrt(xDiff * xDiff + yDiff * yDiff + zDiff * zDiff);
    }
    
    // Overrides the toString method. Returns a SE-formated GPS string
    public String toString() {
      return "GPS:" + title + ":" + String.format("%.2f", x) + ":" + String.format("%.2f", y) + ":" +
        String.format("%.2f", z) + ":";
    }
  }
  
  
  // A group of points.
  public class Cluster {
    int clusterID;
    HashSet<GPS> pointSet;
    GPS centroid;
    
    // Add a coordinate to the cluster
    public void add(GPS coord) {
      pointSet.add(coord);
    }
    
    // Constructor, takes the ID# and a GPS coord as parameters
    Cluster(int ID, GPS coord) {
      clusterID = ID;
      pointSet = new HashSet<GPS>();
      pointSet.add(coord);
      centroid = coord;
    }
    
    // Calculates and returns the centroid of the cluster.
    public GPS getCentroid() {
      int pointCounter = 0;
      int xTotal = 0;
      int yTotal = 0;
      int zTotal = 0;
      
      // Iterate through all coordinates in the cluster and sum the XYZ coords
      Iterator<GPS> itr = pointSet.iterator();
      while (itr.hasNext()) {
        GPS currentCoord = (GPS)itr.next();
        xTotal += currentCoord.x;
        yTotal += currentCoord.y;
        zTotal += currentCoord.z;
        pointCounter++;
      }
      
      // Creates our new centroid (midpoint) and returns it.
      centroid = new GPS(xTotal / pointCounter, yTotal / pointCounter, zTotal / pointCounter, "Centroid " + 
                             clusterID);
      
      return centroid;
    }
    
    // Prints the distance to each point from the centroid.
    public void printCentroidDistances() {
      System.out.println("Centroid " + clusterID);
      String distanceString = "";
      for (GPS coord : pointSet) {
        distanceString += coord.euclideanDistance(centroid) + ", ";
      }
    }
  }
  
  
  // Reads a file line-by line, parsing them into GPS points and adding them to our HashSet allPoints.
  public void readPoints() {
    // Set up our file of coords to be read.
    File file = new File("GPS_Coords.txt");
    try {
      BufferedReader reader = new BufferedReader(new FileReader(file));
      
      // Read the file line-by-line, parsing each line into a GPS coord and add it to our set of all GPS points
      String s;
      while((s = reader.readLine()) != null) {
        allPoints.add(new GPS(s));
      }
      reader.close();
    }
    catch (IOException e) {System.out.println("Error: File not Found");}
  }
  
  
  // Method overload. Reads GPS points from a UI, parsing them into GPS points and adding them to our HashSet allPoints.
  public void readPoints(SE_Cluster_UI UI) {
    String[] pointArray = UI.getCoordList().split("\n");
    
    for (String coord : pointArray) {
      allPoints.add(new GPS(coord));
    }
  }
  
  // Reads a file line-by line, parsing them into GPS points and adding them to our HashSet allPoints.
  public void readPointsFromFile(String fileName, SE_Cluster_UI UI) {
    // Set up our file of coords to be read.
    File file = new File(fileName);
    try {
      BufferedReader reader = new BufferedReader(new FileReader(file));
      
      // Read the file line-by-line, parsing each line into a GPS coord and add it to our set of all GPS points
      String s;
      while((s = reader.readLine()) != null) {
        UI.coordList.append(s + "\n");
      }
      reader.close();
    }
    catch (IOException e) {System.out.println("Error: File not Found");}
  }
  
  // For use with the UI
  public void createClustersButton(SE_Cluster_UI UI) {
    
    for (String s : UI.coordList.getText().split("\n")) {
      allPoints.add(new GPS(s));
    }
    
    // For IDing our clusters as we create them.
    int clusterCount = 1;
    // Instantiate our new cluster set
    clusterSet = new LinkedList<Cluster>();
    
    for(GPS coord : allPoints) {
      boolean added = false;
      for (Cluster c : clusterSet) {
        boolean addToCluster = true;
        for (GPS clusterCoord : c.pointSet) {
          if (coord.euclideanDistance(clusterCoord) > 50000) {addToCluster = false;}
          break;
        }
        if (addToCluster) {
          c.add(coord); added = true;
          // UI.outputTextArea.append("Adding to cluster " + c.clusterID + "\n");
        }
      }
      if (!added) {
        clusterSet.add(new Cluster(clusterCount, coord));
        // UI.outputTextArea.append("New cluster: " + clusterCount + "\n");
        clusterCount++;
      }
    }
  }
  
  // Overload. Reads GPS from 
  public void createClusters() {
    // For IDing our clusters as we create them.
    int clusterCount = 1;
    // Instantiate our new cluster set
    clusterSet = new LinkedList<Cluster>();
    
    for(GPS coord : allPoints) {
      boolean added = false;
      for (Cluster c : clusterSet) {
        boolean addToCluster = true;
        for (GPS clusterCoord : c.pointSet) {
          if (coord.euclideanDistance(clusterCoord) > 40000) {addToCluster = false;}
          break;
        }
        if (addToCluster) {
          c.add(coord); added = true;
          // System.out.println("Adding to cluster " + c.clusterID);
        }
      }
      if (!added) {
        clusterSet.add(new Cluster(clusterCount, coord));
        // System.out.println("New cluster: " + clusterCount);
        clusterCount++;
      }
    }
  }
  
  public void createCentroids() {
    for (Cluster c : clusterSet) {
      System.out.println("Cluster " + c.clusterID + ", n = " + c.pointSet.size());
      System.out.println(c.getCentroid());
    }
  }
  
  // Overload for use with UI
  public void createCentroids(SE_Cluster_UI UI) {
    for (Cluster c : clusterSet) {
      UI.outputTextArea.append("Cluster " + c.clusterID + ", n = " + c.pointSet.size() + "\n");
      UI.outputTextArea.append(c.getCentroid().toString() + "\n");
    }
  }
    
  public void printSpread() {
    for (Cluster c : clusterSet) {
      System.out.println("Centroid " + c.clusterID);
      String spreadString = "";
      DecimalFormat df = new DecimalFormat();
      df.setMaximumFractionDigits(2);
      for (GPS coord : c.pointSet) {
        spreadString += (df.format(coord.euclideanDistance(c.centroid) / 1000) + "km, ");
      }
      System.out.println(spreadString);
    }
  }
  
  // This should return a value of 25.68Km (TESTED, WORKS)
  public void testEuclideanDistance() {
    GPS test1 = new GPS("GPS:Centroid 2:-1925086.0:3676865.0:650862.0:");
    GPS test2 = new GPS("GPS:Senpai'sHolyBeyblade #1:-1913944.64:3698235.2:642000.37:");
    System.out.println(test1.euclideanDistance(test2));
  }
  
  public static void main(String[] args) {
    SE_Cluster test = new SE_Cluster();
    test.readPoints();
    
    System.out.println();
    test.createClusters();
    
    System.out.println();
    test.createCentroids();
    
    System.out.println();
    test.printSpread();
    
    // System.out.println();
    // test.testEuclideanDistance();
  }
}