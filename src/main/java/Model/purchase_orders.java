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
public class purchase_orders {
    
    //TABLE purchase_orders
    private String purchase_order;
    private String original_ship_date;
    private String current_ship_date;
    private String eta_bms;
    private String fu_date;
    private String fu_notes;

    
    
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
    
            
}
