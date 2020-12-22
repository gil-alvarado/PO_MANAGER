/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;


import Controller.LoginViewController;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.ucanaccess.complex.Attachment;
import org.apache.commons.io.FileUtils;//writeByteArrayToFile(tempFile, f.getData());


/**
 *
 * @author Gilbert Alvarado
 */
public class FileHelper {
    
    private static String lineBreak = "-----------------------------------";
    public static void createDirectory(String name){
        File tempDir = new File(ConnectionUtil.desktopLocation + "/BMStemp/" + name);
        boolean dirExec = tempDir.mkdir();
        if(dirExec){
            System.out.println("dir exists!!\ncontinue to creat file");
        }
    }
    
    //##########################################################################
    
    public static void creatFile(String purchase_order){
        
        try {
            File rwFile = new File(ConnectionUtil.desktopLocation + "/BMStemp/" +purchase_order+ "/" + purchase_order + "-RWC.txt");
            
            boolean rwFileExec = rwFile.createNewFile();
            
            if(rwFileExec ){
                System.out.println("File's have been created");
            }
            else{
                System.out.println("file's already exists!");
            }
        } catch (IOException ex) {
            Logger.getLogger(FileHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    //##########################################################################   
    public static boolean wirteToFile(String purchase_order, String rwData, String fuData){
        
        try{
            BufferedWriter writer = new BufferedWriter( 
                    new FileWriter(ConnectionUtil.desktopLocation + "/BMStemp/" +purchase_order+ "/" +purchase_order +"-RWC.txt") );
                
                if(rwData.trim() == null){
                    System.out.println("NO COMMENT ENTERED");
                    writer.close();
                    
                }else{
                    writer.write(lineBreak);
                    writer.newLine();
                    writer.write(LoginViewController.current_user + ": ");
                    writer.newLine();
                    writer.write(lineBreak);
                    writer.newLine();
                    writer.write(rwData);
                    writer.newLine();
                    writer.write(lineBreak);
                    writer.close();
                }
                
                writer = new BufferedWriter( 
                    new FileWriter(ConnectionUtil.desktopLocation + "/BMStemp/" +purchase_order+ "/" +purchase_order +"-FUN.txt") );
                if(fuData.trim() == null){
                    System.out.println("NO COMMENT ENTERED");
                    writer.close();
                    
                }else{
                    writer.write(lineBreak);
                    writer.newLine();
                    writer.write(LoginViewController.current_user + ": ");
                    writer.newLine();
                    writer.write(lineBreak);
                    writer.newLine();
                    writer.write(fuData);
                    writer.newLine();
                    writer.write(lineBreak);
                    writer.close();
                }
                
                return true;
                
            } catch (IOException ex) {
            Logger.getLogger(FileHelper.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    //##########################################################################       
    
    public static File getRWFile(String purchase_order){
        return new File(ConnectionUtil.desktopLocation + "/BMStemp/" +purchase_order+ "/" + purchase_order + "-RWC.txt");
    }

    //##########################################################################
    
    public static File getFUFile(String purchase_order ){
        
        return new File(ConnectionUtil.desktopLocation+ "/BMStemp/" +purchase_order+ "/" + purchase_order + "-FUN.txt");
    }
}
