/**
 *
 * @author Gilbert Alvarado
 */
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
public class ModelOverviewTable {
    
    
    private String confirmed,po,brg,supplier,cur,da,funotes;

    private String ip,lc;
    
    public ModelOverviewTable
        (String confirmed,
            String po,
            String brg, 
            String supplier, 
             String cur,
             String date,
             String funotes,
            String ip, String lc) {
        
        //orderID,<--EXCLUDE 
        //PO,brg, supplier, parameter, confirmed, qty, ip,lc,org ship, eta, curShip, fu
        this.confirmed = confirmed;
        this.po = po;
        this.brg = brg;
        this.supplier = supplier;
        this.cur = cur;
        this.da = date;
        this.funotes = funotes;
        
        this.ip = ip;
        this.lc = lc;
    }
    

    public ModelOverviewTable() {
        
    }

    /**
     * @return the confirmed
     */
    public String getConfirmed() {
        return confirmed;
    }

    /**
     * @param confirmed the confirmed to set
     */
    public void setConfirmed(String confirmed) {
        this.confirmed = confirmed;
    }
    
    /**
     * @return the po
     */
    public String getPo() {
        return po;
    }

    /**
     * @param po the po to set
     */
    public void setPo(String po) {
        this.po = po;
    }

    /**
     * @return the brg
     */
    public String getBrg() {
        return brg;
    }

    /**
     * @param brg the brg to set
     */
    public void setBrg(String brg) {
        this.brg = brg;
    }
    

    /**
     * @return the supplier
     */
    public String getSupplier() {
        return supplier;
    }

    /**
     * @param supplier the supplier to set
     */
    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }
    
    /**
     * @return the cur
     */
    public String getCur() {
        return cur;
    }

    /**
     * @param cur the cur to set
     */
    public void setCur(String cur) {
        this.cur = cur;
    }
    
    /**
     * @return the da
     */
    public String getDa() {
        return da;
    }

    /**
     * @param da the lc to set
     */
    public void setDa(String da) {
        this.da = da;
    }

    /**
     * @return the fu
     */
    public String getFunotes() {
        return funotes;
    }

    /**
     * @param funotes the fu to set
     */
    public void setFunotes(String funotes) {
        this.funotes = funotes;
    } 
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getLc() {
        return lc;
    }

    public void setLc(String lc) {
        this.lc = lc;
    }
}
