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
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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
//    private static final File file = new File(classLoader.getResource("BMS_DATABASE_TEST.accdb").getFile());
//    private static final String databaseURL = "jdbc:ucanaccess://" + file.getAbsolutePath();

    private final static String userprofile = System.getenv("USERPROFILE");
    public final static String desktopLocation = userprofile +"\\Desktop" ;
    private final static String databaseURL = "jdbc:ucanaccess://"+userprofile+"\\Desktop\\BMStemp\\BMS_DATABASE_TEST.accdb";
    
    
    //OFFICE TEST
    //private final static String databaseNetworkLocation = "jdbc:ucanaccess://\\\\exch16\\Shared\\BMS_TEST\\BMS_DATABASE_TEST.accdb";
//    public final static String networkLocation = "\\\\exch16\\Shared\\BMS_TEST";
    
    private static ArrayList<String> columnNames = null;
    
    private static LinkedHashMap<Integer, String> userLoginInfo = null;
    
    
    private static LinkedHashMap<Integer, ArrayList<String>> rows= null;
    
    private static LinkedHashMap<Integer, String> roleTableData = null;
    
    private static String dbURL ;//= "jdbc:ucanaccess://";
    private static String dbDirectory;
    
    
    
//    public static Connection conDB(){
////        System.out.println("CURRENT USER: " + System.getProperty("user.name"));
//        
//        try{
//            
//            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
//            
//            Connection connection= DriverManager.getConnection(databaseURL,"app","app");
////            Connection connection= DriverManager.getConnection(databaseNetworkLocation,"app","app");
//            
//            System.out.println("DB CONNECTED");
//            System.out.println("RETURNING DB CONNECTION: " + connection);
//            return connection;
//        } catch (SQLException ex ) {
//            System.err.println("ConnectionUtil : "+ex.getMessage());
//           return null;
//        }
//        catch (Exception err){
//            System.err.println("ConnectionUtil: " + err);
//            return null;
//        }
//    }
//##############################################################################    
    //method called when user selects a different DataBase
    public static void setDbLocation(String dbLocation, String dbDirectory){
        
        ConnectionUtil.dbURL = "jdbc:ucanaccess://"+ dbLocation;
        
        System.out.println("----------------------------");
        System.out.println("ConnectionUtil: DATABASE LOCATION: " + ConnectionUtil.dbURL );
        ConnectionUtil.dbDirectory = dbDirectory;
        System.out.println("ConnectionUtil: dbDirectoty location: " + ConnectionUtil.dbDirectory );
        System.out.println("----------------------------");
    }
    //--------------------------------------------------------------------------
    public static final String dbDirectoryLocation (){
        return ConnectionUtil.dbDirectory;
    }
    //##########################################################################
    public static Connection conDB(){//String dbLocation){
        
//        System.out.println("DB SELECTED: "  + dbLocation);
        try{
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            System.out.println("ATTEMPTING TO RETURN DATABASE: " + ConnectionUtil.dbURL);
            
            Connection connection= DriverManager.getConnection(ConnectionUtil.dbURL,"app","app");
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
    private static final String addNewUser = "INSERT INTO users(first_name, last_name, user_name, role_id) "
            + "VALUES (?,?,?,?);";
    public static int addNewUser(String firstName, String lastName, String user_name, int role_id){
        
        try{
            PreparedStatement pst = conDB().prepareStatement(addNewUser);//conDB().prepareStatement(addNewUser);
            pst.setString(1, firstName);
            pst.setString(2, lastName);
//            String user_name = firstName.substring(0, 1) + "_" + lastName.substring(0, lastName.length()/2);
//            pst.setInt(2, role_id);
            pst.setString(3, user_name);
            pst.setInt(4, role_id);
            return pst.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println("Error in ConnectionUtil userVerification:" + e.getMessage());
            return 0;
        }
        
    }
    //##########################################################################
    
    private static final String users_query = "SELECT user_id, user_name FROM users;";
    
    public static LinkedHashMap userIDS(){
        
        try{
            Statement statement = conDB().createStatement();
            ResultSet rs = statement.executeQuery(users_query);
//            ResultSetMetaData rsmd = rs.getMetaData();
//
//            userIds = new ArrayList<>();
//            userNames = new ArrayList<>();
            userLoginInfo = new LinkedHashMap<>();
            
            while(rs.next()){
//                userIds.add(rs.getInt("user_id"));
                userLoginInfo.put(rs.getInt("user_id"), rs.getString("user_name"));
            }
            
            return userLoginInfo;
        }
        catch (SQLException e) {
            System.out.println("Error in ConnectionUtil columnNames:" + e.getMessage());
            return null;
        }
    }
    //##########################################################################
    
    public static ResultSet getUsersTableData(){
        try{
            PreparedStatement pst = conDB().prepareStatement("SELECT * FROM users;");
            ResultSet rs = pst.executeQuery();
            
            if(rs.next())
                return rs;
            else 
                return null;
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    //##########################################################################
    //                      get roles table
    //##########################################################################
    private final static String roleTableQuery = "SELECT role_id, role_name FROM role;";
    
    public static LinkedHashMap roleTable(){
        
        try{
            Statement statement = conDB().createStatement();
            ResultSet rs = statement.executeQuery(roleTableQuery);
            roleTableData = new LinkedHashMap<>();
            while(rs.next()){
                roleTableData.put(rs.getInt("role_id"), rs.getString("role_name"));
            }
            
            return roleTableData;
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    //##########################################################################
    
    public static ResultSet userVerification(String user_name){
        
        try{
            PreparedStatement pst = conDB().prepareStatement("SELECT user_name, role_id FROM users WHERE "
                    + "user_name = ?");
            pst.setString(1, user_name);
            
            ResultSet rs = pst.executeQuery();
            
            if(rs.next())
                return rs;
            else
                return null;
        }
        catch (SQLException e) {
            System.out.println("Error in ConnectionUtil userVerification:" + e.getMessage());
            return null;
        }
        
    }
    
    //##########################################################################
    public static boolean preUpdateUser(String fname, String lname, int role, String uname){
        try{
            PreparedStatement pst = conDB()
                    .prepareStatement("SELECT first_name, last_name, role_id, user_name FROM users "
                            + "WHERE first_name = ? AND last_name = ? AND role_id = ? AND user_name = ? ");
            pst.setString(1, fname);
            pst.setString(2, lname);
            pst.setInt(3, role);
            pst.setString(4, uname);
            
            ResultSet rs = pst.executeQuery();
            
            if(rs.next())
                return rs.getString("first_name").equals(fname) && rs.getString("last_name").equals(lname)
                        && rs.getInt("role_id") == role && rs.getString("user_name").equals(uname);
            else
                return false;
        }
        catch (SQLException e) {
            System.out.println("Error in ConnectionUtil userVerification:" + e.getMessage());
            return false;
        }
    }
    
    public int updateUser(String fname, String lname, int role, String uname){
        
        
        
        return 1;
    }
    //-------------------------------------------------------------------------
    public static boolean userExists(String user_name){
        
        try{
            PreparedStatement pst = conDB().prepareStatement("SELECT user_name FROM users WHERE "
                    + "user_name = ?");
            pst.setString(1, user_name);
            
            ResultSet rs = pst.executeQuery();
            
            if(rs.next())
                return rs.getString("user_name").equals(user_name);
            else
                return false;
        }
        catch (SQLException e) {
            System.out.println("Error in ConnectionUtil userVerification:" + e.getMessage());
            return false;
        }
    }
//##############################################################################
    
    public static int updatePacketStatus(String purchase_order, String status){
        
        try{
            PreparedStatement pst = conDB().prepareStatement("UPDATE order_details "
                    + "SET moved_to_packet = ? "
                    + "WHERE purchase_order = ?;");
            
            System.out.println("CONNECTION UTIL, STATUS: " + status);
            
            pst.setString(1, status);
//            if(status.equals("YES"))
//                pst.setString(1, "TRUE");
//            else
//                pst.setString(1, "FALSE");
            
            pst.setString(2,purchase_order);
            
            
            return pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionUtil.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }
    
    
    //##########################################################################
    private static final String getAllDataQuery = 
            "SELECT "
            + "purchase_orders.purchase_order, "
            + "purchase_orders.original_ship_date, "//2
            + "purchase_orders.current_ship_date, "//3
            + "purchase_orders.eta_bms,"//4
            + "purchase_orders.fu_date, "//5
            + "order_details.quantity, order_details.landed_cost, order_details.invoice_price, "
            + "order_details.confirmed, "
            + "order_details.attachments, "//10
            + "bearings.brg_name, "
            + "bearings.PARAMETER, "
            + "bearings.supplier_id, "
            + "po_notes.rw_comments, "//14
            + "po_notes.fu_notes, "//15
            + "order_details.brg_id\n"
            + "" 
            +"FROM (purchase_orders "
            + "INNER JOIN ((suppliers "
            + "INNER JOIN bearings ON suppliers.[supplier_id] = bearings.[supplier_id]) "
            + "INNER JOIN order_details ON bearings.[brg_id] = order_details.[brg_id]) "
            +       "ON purchase_orders.[purchase_order] = order_details.[purchase_order]) "
            + "INNER JOIN po_notes ON purchase_orders.[purchase_order] = po_notes.[purchase_order] "
            + ""
            + "WHERE purchase_orders.purchase_order = ?";
    public static ArrayList columnNames(String purchase_order_parameter){
        try{
            PreparedStatement pst = conDB().prepareStatement(getAllDataQuery);
            pst.setString(1, purchase_order_parameter);
            
            ResultSet rs = pst.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            columnNames = new ArrayList<>();
            
            for(int i = 1; i <= rsmd.getColumnCount(); i++){
//                if(rsmd.getColumnName(i).equals("FileAttachments") == false){
                    columnNames.add(rsmd.getColumnName(i));
//                    System.out.println(rsmd.getColumnName(i));
//                }
            }
            
            return columnNames;
        }
        catch (SQLException e) {
            System.out.println("Error in ConnectionUtil columnNames:" + e.getMessage());
            return null;
        }
    }
    
    //##########################################################################
    
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
//##############################################################################  
    private static final String getAllQuery = 
            "SELECT "
            + "purchase_orders.purchase_order, "
            + "FORMAT(purchase_orders.original_ship_date, 'Short Date'), "//2
            + "FORMAT(purchase_orders.current_ship_date, 'Short Date'), "//3
            + "FORMAT(purchase_orders.eta_bms, 'Short Date'),"//4
            + "FORMAT(purchase_orders.fu_date, 'Short Date'), "//5
            + "order_details.quantity, order_details.landed_cost, order_details.invoice_price, "
            + "order_details.confirmed, "
            + "order_details.attachments, "//10
            + "bearings.brg_name, "
            + "bearings.PARAMETER, "
            + "bearings.supplier_id, "
            + "po_notes.rw_comments, "//14
            + "po_notes.fu_notes, "//15
            + "order_details.brg_id\n"
            + "" 
            +"FROM (purchase_orders "
            + "INNER JOIN ((suppliers "
            + "INNER JOIN bearings ON suppliers.[supplier_id] = bearings.[supplier_id]) "
            + "INNER JOIN order_details ON bearings.[brg_id] = order_details.[brg_id]) "
            +       "ON purchase_orders.[purchase_order] = order_details.[purchase_order]) "
            + "INNER JOIN po_notes ON purchase_orders.[purchase_order] = po_notes.[purchase_order] "
            + ""
            + "WHERE purchase_orders.purchase_order = ?";
    public static ResultSet getAllData(String purchase_order){
        
        try{
            PreparedStatement pst = conDB().prepareStatement(getAllQuery);
            pst.setString(1, purchase_order);
            
            ResultSet rs = pst.executeQuery();
            
            if(rs.next())
                return rs;
            else
                return null;
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
    //##########################################################################
    //USED IN ADD PO
    //CREATE METHOD TO UPDATE
    //##########################################################################
    //files were modified before calling method
    private static final String updateNotesTable = 
            "INSERT INTO po_notes( purchase_order, rw_comments, fu_notes ) "
            + "VALUES(?,?,?)";
    public static int updateNotesTable(String purchase_order){
        try{
            
            PreparedStatement pst = conDB().prepareStatement(updateNotesTable);
            pst.setString(1, purchase_order);
            
            File file = FileHelper.getRWFile(purchase_order);
            byte[] attachmentData = java.nio.file.Files.readAllBytes(Paths.get(file.getAbsolutePath()));
            System.out.println("READ RW DATA");
            Attachment[] attachment = new Attachment[]{ 
                    new Attachment(file.getAbsolutePath(), file.getName(),null,attachmentData,null,null)};
            //2: rw_comment

            pst.setObject(2, attachment);

            //3: fu_note

            file = null; attachmentData = null; attachment = null;
            
            file = FileHelper.getFUFile(purchase_order);
            attachmentData = java.nio.file.Files.readAllBytes(Paths.get(file.getAbsolutePath()));
            System.out.println("READ FU DATA");
            attachment = new Attachment[]{new Attachment(file.getAbsolutePath(), file.getName(),null,attachmentData,null,null)};
            pst.setObject(3, attachment);
            
            return pst.executeUpdate();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(ConnectionUtil.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
        
//        return 0;
    }
    //--------------------------------------------------------------------------
    private static final String updateNotes = "UPDATE po_notes "
            + "SET rw_comments = ?, fu_notes = ? "
            + "WHERE purchase_order = ?";
    public static int updateNotesAttachments(String purchase_order){
        try{
            
            PreparedStatement pst = conDB().prepareStatement(updateNotes);
            
            File file = FileHelper.getRWFile(purchase_order);
            byte[] attachmentData = java.nio.file.Files.readAllBytes(Paths.get(file.getAbsolutePath()));
            System.out.println("READ RW DATA");
            Attachment[] attachment = new Attachment[]{ 
                    new Attachment(file.getAbsolutePath(), file.getName(),null,attachmentData,null,null)};
            //1: rw_comment

            pst.setObject(1, attachment);

            //2: fu_note

            file = null; attachmentData = null; attachment = null;
            
            file = FileHelper.getFUFile(purchase_order);
            attachmentData = java.nio.file.Files.readAllBytes(Paths.get(file.getAbsolutePath()));
            System.out.println("READ FU DATA");
            attachment = new Attachment[]{new Attachment(file.getAbsolutePath(), file.getName(),null,attachmentData,null,null)};
            pst.setObject(2, attachment);
            
            pst.setString(3, purchase_order);
            
            return pst.executeUpdate();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(ConnectionUtil.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }
   
}
