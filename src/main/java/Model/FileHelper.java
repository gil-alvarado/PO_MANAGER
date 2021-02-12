/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;


import Controller.LoginViewController;
import Controller.NotesViewController;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Gilbert Alvarado
 */
public class FileHelper {
    
    //create directory for specific PO
    public static boolean createDirectory(String purchase_order){
        File tempDir = new File(ConnectionUtil.dbDirectoryLocation()+ "\\purchase_orders\\" + purchase_order);
        System.out.println("FileHelper: CREATE PO DIRECTORY FOR " + purchase_order);
        return tempDir.mkdirs();
    }
    
    public static boolean createEmailDirectory(){
        File tempDir = new File(System.getenv("USERPROFILE") + "\\Documents\\BMS_DB_APP\\tempEmailDir");//ConnectionUtil.dbDirectoryLocation()+ "\\tempEmailDir");
        return tempDir.mkdirs();
    }
    public static String getTempEmailDirectoryLocation(){
        return System.getenv("USERPROFILE") + "\\Documents\\BMS_DB_APP\\tempEmailDir";//ConnectionUtil.dbDirectoryLocation() + "\\tempEmailDir";
    }    
    //--------------------------------------------------------------------------

    public static String getPoAttachmentDirectory(String purchase_order) {
        return ConnectionUtil.dbDirectoryLocation()+"\\purchase_orders\\"+purchase_order +"\\Attachments";
    }

    public static boolean createPoAttachmentsDirectory(String purchase_order){
        File tempDir = new File(ConnectionUtil.dbDirectoryLocation()+"\\purchase_orders\\"+purchase_order +"\\Attachments");
//        poAttachmentDirectory = tempDir.getAbsolutePath();
        return tempDir.mkdir();
    }
    public static boolean createTemplateDirectory(){
        File tempDir = new File(ConnectionUtil.dbDirectoryLocation()+"\\report_templates");
//        poAttachmentDirectory = tempDir.getAbsolutePath();
        return tempDir.mkdir();
    }
    public static String getTemplateExternalSource(){
        return ConnectionUtil.dbDirectoryLocation()+"\\report_templates\\template_TAG.docx";
    }
    
//    private static final String report_template = getClass().getClassLoader().getResource("test/resources/Templates/template_TAG.docx").getPath();
//    public static String getLocalFileTemplate(){
//        return report_template;
//    }
    //##########################################################################
    //relates to RW comment and notes
    public static boolean createCSVfiles(String purchase_order){//prep_file
        
//        FileOutputStream fos_RW;
        FileOutputStream fos_FU;
        
        FileOutputStream outFile = null;
        
        StringBuilder data;
        
        try {
            data = new StringBuilder();

            outFile = new FileOutputStream(ConnectionUtil.dbDirectoryLocation()+"\\purchase_orders\\"+ purchase_order + "\\" +purchase_order + "-RWC.csv",true);
            
            System.out.println("adding headers to buffer");
            data.append("purchase_order").append(",");
            data.append("user").append(",");
            data.append("date").append(",");
            data.append("note").append("\n");
            
            System.out.println("ADDING HEADERS TO FILES");
            
            outFile.write(data.toString().getBytes());
            
            outFile = new FileOutputStream(ConnectionUtil.dbDirectoryLocation()+"\\purchase_orders\\"+ purchase_order + "\\" +purchase_order + "-FUN.csv",true);
            
            outFile.write(data.toString().getBytes());
            
            System.out.println("WROTE HEADERS TO CSV FILEs");
            
            outFile.close();
            
            return true;
            
        } catch (IOException ex) {
            Logger.getLogger(FileHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
    //##########################################################################   
    //USED WHEN USER ADDS A NEW PO, CONSIDER RENAMING
    //##########################################################################
    public static boolean wirteToFile(String purchase_order, String user, String rwData, String fuData){
        
        if(rwData.isEmpty() && fuData.isEmpty())//dont bother writing
            return true;
        
        FileOutputStream outFile = null;
        
        try{
                
            StringBuilder sb = new StringBuilder();
            
            //##################################################################
            //      RW COMMENT
            //##################################################################            
            
            if(!rwData.isEmpty()){
                sb.append(purchase_order).append(",");
                sb.append(user).append(",");
                sb.append(new Date().toString()).append(",");
                sb.append(rwData).append("\n");
 
                outFile = new FileOutputStream(ConnectionUtil.dbDirectoryLocation()+"\\purchase_orders\\"+ purchase_order + "\\" +purchase_order + "-RWC.csv",true);
                
                outFile.write(sb.toString().getBytes());
                outFile.flush();
                sb.setLength(0);
            }
            
            //##################################################################
            //      FU NOTES
            //##################################################################
            if(!fuData.isEmpty()){
                sb.append(purchase_order).append(",");
                sb.append(user).append(",");
                sb.append(new Date().toString()).append(",");
                sb.append(fuData).append("\n");

                outFile = new FileOutputStream(ConnectionUtil.dbDirectoryLocation()+"\\purchase_orders\\"+ purchase_order + "\\" +purchase_order + "-FUN.csv",true);

                outFile.write(sb.toString().getBytes());
                outFile.flush();
                outFile.close();
            }
            return true;
                
            } catch (IOException ex) {
            Logger.getLogger(FileHelper.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }finally{
            if(outFile!=null)
                try {
                    outFile.close();
            } catch (IOException ex) {
                Logger.getLogger(FileHelper.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    //##########################################################################
    //          USED BY NOTES VIEW, DO NOT CREATE NEW FILE
    //          OPEN EXISTING FILE  
    //          AND UPDATE po_notes table ATTACHMENT
    //##########################################################################
    public static boolean updateFUCsvFile(File file, String purchase_order, String new_content){
        
        
        FileWriter src = null;
        try {
            
            src = new FileWriter(file, true);
            src.append(purchase_order).append(",");
            src.append(LoginViewController.current_user).append(",");
            src.append(new Date().toString()).append(",");
            src.append(new_content).append("\n");
            
            return true;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FileHelper.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
//                outFile.close();
                src.close();
            } catch (IOException ex) {
                Logger.getLogger(FileHelper.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }
    //##########################################################################       
    
    public static File getRWFile(String purchase_order){
        
        return new File(ConnectionUtil.dbDirectoryLocation()+ "\\purchase_orders\\" +purchase_order+"/"+ purchase_order+"-RWC.csv");
//            return new File(ConnectionUtil.desktopLocation + "/BMStemp/" +purchase_order+ "/" + purchase_order + "-RWC.txt");
//      return new File(ConnectionUtil.desktopLocation + "/BMStemp/" +purchase_order+ "/" + purchase_order + "-RWC.csv");
//      return new File(ConnectionUtil.networkLocation + "/" +purchase_order+ "/" + purchase_order + "-RWC.csv");
    }

    //##########################################################################
    
    public static File getFUFile(String purchase_order ){
        return new File(ConnectionUtil.dbDirectoryLocation()+ "\\purchase_orders\\" +purchase_order+"/"+ purchase_order+"-FUN.csv");
//            return new File(ConnectionUtil.desktopLocation+ "/BMStemp/" +purchase_order+ "/" + purchase_order + "-FUN.csv");
//            return new File(ConnectionUtil.networkLocation+ "/" +purchase_order+ "/" + purchase_order + "-FUN.csv");
    }
    
    //##########################################################################
    public static StringBuilder fileContent(File file){
        
        try{
            BufferedReader br = 
                    new BufferedReader(
                            new FileReader(file.getAbsoluteFile()));
            
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while(line != null){
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            br.close();
            return sb;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileHelper.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (IOException ex) {
            Logger.getLogger(FileHelper.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    //##########################################################################
    //ERROR OCCURS HERE: VALIDATE DATE
    //##########################################################################
    public static String csvFileContent(File csv_file){
        try {
            
                BufferedReader csvReader = new BufferedReader(new FileReader(csv_file));
                StringBuilder sb = new StringBuilder();
                String row = "";
                String []header = csvReader.readLine().split(",");
                
                int line_length = 0;
                for(String h: header)
                        if(h.length()>line_length)
                            line_length = h.length();
                
                //##############################################################
                // VALIDATE EMPTY NOTES/COMMENTS
                //##############################################################
                while((row = csvReader.readLine()) != null){
                    System.out.println("READING CSV FILE");
                    String row_data[] = row.split(",");
                    
                    String l = "";
                    
                    for(int i = 0; i < (line_length*2); i++){
                        l+="--";
                    }
                    l+="\n";
                    sb.append(l).append(row_data[1]).append(": ").append(row_data[2]).append("\n").append(l);
                    sb.append(row_data[3]).append("\n\n");//NOTE
                }
//                existingMessageTextArea.setText(sb.toString());
//                sb.setLength(0);
                return sb.toString();
                
        } catch (FileNotFoundException ex) {
            Logger.getLogger(NotesViewController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(NotesViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
