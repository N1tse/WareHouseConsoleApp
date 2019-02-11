/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test_db2;

public class Product {
       int product_id;
       String code;
       String product_name;
       int current_stock;
       String desc;
       String brand;
       String category;
       
    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public int getCurrent_stock() {
        return current_stock;
    }

    public void setCurrent_stock(int current_stock) {
        this.current_stock = current_stock;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }
       
    
    public String toString(){
        return "PRODUCT_ID:"+product_id+"|| PRODUCT_CODE: "+code+"|| PRODUCT_NAME: "+product_name+"|| BRAND: "+brand+"|| DESCRIPTION: "+desc+"|| CURRENT_STOCK: "+current_stock+"\n";
    }
       
}
