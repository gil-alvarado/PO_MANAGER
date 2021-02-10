/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.Serializable;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import javafx.beans.property.BooleanProperty;
import net.ucanaccess.complex.Attachment;

/**
 *
 * @author Gilbert Alvarado
 */

/*
SELECT 
po.purchase_order, po.original_ship_date, 
po.current_ship_date, po.eta_bms, po.fu_date, 

od.quantity, od.landed_cost, od.invoice_price, 
od.confirmed, od.attachments, 

brg.brg_name, brg.PARAMETER, brg.supplier_id,

note.rw_comments, note.fu_notes

FROM (purchase_orders AS po 
INNER JOIN (bearings AS brg
INNER JOIN order_details AS od
    ON brg.[brg_id] = od.[brg_id]) ON po.[purchase_order] = od.[purchase_order]) 
INNER JOIN po_notes AS note ON po.[purchase_order] = note.[purchase_order];
*/



public class BMSPurchaseOrderModel implements Serializable{
    
    private String purchase_order, supplier, confirmed, invoice_price, brg_number;
    private String parameter, quantity, landing_cost;
    private Date org_ship_date, current_ship_date, eta_ship_date, fu_ship_date;
    private String orgDateFormat, curDateFormat, etaDateFormat,fuDateFormat;

    private Attachment[] attachments;//change to array
    private int attachmentLength;
    private boolean packet;
    private BooleanProperty registered;

    
    
//    private static final DecimalFormat LCformat = new DecimalFormat("$###,###,#00.00");
//    private static final DecimalFormat IPformat = new DecimalFormat("$###,###,#00.000");
    private static final SimpleDateFormat MMddyyyy = new SimpleDateFormat("MM/dd/yyyy");
       
    public BMSPurchaseOrderModel(String purchase_order, String supplier, 
            String confirmed, String invoice_price, String brg_number, 
            String parameter, String quantity, String landing_cost, 
            Date original_ship, Date current_ship, Date eta_ship, Date fu_ship, 
            Attachment []attachments) {
        
        this.purchase_order = purchase_order;
        this.supplier = supplier;
        this.confirmed = (confirmed.equals("FALSE")) ? "NO" : "YES";
        this.invoice_price = invoice_price;
        this.brg_number = brg_number;
        this.parameter = parameter;
        this.quantity = quantity;
        this.landing_cost = landing_cost;
        this.org_ship_date = original_ship;
        this.current_ship_date = current_ship;
        this.eta_ship_date = eta_ship;
        this.fu_ship_date = fu_ship;
        
        this.orgDateFormat = (original_ship != null) ? MMddyyyy.format(original_ship) : "N/A";
        this.curDateFormat = (current_ship != null )? MMddyyyy.format(current_ship) : "N/A";
        this.etaDateFormat = (eta_ship != null ) ? MMddyyyy.format(eta_ship) : "N/A";
        this.fuDateFormat = (fu_ship != null) ? MMddyyyy.format(fu_ship) : "N/A";
        
        this.attachments = attachments;
        this.attachmentLength = (attachments == null) ? 0 : attachments.length;
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
    
    //##########################################################################
    //COSTS

    public String getLanding_cost() {
        return this.landing_cost;
    }

    public void setLanding_cost(String landing_cost) {
        this.landing_cost = landing_cost;
    }
    
    public String getInvoice_price() {
        return this.invoice_price;
    }

    public void setInvoice_price(String invoice_price) {
        this.invoice_price = invoice_price;
    }

    //##########################################################################
    //DATE METHODS
    
    public Date getOrg_ship_date() {
        return this.org_ship_date;
    }
    public String getOrgShipDateFormat(){
        return MMddyyyy.format(this.org_ship_date);
    }
    public void setOrg_ship_date(Date org_ship_date) {
        this.org_ship_date = org_ship_date;
    }
    //----------------------------------------------------
    public Date getCurrent_ship_date() {
        return this.current_ship_date;
    }
    public String getCurrentShipDateFormat(){
        return MMddyyyy.format(this.current_ship_date);
    }
    
    public void setCurrent_ship_date(Date current_ship_date) {
        this.current_ship_date = current_ship_date;
    }
    //----------------------------------------------------
    public Date getEta_ship_date() {
        return this.eta_ship_date;
    }
    public String getEtaShipDateFormat(){
        return MMddyyyy.format(this.eta_ship_date);
    }
    
    public void setEta_ship_date(Date eta_ship_date) {
        this.eta_ship_date = eta_ship_date;
    }
    //----------------------------------------------------
    public Date getFu_ship_date() {
        return this.fu_ship_date;
    }
    public String getFuShipDateFormat(){
        return MMddyyyy.format(this.fu_ship_date);
    }
    public void setFu_ship_date(Date fu_ship_date) {
        this.fu_ship_date = fu_ship_date;
    }

    //--------------------------------------------------------------------------
    
    public String getAttachmentLength(){
        return String.valueOf(attachmentLength);
    }
    public Attachment []getAttachments() {
        return attachments;
    }

    public void setAttachments(Attachment[] attachments) {
        this.attachments = attachments;
    }

    public String getOrgDateFormat() {
        return orgDateFormat;
    }

    public void setOrgDateFormat(String orgDateFormat) {
        this.orgDateFormat = orgDateFormat;
    }

    public String getCurDateFormat() {
        return curDateFormat;
    }

    public void setCurDateFormat(String curDateFormat) {
        this.curDateFormat = curDateFormat;
    }

    public String getEtaDateFormat() {
        return etaDateFormat;
    }

    public void setEtaDateFormat(String etaDateFormat) {
        this.etaDateFormat = etaDateFormat;
    }

    public String getFuDateFormat() {
        return fuDateFormat;
    }

    public void setFuDateFormat(String fuDateFormat) {
        this.fuDateFormat = fuDateFormat;
    }
    
    @Override
    public String toString(){
        return this.purchase_order;
    }
    
}
