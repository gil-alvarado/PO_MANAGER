/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.Serializable;
import java.sql.Date;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import net.ucanaccess.complex.Attachment;

/**
 *
 * @author Gilbert Alvarado
 */
public class ModelManageDBTable  {
        
    private String po, brg, cur, supplier,p,attachment;
    private Attachment att[];
    
    private boolean packet;
    private BooleanProperty registered;
    
    private Date originalDate;

    public Date getOriginalDate() {
        return originalDate;
    }
    
    public ModelManageDBTable(String po,String supplier, String brg, String cur,  String packet, Date date) {
//        this.confirmed = confirmed;
        this.po = po;
        
        this.supplier = supplier;
        
        this.brg = brg;
        this.cur = cur;
//        this.attachment = attachment;
//        this.att = attachment;
        this.p = packet;
        this.packet = (packet.equals("YES"));
        this.registered = new SimpleBooleanProperty(this.packet);
        
        this.originalDate = date;
    }
    public String getPo() {
        return po;
    }

    public void setPo(String po) {
        this.po = po;
    }

    public String getBrg() {
        return brg;
    }

    public void setBrg(String brg) {
        this.brg = brg;
    }

    public String getCur() {
        return cur;
    }

    public void setCur(String cur) {
        this.cur = cur;
    }

    //import net.ucanaccess.complex.Attachment;
    public String getAttachment() {
        
        
        return attachment;
    }
    public void setAtt(Attachment []att){
        this.att = att;
    }
    
   
    public int getNumberAttachments(){
        return this.att.length;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public boolean getPacket() {
        return packet;
    }

    public BooleanProperty registeredProperty() { return registered; }
    
    public void setPacket(boolean packet) {
        this.packet = packet;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }
    
    public String toString(){
        return this.po;
    }
}
