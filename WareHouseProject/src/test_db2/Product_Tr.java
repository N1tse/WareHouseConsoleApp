
package test_db2;

import java.sql.Date;

public class Product_Tr {
    private String date;
    private int quantity,id;
    private String product_name,description,brand,code;
    private String fname,lname,role;
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }
    

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }
    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    
    public String toString(){
        return "TRANSACTION_ID:"+id+"|| DATE: "+date+"|| QUANTITY: "+quantity+"|| PRODUCT_CODE: "+code+"|| DESCRIPTION: "+description+"|| BRAND: "+brand+"|| MADE_BY: "+fname+" "+lname+"|| ROLE: "+role+"\n";
    }
}
