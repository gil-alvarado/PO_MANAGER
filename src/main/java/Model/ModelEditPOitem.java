/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import static Model.ConnectionUtil.conDB;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import net.ucanaccess.complex.Attachment;

/**
 *
 * @author Gilbert Alvarado
 */
public class ModelEditPOitem {
    
    private String purchase_order, supplier, confirmed, invoice_price, brg_number;
    private String parameter, quantity, landing_cost;
    private Date original_ship, current_ship, eta_ship, fu_ship;
    private Attachment attachments;
    
    private LinkedHashMap<String, Object> data;
    
    
    
    public ModelEditPOitem(String purchase_order) {
        this.purchase_order = purchase_order;
        populateFields();
    }

    public String getPurchase_order() {
        return purchase_order;
    }

    public void setPurchase_order(String purchase_order) {
        this.purchase_order = purchase_order;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(String confirmed) {
        this.confirmed = confirmed;
    }

    public String getInvoice_price() {
        return invoice_price;
    }

    public void setInvoice_price(String invoice_price) {
        this.invoice_price = invoice_price;
    }

    public String getBrg_number() {
        return brg_number;
    }

    public void setBrg_number(String brg_number) {
        this.brg_number = brg_number;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getLanding_cost() {
        return landing_cost;
    }

    public void setLanding_cost(String landing_cost) {
        this.landing_cost = landing_cost;
    }

    public Date getOriginal_ship() {
        return original_ship;
    }

    public void setOriginal_ship(Date original_ship) {
        this.original_ship = original_ship;
    }

    public Date getCurrent_ship() {
        return current_ship;
    }

    public void setCurrent_ship(Date current_ship) {
        this.current_ship = current_ship;
    }

    public Date getEta_ship() {
        return eta_ship;
    }

    public void setEta_ship(Date eta_ship) {
        this.eta_ship = eta_ship;
    }

    public Date getFu_ship() {
        return fu_ship;
    }

    public void setFu_ship(Date fu_ship) {
        this.fu_ship = fu_ship;
    }

    public Attachment getAttachments() {
        return attachments;
    }

    public void setAttachments(Attachment attachments) {
        this.attachments = attachments;
    }
     private static final String getAllQuery = 
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
    private void populateFields(){
        
//        ArrayList<String> column_names = ConnectionUtil.columnNames(purchase_order);
        
        
        try{
            PreparedStatement pst = conDB().prepareStatement(getAllQuery);
            pst.setString(1, purchase_order);
            
            ResultSet rs = pst.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            data = new LinkedHashMap<>();
            while(rs.next()){
                for(int i = 1; i <= rsmd.getColumnCount(); i++ ){
                    String name = rsmd.getColumnName(i);
                    data.put(rsmd.getColumnName(i), rs.getObject(name));
                    System.out.println("ADDED " +name + " TO MAP");
                }
            }
            
            
            
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    //##########################################################################
    //              EXECUTE STATEMENTS HERE
    //##########################################################################
    public Object getData(String column_name){
        return data.get(column_name);
    }
    
}
