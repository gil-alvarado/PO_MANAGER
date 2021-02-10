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
public class UserInfoModel {
    
    String first, last, user,role;
    private int role_id;
//    private int role; 
//    private String role;

    public UserInfoModel(String first, String last, String user, String role, int role_id) {
        this.first = first;
        this.last = last;
        this.user = user;
        this.role = role;
        
        this.role_id = role_id;
    }
/**
     * @return the first
     */
    public String getFirst() {
        return first;
    }
/**
     * @param first the first to set
     */
    public void setFirst(String first) {
        this.first = first;
    }
/**
     * @return the last
     */
    public String getLast() {
        return last;
    }
/**
     * @param last the last to set
     */
    public void setLast(String last) {
        this.last = last;
    }
/**
     * @return the user
     */
    public String getUser() {
        return user;
    }
/**
     * @param user the user to set
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * @return the role
     */
    public String getRole() {
        return role;
    }

    /**
     * @param role the role to set
     */
    public void setRole(String role) {
        this.role = role;
    }

    public int getRole_id() {
        return role_id;
    }

    public void setRole_id(int role_id) {
        this.role_id = role_id;
    }
    
    
}
