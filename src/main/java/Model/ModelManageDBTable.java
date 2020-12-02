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
public class ModelManageDBTable {
    
    private String confirmed,po, brg, cur, attachment,packet;

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

    public String getAttachment() {
        return attachment;
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
