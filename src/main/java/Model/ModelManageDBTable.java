/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.File;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.ResultSet;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import net.ucanaccess.complex.Attachment;
import net.ucanaccess.complex.SingleValue;


/**
 *
 * @author Gilbert Alvarado
 */
public class ModelManageDBTable {
    
    private String po, brg, cur, supplier,p,attachment;
    private SingleValue[] svs;
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
    
    public void displayAttachmentCHANGE(){
        
//        try (Statement s = ConnectionUtil.conDB().createStatement()) {
//            try (ResultSet rs = s.executeQuery("SELECT Attachments FROM AttachTest WHERE ID=1")) {
//                rs.next();
//                Attachment[] atts = (Attachment[]) rs.getObject(1);  // net.ucanaccess.complex.Attachment
//                System.out.println("NUMBER OF ATTACHMENTS: " + att.length);
//                for (Attachment a : att) {
//                    System.out.println("File Name: " + a.getName());//FileNAME
//                    System.out.println("File Type: " + a.getType());//File Type
////                    org.apache.commons.io.FileUtils.writeByteArrayToFile(
////                            new File("C:/Users/Gord/Desktop/" + att.getName()), 
////                            att.getData());
//                }
//            }
//        } catch (SQLException ex) {   
//            Logger.getLogger(ModelManageDBTable.class.getName()).log(Level.SEVERE, null, ex);
//        }
        
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
//    public String getPacket() {
//        return p;
//    }
//
//    public void setPacket(String packet) {
//        this.p = packet;
//    }
    
//    public String getConfirmed() {
//        return confirmed;
//    }
//
//    public void setConfirmed(String confirmed) {
//        this.confirmed = confirmed;
//    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }
}
