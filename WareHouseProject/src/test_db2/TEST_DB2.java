
package test_db2;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
//import static test_db2.Role.SuperAdminMenu;

public class TEST_DB2 {

    public static void main(String[] args){
        database db = new database();
        Scanner scanner = new Scanner(System.in);
        int g;
        boolean entry = false;
        System.out.println("");
        System.out.println("                 WELCOME");
        System.out.println("");
        
        //return what role is the user
           String role = null;
    while(entry == false){
        System.out.println("");
        System.out.println("PRESS 1 FOR LOGIN || PRESS 2 FOR REGISTER || PRESS 3 FOR EXIT");
        System.out.println("");
      if(scanner.hasNextInt()){
        g = scanner.nextInt();
       if(g == 1){
        try {
            boolean flagR = false;
            role = db.login();
            if(role==null){
                while(flagR == false){
                    System.out.println("");
                        System.out.println("YOU ARE NOT REGISTER!");
                        System.out.println("IF YOU WANT TO REGISTER PRESS 1 OR PRESS 2 FOR LOGIN");
                        System.out.println("");
                        if(scanner.hasNextInt()){
                            switch (scanner.nextInt()) {
                                case 1:
                                    role = db.register();
                                    flagR = true;
                                    break;
                                case 2:
                                    role = db.login();
                                    flagR = true;
                                    break;
                                default:
                                    System.out.println("");
                                    System.out.println("WRONG INPUT");
                                    System.out.println("");
                                    String temp = scanner.next();
                                    flagR = false;
                                    break;
                            }
                        }else{
                            System.out.println("");
                            System.out.println("WRONG INPUT");
                            System.out.println("");
                            String temp = scanner.next();
                            flagR = false;
                        }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(TEST_DB2.class.getName()).log(Level.SEVERE, null, ex);
        }

        
        switch(role){
            case "S_ADMIN":
                Role sadmin = new Role();
                boolean flag = false;
                    while(flag==false){
                    sadmin.SuperAdminMenu();
                    if(!scanner.hasNextInt()){
                        System.out.println("");
                        System.out.println("NOT A VALID ENTRY");
                        System.out.println("");
                        String cont = scanner.next();
                    }else if(scanner.hasNextInt()){
                    int option = scanner.nextInt();
                        if(option==1){
                            ArrayList <User> users = db.allUsers();
                            System.out.println(users);
                            System.out.println("");
                            System.out.println("IF YOU WANT TO GO BACK TO MENU PRESS MENU!\nIF YOU WANT TO EXIT PRESS EXIT!");
                            String cont = scanner.next().toUpperCase();
                            if(cont.equals("MENU")){
                                flag = false;
                            }else if(cont.equals("EXIT")){
                                flag = true;
                            }
                        }else if(option == 2){
                           db.allTransactions();
                            System.out.println("");
                            System.out.println("IF YOU WANT TO GO BACK TO MENU PRESS MENU!\nIF YOU WANT TO EXIT PRESS EXIT!");
                            String cont = scanner.next().toUpperCase();
                            if(cont.equals("MENU")){
                                flag = false;
                            }else if(cont.equals("EXIT")){
                                flag = true;
                            }
                        }else if(option == 3){
                            db.updateUser();
                            System.out.println("");
                            System.out.println("IF YOU WANT TO GO BACK TO MENU PRESS MENU!\nIF YOU WANT TO EXIT PRESS EXIT!");
                            String cont = scanner.next().toUpperCase();
                            if(cont.equals("MENU")){
                                flag = false;
                            }else if(cont.equals("EXIT")){
                                flag = true;
                            }
                        }else if(option == 4){
                            db.searchUserSadmin();
                            System.out.println("");
                            System.out.println("IF YOU WANT TO GO BACK TO MENU PRESS MENU!\nIF YOU WANT TO EXIT PRESS EXIT!");
                            String cont = scanner.next().toUpperCase();
                            if(cont.equals("MENU")){
                                flag = false;
                            }else if(cont.equals("EXIT")){
                                flag = true;
                            }
                        }else if(option == 5){
                            db.searchTransactionSAdmin();
                            System.out.println("");
                            System.out.println("IF YOU WANT TO GO BACK TO MENU PRESS MENU!\nIF YOU WANT TO EXIT PRESS EXIT!");
                            String cont = scanner.next().toUpperCase();
                            if(cont.equals("MENU")){
                                flag = false;
                            }else if(cont.equals("EXIT")){
                                flag = true;
                            }
                        }else if(option == 6){
                            db.deleteUser();
                            System.out.println("");
                            System.out.println("IF YOU WANT TO GO BACK TO MENU PRESS MENU!\nIF YOU WANT TO EXIT PRESS EXIT!");
                            String cont = scanner.next().toUpperCase();
                            if(cont.equals("MENU")){
                                flag = false;
                            }else if(cont.equals("EXIT")){
                                flag = true;
                            }
                        }else if(option == 7){
                             db.deleteProfile();
                             flag = true;
                        }else if(option == 8){
                            flag = true;
                        }else{
                            System.out.println("");
                            System.out.println("WRONG INPUT");
                        }
                    }
                    }
                break;  
            case "ADMIN":
                Role admin = new Role();
                boolean flagAdmin= false;
                while(flagAdmin==false){
                    admin.adminMenu();
                if(!scanner.hasNextInt()){
                        System.out.println("");
                        System.out.println("NOT A VALID ENTRY");
                        String cont = scanner.next();
                    }else if(scanner.hasNextInt()){
                    int option = scanner.nextInt();
                    if(option==1){
                        db.allProducts();
                        System.out.println("");
                        System.out.println("IF YOU WANT TO GO BACK TO MENU PRESS MENU!\nIF YOU WANT TO EXIT PRESS EXIT!");
                            String cont = scanner.next().toUpperCase();
                            if(cont.equals("MENU")){
                                flagAdmin = false;
                            }else if(cont.equals("EXIT")){
                                flagAdmin = true;
                            }
                    }else if(option==2){
                        db.allTransactions();
                        System.out.println("");
                        System.out.println("IF YOU WANT TO GO BACK TO MENU PRESS MENU!\nIF YOU WANT TO EXIT PRESS EXIT!");
                            String cont = scanner.next().toUpperCase();
                            if(cont.equals("MENU")){
                                flagAdmin = false;
                            }else if(cont.equals("EXIT")){
                                flagAdmin = true;
                            }
                    }else if(option==3){
                        db.makeTransaction();
                        System.out.println("");
                        System.out.println("IF YOU WANT TO GO BACK TO MENU PRESS MENU!\nIF YOU WANT TO EXIT PRESS EXIT!");
                            String cont = scanner.next().toUpperCase();
                            if(cont.equals("MENU")){
                                flagAdmin = false;
                            }else if(cont.equals("EXIT")){
                                flagAdmin = true;
                            }
                         }else if(option==4){
                            db.newProduct();
                            System.out.println("");
                            System.out.println("IF YOU WANT TO GO BACK TO MENU PRESS MENU!\nIF YOU WANT TO EXIT PRESS EXIT!");
                                String cont = scanner.next().toUpperCase();
                                if(cont.equals("MENU")){
                                    flagAdmin = false;
                                }else if(cont.equals("EXIT")){
                                    flagAdmin = true;
                                }
                         }else if(option == 5){
                             //SEARCH FOR PRODUCT
                             db.searchProductAdmin();
                             System.out.println("");
                             System.out.println("IF YOU WANT TO GO BACK TO MENU PRESS MENU!\nIF YOU WANT TO EXIT PRESS EXIT!");
                                String cont = scanner.next().toUpperCase();
                                if(cont.equals("MENU")){
                                    flagAdmin = false;
                                }else if(cont.equals("EXIT")){
                                    flagAdmin = true;
                                }
                         }else if(option == 6){
                             db.deleteProduct();
                             System.out.println("");
                             System.out.println("IF YOU WANT TO GO BACK TO MENU PRESS MENU!\nIF YOU WANT TO EXIT PRESS EXIT!");
                                String cont = scanner.next().toUpperCase();
                                if(cont.equals("MENU")){
                                    flagAdmin = false;
                                }else if(cont.equals("EXIT")){
                                    flagAdmin = true;
                                }
                         }else if(option == 7){
                             db.updateProduct();
                             System.out.println("");
                             System.out.println("IF YOU WANT TO GO BACK TO MENU PRESS MENU!\nIF YOU WANT TO EXIT PRESS EXIT!");
                                String cont = scanner.next().toUpperCase();
                                if(cont.equals("MENU")){
                                    flagAdmin = false;
                                }else if(cont.equals("EXIT")){
                                    flagAdmin = true;
                                }
                         }else if(option == 8){
                             db.lowStockProducts();
                             System.out.println("");
                             System.out.println("IF YOU WANT TO GO BACK TO MENU PRESS MENU!\nIF YOU WANT TO EXIT PRESS EXIT!");
                                String cont = scanner.next().toUpperCase();
                                if(cont.equals("MENU")){
                                    flagAdmin = false;
                                }else if(cont.equals("EXIT")){
                                    flagAdmin = true;
                                }
                         }else if(option == 9){
                             db.deleteProfile();
                             flagAdmin = true;
                         }else if(option == 10){
                            flagAdmin = true;
                        }else{
                             System.out.println("");
                            System.out.println("WRONG INPUT");
                        }
                    }
                }
                break;                
            case "USER":
                 Role user = new Role();
                boolean flagUser= false;
                while(flagUser==false){
                    user.userMenu();
                if(!scanner.hasNextInt()){
                        System.out.println("");
                        System.out.println("NOT A VALID ENTRY");
                        String cont = scanner.next();
                    }else if(scanner.hasNextInt()){
                    int option = scanner.nextInt();
                    if(option==1){
                        db.allProductsUser();
                        System.out.println("");
                        System.out.println("IF YOU WANT TO GO BACK TO MENU PRESS MENU!\nIF YOU WANT TO EXIT PRESS EXIT!");
                            String cont = scanner.next().toUpperCase();
                            if(cont.equals("MENU")){
                                flagUser = false;
                            }else if(cont.equals("EXIT")){
                                flagUser = true;
                            }
                    }else if(option==2){
                        db.allTransactionsUser();
                        System.out.println("");
                        System.out.println("IF YOU WANT TO GO BACK TO MENU PRESS MENU!\nIF YOU WANT TO EXIT PRESS EXIT!");
                            String cont = scanner.next().toUpperCase();
                            if(cont.equals("MENU")){
                                flagUser = false;
                            }else if(cont.equals("EXIT")){
                                flagUser = true;
                            }
                            
                    }else if(option==3){
                        db.makeTransaction();
                        System.out.println("");
                        System.out.println("IF YOU WANT TO GO BACK TO MENU PRESS MENU!\nIF YOU WANT TO EXIT PRESS EXIT!");
                            String cont = scanner.next().toUpperCase();
                            if(cont.equals("MENU")){
                                flagUser = false;
                            }else if(cont.equals("EXIT")){
                                flagUser = true;
                            }
                    }else if(option==4){
                        db.searchProductUser();
                        System.out.println("");
                        System.out.println("IF YOU WANT TO GO BACK TO MENU PRESS MENU!\nIF YOU WANT TO EXIT PRESS EXIT!");
                            String cont = scanner.next().toUpperCase();
                            if(cont.equals("MENU")){
                                flagUser = false;
                            }else if(cont.equals("EXIT")){
                                flagUser = true;
                            }
                    }else if(option == 5){
                        db.updateUserInfo();
                        System.out.println("");
                        System.out.println("IF YOU WANT TO GO BACK TO MENU PRESS MENU!\nIF YOU WANT TO EXIT PRESS EXIT!");
                            String cont = scanner.next().toUpperCase();
                            if(cont.equals("MENU")){
                                flagUser = false;
                            }else if(cont.equals("EXIT")){
                                flagUser = true;
                            }
                    }else if(option == 6){
                        db.deleteProfile();
                        flagUser = true;
                    }else if(option==7){
                        flagUser = true;
                    }
                    }
        }
                break;
                
        
        
        
        
        

        
        
        
        
//        db.close();
    }
        entry = true;
    }else if(g == 2){
        db.register();
        entry = false;
    }else if(g == 3){
        entry = true;
    }else{
           System.out.println("");
           System.out.println("WRONG INPUT");
           entry = false;
    }
    }else{
          System.out.println("");
          System.out.println("WRONG INPUT");
          entry = false;
          String temp = scanner.next();
      }
    }
    }
    
}
