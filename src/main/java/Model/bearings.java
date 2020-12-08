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
public class bearings {

    private String brg_id;
    private String brg_name;
    private String PARAMETER;
    private int supplier_id;
    
    
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

    public int getSupplier_id() {
        return supplier_id;
    }

    public void setSupplier_id(int supplier_id) {
        this.supplier_id = supplier_id;
    }
    
    
    
}
