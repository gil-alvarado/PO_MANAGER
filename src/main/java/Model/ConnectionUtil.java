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
import Controller.LoginViewController;
import Controller.ManageUsersViewController;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.time.LocalDate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
//    public final static String desktopLocation = userprofile +"\\Desktop" ;
//    private final static String databaseURL = "jdbc:ucanaccess://"+userprofile+"\\Desktop\\BMStemp\\BMS_DATABASE_TEST.accdb";
    
    
    //OFFICE TEST
    //private final static String databaseNetworkLocation = "jdbc:ucanaccess://\\\\exch16\\Shared\\BMS_TEST\\BMS_DATABASE_TEST.accdb";
//    public final static String networkLocation = "\\\\exch16\\Shared\\BMS_TEST";
    
    private static ArrayList<String> columnNames = null;
    
    private static LinkedHashMap<Integer, String> userLoginInfo = null;
    
    
    private static LinkedHashMap<Integer, String> roleTableData = null;
    
    private static String dbURL ;//= "jdbc:ucanaccess://";
    private static String dbDirectory;
    
//##############################################################################    
    //method called when user selects a different DataBase
    public static void setDbLocation(String dbLocation, String dbDirectory){
        
        ConnectionUtil.dbURL = "jdbc:ucanaccess://"+ dbLocation;
        
        System.out.println("----------------------------");
        System.out.println("ConnectionUtil: DATABASE LOCATION: " + ConnectionUtil.dbURL );
        ConnectionUtil.dbDirectory = dbDirectory;
        System.out.println("ConnectionUtil: dbDirectoty location: " + ConnectionUtil.dbDirectory );
        System.out.println("----------------------------");
        if(!FileHelper.createTemplateDirectory())
            System.out.println("TEMPLATE DIRECTORY ALREADY EXISTS");
        else
            System.out.println("CREATED TEMPLATE DIRECTORY");
    }
    
    //--------------------------------------------------------------------------
    
    public static final String dbDirectoryLocation (){
        return ConnectionUtil.dbDirectory;
    }
    //##########################################################################
    public static Connection conDB(){
        
        try{
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            System.out.println("ATTEMPTING TO RETURN DATABASE: " + ConnectionUtil.dbURL);
            
            Connection connection= DriverManager.getConnection(ConnectionUtil.dbURL,"app","app");
            System.out.println("DB CONNECTED");
            System.out.println("RETURNING DB CONNECTION: " + connection);
            System.out.println("CURRENT WINDOWS USER: " + System.getenv("USERPROFILE"));
            return connection;
        } catch ( SQLException ex ) {
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
            pst.setString(3, user_name);
            pst.setInt(4, role_id);
            return pst.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println("Error in ConnectionUtil userVerification:" + e.getMessage());
            return 0;
        }
        
    }
    
    public static HashMap<String, UserInfoModel> getUsersTable(){

        try {
            ResultSet rs = conDB().createStatement().executeQuery("SELECT * FROM users;");
            
            HashMap<String,UserInfoModel> user_data = new HashMap<>();
//            List <UserInfoModel> data = new ArrayList<>();
            
            while(rs.next()){
                String position = (rs.getInt("role_id") == 1) ? "ADMIN": "STAFF";
//                if(rs.getInt("role_id") == 1)
//                    position = "ADMIN";
//                else
//                    position = "STAFF";
                user_data.put(rs.getString("user_name"),new UserInfoModel(
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("user_name"),position,
                        rs.getInt("role_id")
                ));
            }
            
            return user_data;

        } catch (SQLException ex) {
            Logger.getLogger(ManageUsersViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    //##########################################################################
    
    private static final String users_query = "SELECT user_id, user_name FROM users;";
    
    public static LinkedHashMap userIDS(){
        
        try{
            Statement statement = conDB().createStatement();
            ResultSet rs = statement.executeQuery(users_query);
            userLoginInfo = new LinkedHashMap<>();
            
            while(rs.next()){
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

    //--------------------------------------------------------------------------
    
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
    
    //##########################################################################
    
    public static int updatePacketStatus(String purchase_order, String status){
        
        try{
            PreparedStatement pst = conDB().prepareStatement("UPDATE order_details "
                    + "SET moved_to_packet = ? "
                    + "WHERE purchase_order = ?;");
            
            System.out.println("CONNECTION UTIL, STATUS: " + status);
            
            pst.setString(1, status);
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
    
    //EditPOView No Anchor LINE 259
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
    
    
    private static final String newGetAll =
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
            + "INNER JOIN po_notes ON purchase_orders.[purchase_order] = po_notes.[purchase_order] ;";
    public static HashMap<String,BMSPurchaseOrderModel> getAllData(){
        /*
        BMSPurchaseOrderModel(String purchase_order, String supplier, 
            String confirmed, String invoice_price, String brg_number, 
            String parameter, String quantity, String landing_cost, 
            Date original_ship, Date current_ship, Date eta_ship, Date fu_ship, 
            Attachment attachments)
        */
        DecimalFormat LCformat = new DecimalFormat("$###,###,##0.00");
        DecimalFormat IPformat = new DecimalFormat("$###,###,##0.000");
        try{
            Statement statement = conDB().createStatement();
            ResultSet rs = statement.executeQuery(newGetAll);
            HashMap<String, BMSPurchaseOrderModel> allData = new HashMap<>();
            
            while(rs.next()){
                allData.put(rs.getString("purchase_order"),//KEY
                        
                        new BMSPurchaseOrderModel(
                                rs.getString("purchase_order"),
                                rs.getString("supplier_id"),
                                rs.getString("confirmed"),
                                IPformat.format(rs.getDouble("invoice_price")),
                                rs.getString("brg_name"),
                                rs.getString("PARAMETER"),
                                rs.getString("quantity"),
                                LCformat.format(rs.getDouble("landed_cost")),
                                rs.getDate("original_ship_date"),
                                rs.getDate("current_ship_date"),
                                rs.getDate("eta_bms"),
                                rs.getDate("fu_date"),(Attachment[])rs.getObject("attachments")
                        ));
            }
            
            return allData;
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;          
    }
    public static List<BMSPurchaseOrderModel> getOrderList(String po, String supplier){
        
        PreparedStatement pst = null;
        
        List <BMSPurchaseOrderModel> order_list = new ArrayList<>(); 
        
        for(Map.Entry<String, BMSPurchaseOrderModel> item : getAllData().entrySet()){     
            if(item.getValue().getSupplier().equals(supplier) && item.getKey().equals(po)){
                order_list.add(item.getValue());
           } 
        }
        return order_list;
    }
    //--------------------------------------------------------------------------
    
    //EDIT POVIEW LINE 257
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
    
    private static final String updateNotesTable = 
            "INSERT INTO po_notes( purchase_order, rw_comments, fu_notes ) "
            + "VALUES(?,?,?)";
    
    public static boolean updateNotesTable(String purchase_order){
        try{
            
            PreparedStatement pst = conDB().prepareStatement(updateNotesTable);
            pst.setString(1, purchase_order);
            
            File file = FileHelper.getRWFile(purchase_order);
            byte[] attachmentData = java.nio.file.Files.readAllBytes(Paths.get(file.getAbsolutePath()));
            System.out.println("READ RW DATA");
            Attachment[] attachment = new Attachment[]{ 
                    new Attachment(file.getAbsolutePath(), file.getName(),"csv",attachmentData,null,null)};
            //2: rw_comment

            pst.setObject(2, attachment);

            //3: fu_note

            file = null; attachmentData = null; attachment = null;
            
            file = FileHelper.getFUFile(purchase_order);
            attachmentData = java.nio.file.Files.readAllBytes(Paths.get(file.getAbsolutePath()));
            System.out.println("READ FU DATA");
            attachment = new Attachment[]{new Attachment(file.getAbsolutePath(), file.getName(),"csv",attachmentData,null,null)};
            pst.setObject(3, attachment);
            
            return pst.executeUpdate() == 1;
        } catch (SQLException | IOException ex) {
            Logger.getLogger(ConnectionUtil.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
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
    
    //##########################################################################
    //                  ADD PO METHODS
    //##########################################################################
    public static boolean poExists(String purchase_order){
        
        PreparedStatement pst = null;
        
        try {
            pst = conDB().prepareStatement("SELECT purchase_order FROM purchase_orders WHERE purchase_order = ?;");
            pst.setString(1, purchase_order);
            
            ResultSet rs = pst.executeQuery();
            
            System.out.println("CHECKING IF ENTERED PO EXISTS. IF EXISTS, WARN USER AND END PROCESS");
            
            if(rs.next())
                return true;
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
    
    //--------------------------------------------------------------------------
    
    public static boolean addPo(String po, LocalDate org, LocalDate cur, LocalDate eta, LocalDate fu){
        PreparedStatement pst = null;
        try {
            pst =conDB().prepareStatement("INSERT INTO "
            + "purchase_orders(purchase_order, original_ship_date, current_ship_date, eta_bms, fu_date) " 
            + "values(?,?,?,?,?);");
            pst.setString(1, po);
            pst.setDate(2, java.sql.Date.valueOf(org));
            pst.setDate(3, java.sql.Date.valueOf(cur));
            pst.setDate(4, java.sql.Date.valueOf(eta));
            if(fu != null)
                pst.setDate(5, java.sql.Date.valueOf(fu));   
            else
                pst.setDate(5, null);  
            
            return (pst.executeUpdate() == 1);
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }        
    
    //--------------------------------------------------------------------------
    
    public static boolean parameterExists(String parameter){
        
        PreparedStatement pst = null;
        
        try {
            pst = conDB().prepareStatement("SELECT PARAMETER\n" +
                    "FROM PARAMETER\n" +
                    "WHERE PARAMETER = ?;");
            
            pst.setString(1, parameter);
            ResultSet rs = pst.executeQuery();
            
            if(rs.next()){
                return true;
            }else{
                System.out.println(parameter + " DOES NOT EXIST, ADDING TO PARAMETER" );
                    pst = 
                    conDB().prepareStatement("INSERT INTO PARAMETER "
                            + "( PARAMETER )"
                            + "values(?);");
                    pst.setString(1, parameter);
                    return (pst.executeUpdate() == 1);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
    
    //--------------------------------------------------------------------------
    
    public static boolean supplierExists(String supplier){
        
        PreparedStatement pst = null;
        
        try {
            pst = conDB().prepareStatement("SELECT supplier_id FROM suppliers WHERE supplier_id = ?;");
            pst.setString(1, supplier);
            
            ResultSet rs = pst.executeQuery();
            
            if(rs.next()){
                return true;
            }else{
                    pst =conDB().prepareStatement("INSERT INTO suppliers "
                            + "( supplier_id )"
                            + "values(?);");
                    pst.setString(1, supplier);
                    return ( pst.executeUpdate() == 1 );
            }
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    //--------------------------------------------------------------------------
    
    public static boolean bearingsExists(String bearing, String parameter, String supplier){
        PreparedStatement pst = null;
        
        try {
            pst = conDB().prepareStatement("SELECT brg_name FROM bearings WHERE brg_name = ? "
                    + "AND PARAMETER = ? AND supplier_id = ?;");
            pst.setString(1, bearing);
            pst.setString(2, parameter);
            pst.setString(3, supplier);
                
            ResultSet rs = pst.executeQuery();
            
            if(rs.next())
                return true;
            else{
                
                pst = 
                    conDB().prepareStatement("INSERT INTO bearings "
                            + "( brg_name, PARAMETER, supplier_id )"
                            + "values(?,?,?);");
                pst.setString(1, bearing);
                pst.setString(2, parameter);
                pst.setString(3, supplier);
                return pst.executeUpdate() == 1;
            }
            
            
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
    
    //--------------------------------------------------------------------------
    
    public static int getBrgId(String bearing, String parameter, String supplier){
        PreparedStatement pst = null;
        try {
            pst = conDB().prepareStatement("SELECT brg_id FROM bearings "
                    + "WHERE brg_name = ?  AND "
                    + "PARAMETER = ? AND "
                    + "supplier_id = ?;");
            
            pst.setString(1, bearing);
            pst.setString(2, parameter);
            pst.setString(3, supplier);
            
            ResultSet rs = pst.executeQuery();
            
            if(rs.next())
                return rs.getInt("brg_id");
            else
                return -1;
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }
    
    //--------------------------------------------------------------------------
    
    public static boolean insertOrderDetails(String purchase_order, int brg_id, String quantity, String landing_cost,
                            String invoice_price, String confirmed){
        
        PreparedStatement pst = null;
        try {
            pst =conDB().prepareStatement("INSERT INTO order_details "
                    + "( purchase_order, brg_id, quantity, landed_cost, invoice_price, confirmed )"
                    + " values(?,?,?,?,?,?);");
            pst.setString(1, purchase_order);
            pst.setInt(2, brg_id);
            pst.setString(3, quantity);
            pst.setString(4, landing_cost);
            pst.setString(5, invoice_price);
            pst.setString(6,confirmed);
            
            return (pst.executeUpdate() == 1);
            
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
    
    //--------------------------------------------------------------------------
    
    public static boolean updateOrderDetailsATTACHMENTS(Attachment[] attachments, String purchase_order){
        
        PreparedStatement pst = null;
        
        try {
            pst = conDB().prepareStatement("UPDATE order_details SET attachments = ? WHERE purchase_order = ?;");
            pst.setObject(1, attachments);
            pst.setString(2, purchase_order);
            
            
            return ( pst.executeUpdate() == 1 );
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
    
    //##########################################################################
    //                  END ADD METHODS
    //##########################################################################
    //                  EDIT PO METHODS
    //##########################################################################    
    private static final String UPDATEpurchase_orders = "UPDATE purchase_orders SET "
        + "purchase_order = ?, "
        + "original_ship_date = ?, "
        + "current_ship_date = ?, "
        + "eta_bms = ?, "
        + "fu_date = ? "
        + "WHERE "
        + "purchase_order = ?;";
    public static boolean updatePurchaseOrders(String purchase_order, LocalDate org, LocalDate cur, LocalDate eta,LocalDate fu ){
        
        PreparedStatement pst = null;
        
        try {
            pst = conDB().prepareStatement(UPDATEpurchase_orders);
            
            pst.setString(1, purchase_order);
            pst.setDate(2, java.sql.Date.valueOf(org));
            pst.setDate(3, java.sql.Date.valueOf(cur));
            pst.setDate(4, java.sql.Date.valueOf(eta));
            if(fu != null)
                pst.setDate(5, java.sql.Date.valueOf(fu));
            else
                pst.setDate(5, null);
            
            pst.setString(6, purchase_order);
            
            return ( pst.executeUpdate() == 1) ;
            
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    private static final String UPDATEorder_details = "UPDATE order_details SET "
        + "order_details.brg_id = ?, "
        + "order_details.quantity = ?, "
        + "order_details.landed_cost = ?, "
        + "order_details.invoice_price = ?, "
        + "order_details.confirmed = ? "
        + "WHERE "
        + "order_details.purchase_order = ?;" ;
    public static boolean updateOrderDetails(int brg_id, String quantity, String landed_cost, String invoice_price,
            String confirmed, String purchase_order){
        
        PreparedStatement pst = null;
        
        try {
            pst = conDB().prepareStatement(UPDATEorder_details);
            pst.setInt(1, brg_id);
            pst.setString(2, quantity);
            pst.setString(3, landed_cost);
            pst.setString(4, invoice_price);
            pst.setString(5, confirmed);
            pst.setString(6, purchase_order);
            
            return ( pst.executeUpdate() == 1 );
                
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
    
    public static boolean deleteFromDatabase(String purchase_order){
        
        PreparedStatement pst = null;
        
        try {
            pst = conDB().prepareStatement("DELETE FROM purchase_orders WHERE purchase_order = ?;");
            pst.setString(1, purchase_order);
            
            return ( pst.executeUpdate() == 1 );
            
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
    
}
