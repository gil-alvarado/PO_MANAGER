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
public class order_deatils {
    
    private int order_details_id;
    private String purchas_order;
    private String brg_id;
    private String quantity;
    private double landed_cost;
    private double invoice_price;
    private String confirmed;
    private Attachment []attachments;
    private String moved_to_packet;

    public int getOrder_details_id() {
        return order_details_id;
    }

    public void setOrder_details_id(int order_details_id) {
        this.order_details_id = order_details_id;
    }

    public String getPurchas_order() {
        return purchas_order;
    }

    public void setPurchas_order(String purchas_order) {
        this.purchas_order = purchas_order;
    }

    public String getBrg_id() {
        return brg_id;
    }

    public void setBrg_id(String brg_id) {
        this.brg_id = brg_id;
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
    
    
}
