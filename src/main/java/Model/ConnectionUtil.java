/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author Gilbert Alvarado
 */
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.ucanaccess.complex.Attachment;

/**
 *
 * @author oXCToo
 */
public class ConnectionUtil {
//    static Connection conn = null;
    
//    private static final ClassLoader classLoader = getClass().getClassLoader();
    private static final ClassLoader classLoader = ConnectionUtil.class.getClassLoader();
    private static final File file = new File(classLoader.getResource("BMS_DATABASE.accdb").getFile());
    
//    private final static String databaseURL = "jdbc:ucanaccess://C://Users//gilbe//DocumeS_nts//Vie//BMS//data.accdb";
    private final static String databaseURL = "jdbc:ucanaccess://C://Users//gilbe//Documents//JavaFX//BMS_PO_Manager//BMS_DATABASE.accdb";
//    private static final String databaseURL = "jdbc:ucanaccess://" + file.getAbsolutePath().toString();    
    
    private static ArrayList<String> columnNames = null;
    private static LinkedHashMap<Integer, ArrayList<String>> rows= null;
    
//    private final static String query = "SELECT * FROM PO_2020;";
    private final static String query = "SELECT * FROM purchase_orders;";
//    private static final  String queryRef = "SELECT * FROM COVER_REF_FILE_2020;";
    
//    private static ResultSet rs;
//    private static Statement statement;

    
    public static Connection conDB(){
        
        try{//connect to DB and return connection
//            Class.forName("com.mysql.jdbc.Driver");
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
//            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/keeptoo_systems", "root", "R.E.4.R.E.51993!");
            Connection connection= DriverManager.getConnection(databaseURL,"app","app");
            //conn =  DriverManager.getConnection(databaseURL);
            
            System.out.println("DB CONNECTED");
            System.out.println("RETURNING DB CONNECTION: " + connection);
            return connection;
        } catch (SQLException ex ) {
            System.err.println("ConnectionUtil : "+ex.getMessage());
           return null;
        }
        catch (Exception err){
            System.err.println("ConnectionUtil: " + err);
            return null;
        }
    }
//##############################################################################    
    //method called when user selects a different DataBase
    public static Connection conDB(String dbLocation){
        
        try{
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            Connection connection= DriverManager.getConnection(dbLocation,"app","app");
            System.out.println("DB CONNECTED");
            System.out.println("RETURNING DB CONNECTION: " + connection);
            return connection;
        } catch (SQLException ex ) {
            System.err.println("ConnectionUtil : "+ex.getMessage());
           return null;
        }
        catch (Exception err){
            System.err.println("ConnectionUtil: " + err);
            return null;
        }
    }
//##############################################################################    
    //fileAttachments column is going to cause problems
    public static ArrayList columnNames(){
        try{
            Statement statement = conDB().createStatement();
            ResultSet rs = statement.executeQuery(query);
            ResultSetMetaData rsmd = rs.getMetaData();
            columnNames = new ArrayList<>();
            
            for(int i = 1; i <= rsmd.getColumnCount(); i++){
                if(rsmd.getColumnName(i).equals("FileAttachments") == false){
                    columnNames.add(rsmd.getColumnName(i));
//                    System.out.println(rsmd.getColumnName(i));
                }
            }
            
            return columnNames;
        }
        catch (SQLException e) {
            System.out.println("Error in ConnectionUtil columnNames:" + e.getMessage());
            return null;
        }
    }
//##############################################################################  
    public static LinkedHashMap dataRows(){
        try{
            Statement statement = conDB().createStatement();
            ResultSet result = statement.executeQuery(query);
//            ResultSetMetaData rsmd = rs.getMetaData();
            rows = new LinkedHashMap<Integer, ArrayList<String>>();
            ArrayList<String> rowData = null;
            
            while(result.next()){
                
                rowData = new ArrayList<String>();
                for(int i = 1; i <= result.getMetaData().getColumnCount() ; i++){
                        //&& result.getMetaData().getColumnName(i).equals("FileAttachments") == false; i++){
                    if(result.getMetaData().getColumnName(i).equals("FileAttachments") == false){//then add data to hashmamp
                        rowData.add(result.getString(i));
                    }
                    rows.put(result.getInt("ORDER_NUMBER"), rowData);
                }
            } 
            
            return rows;
        }
        catch (SQLException e) {
            System.out.println("Error in ConnectionUtil dataRows:" + e.getMessage());
            return null;
        }
    }
//##############################################################################
    private static ArrayList<String> tableList = null;
    
    public static ArrayList getTables(){
        
        try{
//            Statement statement = conDB().createStatement();
//            ResultSet rs = statement.executeQuery(tables);
            ResultSet rs = conDB().getMetaData().getTables(null, null, null, null);
            tableList = new ArrayList<String>();
            
            while(rs.next()){
                    tableList.add(rs.getString("TABLE_NAME"));
            }
            return tableList;
            
        }   catch (SQLException ex) {
                Logger.getLogger(ConnectionUtil.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
//        return null;
    }
//##############################################################################    
    private static final String fileAttachments = "SELECT ORDER_NUMBER, fileAttachments FROM PO_2020;";
    //handle file Attachments
    public static ArrayList attachments(String OrderId){
        try{
            Statement statement = conDB().createStatement();
            ResultSet result = statement.executeQuery(fileAttachments);
//            ResultSetMetaData rsmd = rs.getMetaData();
            rows = new LinkedHashMap<Integer, ArrayList<String>>();
            ArrayList<String> files = null;
            
            return files;
            
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionUtil.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        
    }
    
//##############################################################################
    public static int rowCount(){
        
        try{
            Statement statement = conDB().createStatement();
            ResultSet result = statement.executeQuery("SELECT COUNT(*) FROM purchaseorders;");
//            ResultSetMetaData rsmd = rs.getMetaData();
           System.out.println(result);
            
           result.next();
           System.out.println("COUNT: " + result.getString(1));
            return Integer.parseInt(result.getString(1));
            
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionUtil.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }
}
