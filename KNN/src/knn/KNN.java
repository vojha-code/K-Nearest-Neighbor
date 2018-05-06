/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package knn;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author vojha
 */
public class KNN {

    //Global variables
    private final String filePath = System.getProperty("user.dir") + File.separator + "data" + File.separator;
    //private String data_file = "iris.csv";
    private String data_file = "irisrandom.csv";

    private double[][] inputs; //matrix of n x m where n is number of rows and m number of columns
    private double[] numOutputs; //matrix of n x m where n is number of rows and m number of columns
    private String[] strOutputs; //matrix of n x m where n is number of rows and m number of columns
    private boolean strClass;
    private double[][] trainInput;
    private double[][] testInput;
    private double[] numTrainOutputs;
    private double[] numTestOutputs;
    private String[] strTrainOutputs;
    private String[] strTestOutputs;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        KNN knn = new KNN();
        knn.start();
    }// main ends

    private void start() {
        //Read data and define matrices
        readData();
        splitData(0.77); // percentage;

        print_matrix(trainInput, strTrainOutputs, "Training data");
        print_matrix(testInput, strTestOutputs, "Test data");

        System.out.println("Testing K NN algorithm:\n");
        int k = 3;
        int[] checkPrediction = new int[testInput.length];
        int match = 0;
        int testSample = testInput.length;
        for (int i = 0; i < testSample; i++) {
            int[] neighbours = getKNearestNeighbors(trainInput, testInput[i], k);
            if (!strClass) {
                int[] dNClass = new int[neighbours.length];
                for (int j = 0; j < neighbours.length; j++) {
                    dNClass[j] = (int) numTrainOutputs[neighbours[j]];
                }
                int voteInt = MajorityVoteInt.majority(dNClass);
                if (voteInt == (int) numTestOutputs[i]) {
                    checkPrediction[i] = 1;
                    match++;
                } else {
                    checkPrediction[i] = 0;
                }
            } else {
                ArrayList<String> sNClass = new ArrayList<>();
                for (int j = 0; j < neighbours.length; j++) {
                    sNClass.add(strTrainOutputs[neighbours[j]]);
                }
                String voteStr = MajorityVoteString.winnerClass(sNClass);
                System.out.print("Actual: " + strTestOutputs[i] + " = predicted: " + voteStr);
                if (voteStr.equalsIgnoreCase(strTestOutputs[i])) {
                    checkPrediction[i] = 1;
                    match++;
                } else {
                    checkPrediction[i] = 0;
                }
                System.out.println(" : " + checkPrediction[i]);
            }//else
        }
        double acc = (match / (double) testInput.length) * 100;
        System.out.printf("\nMatched %d out of %d with accuracy:  %.2f \n", match ,testInput.length, acc);
    }

    private double euclideanDistance(double[] instance1, double[] instance2, int length) {
        double distance = 0.0;
        if (false) {
            System.out.printf("<");
            for (int i = 0; i < length; i++) {
                System.out.printf("%.2f, ", instance1[i]);
            }
            System.out.printf(">  <");
            for (int i = 0; i < length; i++) {
                System.out.printf("%.2f, ", instance2[i]);
            }
            System.out.printf("> ");
        }
        for (int i = 0; i < length; i++) {
            distance += Math.pow((instance1[i] - instance2[i]), 2);
        }
        distance = Math.sqrt(distance);
        //System.out.printf("Test %d = Train %d : %.2f \n\n", instance1.length, instance2.length, distance);
        return distance;
    }

    /**
     * 
     * @param trainingSet
     * @param testInstance
     * @param k
     * @return index on neighbor
     */
    private int[] getKNearestNeighbors(double[][] trainingSet, double[] testInstance, int k) {
        // create an array list Object
        double[] distances = new double[trainingSet.length];
        int length = testInstance.length;
        for (int i = 0; i < trainingSet.length; i++) {
            double dist = euclideanDistance(testInstance, trainingSet[i], length);
            distances[i] = dist;
        }
        // Init the element list
        ArrayList<ElementDouble> elements = new ArrayList<ElementDouble>();
        for (int i = 0; i < distances.length; i++) {
            elements.add(new ElementDouble(i, distances[i]));
        }

        // Sort and print
        Collections.sort(elements);
        //Collections.reverse(elements); // If you want reverse order
        int[] indexSort = new int[distances.length];
        int itr = 0;
        for (ElementDouble element : elements) {
            indexSort[itr++] = element.index;
            //System.out.printf("%d , %.2f \n", element.index, element.value);
        }

        int[] neighbors = new int[k];//hold the index of nighbour
        for (int i = 0; i < k; i++) {
            neighbors[i] = indexSort[i];//geting index of sample thosw are neigbour
            if (!strClass) {
                //System.out.print("N: " + neighbors[i] + " " + numTrainOutputs[neighbors[i]] + " \n");
            } else {
                //System.out.print("N: " + neighbors[i] + " " + strTrainOutputs[neighbors[i]] + " \n");
            }
        }
        return neighbors;
    }

    private void splitData(double trainFraction) {
        int trainSize = (int) (inputs.length * trainFraction);
        int testSize = (int) (inputs.length - trainSize);
        trainInput = new double[trainSize][inputs[0].length];
        testInput = new double[testSize][inputs[0].length];
        System.arraycopy(inputs, 0, trainInput, 0, trainSize);
        System.arraycopy(inputs, trainSize, testInput, 0, testSize);

        if (!strClass) {
            numTrainOutputs = new double[trainSize];
            numTestOutputs = new double[trainSize];
            System.arraycopy(numOutputs, 0, numTrainOutputs, 0, trainSize);
            System.arraycopy(numOutputs, trainSize, numTestOutputs, 0, testSize);
        } else {
            strTrainOutputs = new String[trainSize];
            strTestOutputs = new String[testSize];
            System.arraycopy(strOutputs, 0, strTrainOutputs, 0, trainSize);
            System.arraycopy(strOutputs, trainSize, strTestOutputs, 0, testSize);
        }
    }

    private void readData() {
        String fileName = "";

        //read training data
        fileName = data_file;
        System.out.println("Reading data file: " + data_file);
        readDataFromFile(fileName);
        System.out.println("Compleated.");
    }

    public static boolean isNumeric(String str) {
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    private void readDataFromFile(String fileName) {
        int rowlength = 0;
        int collength = 0;
        String absolutePath = filePath + fileName;
        try {
            FileReader fin = new FileReader(absolutePath);//readt output Train to make ensemble
            BufferedReader br = new BufferedReader(fin);
            String line;
            String[] tokens;
            rowlength = 0;
            strClass = false;
            if ((line = br.readLine()) != null) {
                tokens = line.split(",");
                collength = tokens.length;
                if (isNumeric(tokens[collength - 2])) {
                    strClass = true;
                }
            }
            while ((line = br.readLine()) != null) {
                rowlength++;
            }
            rowlength++;//count the last row

            System.out.println("    The file has " + rowlength + " samples");
            System.out.println("    Each sample has a leangth " + collength);

            inputs = new double[rowlength][collength - 1];
            if (!strClass) {
                numOutputs = new double[rowlength];
                System.out.println("Class Type: Numeric");
            } else {
                strOutputs = new String[rowlength];
                System.out.println("Class Type: String");
            }

            br.close();
            fin.close();

            FileReader fin1 = new FileReader(absolutePath);//readt output Train to make ensemble
            BufferedReader br1 = new BufferedReader(fin1);

            System.out.println("Data:");
            for (int i = 0; i < rowlength; i++) {
                System.out.print("|");
                line = br1.readLine();
                tokens = line.split(",");
                for (int j = 0; j < collength - 1; j++) {
                    inputs[i][j] = Double.parseDouble(tokens[j]);
                    if (inputs[i][j] < 0) {
                        System.out.printf("  %.2f", inputs[i][j]);
                    } else {
                        System.out.printf("   %.2f", inputs[i][j]);
                    }
                }
                if (!strClass) {
                    numOutputs[i] = Double.parseDouble(tokens[collength - 1]);
                    if (numOutputs[i] < 0) {
                        System.out.printf("  %.2f", numOutputs[i]);
                    } else {
                        System.out.printf("   %.2f", numOutputs[i]);
                    }
                } else {
                    strOutputs[i] = tokens[collength - 1];
                    System.out.printf("  %s ", strOutputs[i]);
                }
                System.out.print(" \n");
            }//real all rows
        } catch (IOException | NumberFormatException e) {
            System.out.println("Errot in data reading" + e);
        }
    }//read from file

    void print_matrix(double matrix[][], double[] output, String msg) {
        int rowlength = matrix.length;
        int collength = matrix[0].length;
        System.out.println("Printing matrix of size: " + rowlength + " x " + collength);
        System.out.println(msg + ":");
        for (int i = 0; i < rowlength; i++) {
            System.out.printf("%d |", i);
            for (int j = 0; j < collength; j++) {
                if (matrix[i][j] < 0) {
                    System.out.printf("  %.2f", matrix[i][j]);
                } else {
                    System.out.printf("   %.2f", matrix[i][j]);
                }
            }
            System.out.print(" | " + output[i] + "\n");
        }
    }//print matrix

    void print_matrix(double matrix[][], String[] output, String msg) {
        int rowlength = matrix.length;
        int collength = matrix[0].length;
        System.out.println("Printing matrix of size: " + rowlength + " x " + collength);
        System.out.println(msg + ":");
        for (int i = 0; i < rowlength; i++) {
            System.out.printf("%d |", i);
            for (int j = 0; j < collength; j++) {
                if (matrix[i][j] < 0) {
                    System.out.printf("  %.2f", matrix[i][j]);
                } else {
                    System.out.printf("   %.2f", matrix[i][j]);
                }
            }
            System.out.print(" | " + output[i] + "\n");
        }
    }//print matrix

    void save_matrix(double matrix[][], String msg) throws IOException {
        int rowlength = matrix.length;
        int collength = matrix[0].length;
        System.out.println("Saving matrix of size: " + rowlength + " x " + collength);
        System.out.println(msg + ":");

        try (FileWriter fw = new FileWriter("pca_" + data_file);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter pr = new PrintWriter(bw)) {
            for (int i = 0; i < rowlength; i++) {
                for (int j = 0; j < collength; j++) {
                    pr.print(matrix[i][j] + ",");
                }
                pr.println();
            }
            System.out.print("Saved");
        }
    }

}//KNN class end
