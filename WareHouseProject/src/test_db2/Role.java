/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test_db2;


public class Role {
    private int role_id;
    private String role;

    public int getRole_id() {
        return role_id;
    }

    public void setRole_id(int role_id) {
        this.role_id = role_id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    
    public  void SuperAdminMenu(){
                System.out.println("");
                System.out.println("");
                System.out.println("************************************************************|");
                System.out.println("|                       SUPER ADMIN                         |");
                System.out.println("************************************************************|");
                System.out.println("|                 PRESS  NUMBER FOR ACTION                  |");
                System.out.println("____________________________________________________________|");
                System.out.println("|1 - DISPLAY ALL USERS      ||  2 - DISPLAY ALL TRANSACTIONS|");
                System.out.println("|3 - UPDATE A USER          ||  4 - SEARCH FOR USER         |");
                System.out.println("|5 - SEARCH FOR TRANSACTION ||  6 - DELETE A USER           |");
                System.out.println("|7 - DELETE PROFILE         ||  8 - EXIT                    |");
                System.out.println("|___________________________________________________________|");
                System.out.println("");
                System.out.println("");
    }
    public void adminMenu(){
                System.out.println("");
                System.out.println("");
                System.out.println("*********************************************************************|");
                System.out.println("|                              ADMIN                                 |");
                System.out.println("*********************************************************************|");
                System.out.println("|                      PRESS  NUMBER FOR ACTION                      |");
                System.out.println("_____________________________________________________________________|");
                System.out.println("| 1 - DISPLAY ALL PRODUCTS        || 2 - DISPLAY ALL TRANSACTIONS    |");
                System.out.println("| 3 - MAKE A TRANSACTION          || 4 - ADD NEW PRODUCT TO WAREHOUSE|");
                System.out.println("| 5 - SEARCH PRODUCT WITH KEYWORD || 6 - DELETE A PRODUCT            |");
                System.out.println("| 7 - UPDATE A PRODUCT            || 8 - LOW STOCK PRODUCTS          |");
                System.out.println("| 9 - DELETE PROFILE              || 10 - EXIT                       |");
                System.out.println("|____________________________________________________________________|");
                System.out.println("");
                System.out.println("");
    }
        public void userMenu(){
                System.out.println("");
                System.out.println("");
                System.out.println("***********************************************************8*|");
                System.out.println("|                        USER                                |");
                System.out.println("*************************************************************|");
                System.out.println("|                 PRESS  NUMBER FOR ACTION                   |");
                System.out.println("_____________________________________________________________|");
                System.out.println("| 1 - DISPLAY ALL PRODUCTS || 2 - DISPLAY YOUR TRANSACTIONS  |");
                System.out.println("| 3 - MAKE A TRANSACTION   || 4 - SEARCH PRODUCT WITH KEYWORD|");
                System.out.println("| 5 - UPDATE YOUR INFO     || 6 - DELETE YOUR PROFILE        |");
                System.out.println("|                       7 - EXIT                             |");
                System.out.println("|____________________________________________________________|");
                System.out.println("");
                System.out.println("");
    }
    
}
