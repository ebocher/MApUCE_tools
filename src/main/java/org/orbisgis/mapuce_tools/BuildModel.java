package org.orbisgis.mapuce_tools;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;

/**
 * Use to build the file .model and do some Evaluation 
 * @author Melvin Le Gall
 */
public class BuildModel {
    
    //Path to acess the .model
    private String pathModel;
    
    //use to make Evaluation after build
    private RandomForest rf;
    
    //Instances create to build the Random Forest
    private Instances learning;
    
    /**
     * Build the File .model by using RandomForest algorithm
     * @param path path where are stored the data use to build the forest
     * @param pathModelFile path where you want to store the .model file
     * @param classIndex Collumun where the class is
     * @param options List of options (ref Weka.classifier.setOptions())
     * @throws Exception 
     */
    public BuildModel(String path,String pathModelFile, int classIndex,String[] options) throws Exception{
        
        learning = this.makeInstances(path, classIndex);
        
        rf = new RandomForest();
        rf.setOptions(options);
        rf.buildClassifier(learning);     
         
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(pathModelFile));
        oos.writeObject(rf);
        oos.flush();
        oos.close();
        
        pathModel = pathModelFile;
    }
    
    /**
     * Build the File .model by using RandomForest algorithm
     * @param path path where are stored the data use to build the forest
     * @param pathModelFile path where you want to store the .model file
     * @param classIndex Collumun where the class is
     * @throws Exception 
     */
    public BuildModel(String path,String pathModelFile, int classIndex) throws Exception{
        
        Instances train = this.makeInstances(path, classIndex);

        rf = new RandomForest();
        rf.buildClassifier(train);
         
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(pathModelFile));
        oos.writeObject(rf);
        oos.flush();
        oos.close();
        
        pathModel = pathModelFile;
    }
    
 
    /**
     * Convert File in a data usable in weka (Instances)
     * @param path path for create Instances
     * @param classIndex Index of classe 
     * @return the Instances necessary to create Training data
     * @throws Exception 
     */
    private Instances makeInstances(String path, int classIndex) throws Exception{
        
        ConverterUtils.DataSource sourceTestValue = new ConverterUtils.DataSource(path);
    	Instances value = sourceTestValue.getDataSet();
    	value.setClassIndex(classIndex);
        
        return value;
    }
    
    
    /**
     * 
     * @return the Classifier Object in the file .model
     * @throws Exception 
     */
    public RandomForest getClassifier() throws Exception{

        return (RandomForest) weka.core.SerializationHelper.read(pathModel);
    }
    
    /**
     * Method use to evaluate the accuracy of the alogrithm
     * @throws Exception 
     */
    public void evaluate() throws Exception{
        
        Evaluation eval = new Evaluation(learning);
        eval.evaluateModel(rf,learning);
        
        System.out.println(eval.toSummaryString("\nResults\n======\n", false));       
        System.out.println(eval.toMatrixString());
    }
}
