/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import net.ucanaccess.complex.Attachment;

/**
 *
 * @author Gilbert Alvarado
 */
public class ModelDATA {
    
    //TABLE purchase_orders
    private String purchase_order;
    private String original_ship_date;
    private String current_ship_date;
    private String eta_bms;
    private String fu_date;
    private String fu_notes;

    //TABLE order_details
    private int order_details_id;
    //private String purchase_order;
    //private String brg_id;
    private String quantity;
    private double landed_cost;
    private double invoice_price;
    private String confirmed;
    private Attachment []attachments;
    private String moved_to_packet;
    
    //TABLE bearings
    private String brg_id;
    private String brg_name;
    //private String PARAMETER;
    //private int supplier_id;
    
    //TABLE PARAMETER
    private String PARAMETER;
    private String description;
    
    //TABLE supplier
    private int supplier_id;
    private String supplier_name;
    
    public ModelDATA(String purchase_order, String original_ship_date, String current_ship_date, String eta_bms, String fu_date, String fu_notes, int order_details_id, String quantity, double landed_cost, double invoice_price, String confirmed, Attachment[] attachments, String moved_to_packet, String brg_id, String brg_name, String PARAMETER, String description, int supplier_id, String supplier_name) {
        this.purchase_order = purchase_order;
        this.original_ship_date = original_ship_date;
        this.current_ship_date = current_ship_date;
        this.eta_bms = eta_bms;
        this.fu_date = fu_date;
        this.fu_notes = fu_notes;
        this.order_details_id = order_details_id;
        this.quantity = quantity;
        this.landed_cost = landed_cost;
        this.invoice_price = invoice_price;
        this.confirmed = confirmed;
        this.attachments = attachments;
        this.moved_to_packet = moved_to_packet;
        this.brg_id = brg_id;
        this.brg_name = brg_name;
        this.PARAMETER = PARAMETER;
        this.description = description;
        this.supplier_id = supplier_id;
        this.supplier_name = supplier_name;
    }

    public String getPurchase_order() {
        return purchase_order;
    }

    public void setPurchase_order(String purchase_order) {
        this.purchase_order = purchase_order;
    }

    public String getOriginal_ship_date() {
        return original_ship_date;
    }

    public void setOriginal_ship_date(String original_ship_date) {
        this.original_ship_date = original_ship_date;
    }

    public String getCurrent_ship_date() {
        return current_ship_date;
    }

    public void setCurrent_ship_date(String current_ship_date) {
        this.current_ship_date = current_ship_date;
    }

    public String getEta_bms() {
        return eta_bms;
    }

    public void setEta_bms(String eta_bms) {
        this.eta_bms = eta_bms;
    }

    public String getFu_date() {
        return fu_date;
    }

    public void setFu_date(String fu_date) {
        this.fu_date = fu_date;
    }

    public String getFu_notes() {
        return fu_notes;
    }

    public void setFu_notes(String fu_notes) {
        this.fu_notes = fu_notes;
    }

    public int getOrder_details_id() {
        return order_details_id;
    }

    public void setOrder_details_id(int order_details_id) {
        this.order_details_id = order_details_id;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public double getLanded_cost() {
        return landed_cost;
    }

    public void setLanded_cost(double landed_cost) {
        this.landed_cost = landed_cost;
    }

    public double getInvoice_price() {
        return invoice_price;
    }

    public void setInvoice_price(double invoice_price) {
        this.invoice_price = invoice_price;
    }

    public String getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(String confirmed) {
        this.confirmed = confirmed;
    }

    public Attachment[] getAttachments() {
        return attachments;
    }

    public void setAttachments(Attachment[] attachments) {
        this.attachments = attachments;
    }

    public String getMoved_to_packet() {
        return moved_to_packet;
    }

    public void setMoved_to_packet(String moved_to_packet) {
        this.moved_to_packet = moved_to_packet;
    }

    public String getBrg_id() {
        return brg_id;
    }

    public void setBrg_id(String brg_id) {
        this.brg_id = brg_id;
    }

    public String getBrg_name() {
        return brg_name;
    }

    public void setBrg_name(String brg_name) {
        this.brg_name = brg_name;
    }

    public String getPARAMETER() {
        return PARAMETER;
    }

    public void setPARAMETER(String PARAMETER) {
        this.PARAMETER = PARAMETER;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSupplier_id() {
        return supplier_id;
    }

    public void setSupplier_id(int supplier_id) {
        this.supplier_id = supplier_id;
    }

    public String getSupplier_name() {
        return supplier_name;
    }

    public void setSupplier_name(String supplier_name) {
        this.supplier_name = supplier_name;
    }
    
}
