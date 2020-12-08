/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.File;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.ResultSet;
import net.ucanaccess.complex.Attachment;
import net.ucanaccess.complex.SingleValue;


/**
 *
 * @author Gilbert Alvarado
 */
public class ModelManageDBTable {
    
    private String confirmed,po, brg, cur, attachment,packet;
    private SingleValue[] svs;
    private Attachment att[];
    
    
    public ModelManageDBTable(String confirmed, String po, String brg, String cur, String attachment, String packet) {
        this.confirmed = confirmed;
        this.po = po;
        this.brg = brg;
        this.cur = cur;
        this.attachment = attachment;
        this.packet = packet;
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
                System.out.println("NUMBER OF ATTACHMENTS: " + att.length);
                for (Attachment a : att) {
                    System.out.println("File Name: " + a.getName());//FileNAME
                    System.out.println("File Type: " + a.getType());//File Type
//                    org.apache.commons.io.FileUtils.writeByteArrayToFile(
//                            new File("C:/Users/Gord/Desktop/" + att.getName()), 
//                            att.getData());
                }
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

    public String getPacket() {
        return packet;
    }

    public void setPacket(String packet) {
        this.packet = packet;
    }

    public String getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(String confirmed) {
        this.confirmed = confirmed;
    }
}
