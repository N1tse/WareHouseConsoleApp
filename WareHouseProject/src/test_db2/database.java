/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test_db2;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
//import static test_db2.Role.SuperAdminMenu;

public class database {
    
    public static final String DB_NAME = "warehouse";
    public static final String url = "jdbc:mysql://localhost:3306/"+DB_NAME+"?zeroDateTimeBehavior=convertToNull";
    public static final String username = "root";
    public static final String pw ="nitse1990";
    
    public static final String TABLE_ROLE = "roles";
    public static final int COLUMN_ROLE_ID = 1;
    public static final int COLUMN_ROLES_ROLE = 2;
    
    public static final String TABLE_USER = "user";
    public static final String COLUMN_USER_ID ="user_id";
    public static final String COLUMN_USER_USERNAME = "username";
    public static final String COLUMN_USER_PW = "passward";
    public static final String COLUMN_USER_FIRSTNAME = "fname";
    public static final String COLUMN_USER_LASTNAME = "lname";
    public static final String COLUMN_USER_ROLE_ID = "role";
    
    private static final String TABLE_PRODUCT_TR = "product_tr";
    private static final String COLUMN_PRODUCT_TR_ID = "product_tr_id";
    private static final String COLUMN_PRODUCT_DATE = "DATE_TR";
    
    private Connection conn;
    private int returnedUser_id = 0;
    private String returnedRole = null;
    private String returnedFname = null;
    private String returnedLname = null;
    DateTimeFormatter prn = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
    LocalDateTime prnnow = LocalDateTime.now(); 
    
     
     public void writer(String action,String msg){
         String text = "log.txt";
         try{
             PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(text, true))); //the true will append the new data            
             writer.println("TIME: "+prn.format(prnnow)+" || ACTION: "+action+" || MESSAGE: "+msg+"\n");
             writer.close();
         }catch(FileNotFoundException | UnsupportedEncodingException e){
             System.out.println(e);
         } catch (IOException ex) {
            Logger.getLogger(database.class.getName()).log(Level.SEVERE, null, ex);
        }
     }
    //open connection
    public boolean open(){
    
        try{
            conn = DriverManager.getConnection(url,username,pw);
            System.out.println("Connected");
            return true;
        }catch(Exception e){System.out.println("Couldn't connect to DataBase, "+e);}
        return false;
    }
    
    //close connection
    public void close(){
        try{
            if(conn != null){
                conn.close();
                System.out.println("Disconnected!");
            }
        }catch(SQLException e){System.out.println("Couldn't close connection: "+e);}
    }
    
    //return all the ROLES
    public ArrayList<Role> allRoles(){
        try(Statement statement = conn.createStatement();
                ResultSet result = statement.executeQuery("SELECT * FROM"+TABLE_ROLE)){
            ArrayList listofRoles = new ArrayList<>();
            while(result.next()){
                Role role = new Role();
                role.setRole_id(result.getInt(COLUMN_ROLE_ID));
                role.setRole(result.getString(COLUMN_ROLES_ROLE));
                listofRoles.add(role);
            }
            return listofRoles;
        }catch(Exception e){
            System.out.println(e);
            return null;
        }
    }
    
    //RETURN ALL THE PRODUCTS_TRANSACTIONS
    public void allTransactions(){
        open();
        try{
            PreparedStatement q = conn.prepareStatement("SELECT PRODUCT_TR_ID,DATE_TR,QUANTITY,CODE,PRODUCT_NAME,DESCRIPTION,BRAND,FNAME,LNAME,ROLE FROM "+TABLE_PRODUCT_TR+" \n" +
                                                            "INNER JOIN PRODUCT ON "+TABLE_PRODUCT_TR+".PRODUCT_ID = PRODUCT.PRODUCT_ID \n" +
                                                            "INNER JOIN USER ON "+TABLE_PRODUCT_TR+".USER_ID = USER.USER_ID \n" +
                                                            "INNER JOIN ROLES ON USER.ROLE_ID = ROLES.ROLE ORDER BY PRODUCT_TR_ID;");
            ResultSet result = q.executeQuery();
            while(result.next()){
                Product_Tr p_tr = new Product_Tr();
                p_tr.setId(result.getInt(1));
                p_tr.setBrand(result.getString(7));
                p_tr.setCode(result.getString(4));
                p_tr.setQuantity(result.getInt(3));
                p_tr.setDate(result.getString(2));
                p_tr.setProduct_name(result.getString(5));
                p_tr.setFname(result.getString(8));
                p_tr.setLname(result.getString(9));
                p_tr.setRole(result.getString(10));
                p_tr.setDescription(result.getString(6));
                System.out.println(p_tr);  
            }
        }catch(SQLException e){
            System.out.println(e);
        }
        finally{
            close();
        }
    }
    
    
    
    //RETURN ALL THE USERS
    public ArrayList<User> allUsers(){
        open();
        try(Statement statement = conn.createStatement();
                ResultSet result = statement.executeQuery("SELECT distinct user_id,username,PASSWARD,fname,lname,ROLE FROM "+TABLE_USER+" inner join warehouse.roles on warehouse."+TABLE_ROLE+".ROLE_ID = warehouse."+TABLE_USER+".ROLE_ID;")){
            ArrayList <User> listofUsers = new ArrayList<>();
            while(result.next()){
                User user = new User();
                user.setUser_id(result.getInt(COLUMN_USER_ID));
                user.setUsername(result.getString(COLUMN_USER_USERNAME));
                user.setPw(result.getString(COLUMN_USER_PW));
                user.setFname(result.getString(COLUMN_USER_FIRSTNAME));
                user.setLname(result.getString(COLUMN_USER_LASTNAME));
                user.setRole_id(result.getString(COLUMN_USER_ROLE_ID));
                
               listofUsers.add(user);
            }
            return listofUsers;
        }catch(Exception e){
            System.out.println(e);
        return null;
        }
        finally{
            close();
        }
    }

        //RETURN USERS INFO
    public String returnUsersFname(){
        int id = returnedUser_id;
       try{
           open();
           PreparedStatement q = conn.prepareStatement("SELECT * FROM USER WHERE USER_ID="+id);
           ResultSet result = q.executeQuery();
           while(result.next()){
               User u = new User();
               u.setFname(result.getString(4));
               u.setLname(result.getString(5));
               returnedFname = u.getFname();
               returnedLname =u.getLname();
           }
           return returnedFname;
       }catch(SQLException e){
           System.out.println(e);
           return null;
       }finally{
           close();
       }
    }
        public String returnUsersLname(){
        int id = returnedUser_id;
       try{
           open();
           PreparedStatement q = conn.prepareStatement("SELECT * FROM USER WHERE USER_ID="+id);
           ResultSet result = q.executeQuery();
           while(result.next()){
               User u = new User();
               u.setFname(result.getString(4));
               u.setLname(result.getString(5));
               returnedFname = u.getFname();
               returnedLname =u.getLname();
           }
           return returnedLname;
       }catch(SQLException e){
           System.out.println(e);
           return null;
       }finally{
           close();
       }
    }
    public String returnChangedUserFname(int userid){
        String fname = null;
        try{
            open();
            PreparedStatement q = conn.prepareStatement("SELECT * FROM USER WHERE USER_ID="+userid);
            ResultSet result = q.executeQuery();
            while(result.next()){
                User u = new User();
                u.setFname(result.getString(4));
//                u.setLname(result.getString(5));
                fname = u.getFname();
            }
            return fname; 
        }catch(Exception e){
            System.out.println(e);
            return null;
        }finally{
            close();
        }
    }
    public String returnChangedUserLname(int userid){
        String lname = null;
        try{
            open();
            PreparedStatement q = conn.prepareStatement("SELECT * FROM USER WHERE USER_ID="+userid);
            ResultSet result = q.executeQuery();
            while(result.next()){
                User u = new User();
//                u.setFname(result.getString(4));
                u.setLname(result.getString(5));
                lname = u.getLname();
            }
            return lname; 
        }catch(SQLException e){
            System.out.println(e);
            return null;
        }finally{
            close();
        }
    }
    //ALL USERS ID
    public ArrayList allUsersId(){
        try{
            open();
            PreparedStatement q = conn.prepareStatement("SELECT * FROM USER;");
            ResultSet result = q.executeQuery();
            ArrayList <Integer> ids = new ArrayList();
            while(result.next()){
                ids.add(result.getInt(1));
            }
            return ids;
        }catch(SQLException e){
            System.out.println(e);
        }finally{
            close();
            return null;
        }
    }
    
    //update a user
    public void updateUser(){
        boolean flag = false;
        int p = 0;
        database db = new database();
        ArrayList <User> users = db.allUsers();
        Scanner scanner = new Scanner(System.in);
       while(flag == false){
        System.out.println(users);
        System.out.println("GIVE ID OF USER YOU WANT TO UPDATE");
//        while (!scanner.hasNextInt()) {
        if(!scanner.hasNextInt()){
                    System.out.println("NOT A VALID ENTRY");
    //                SuperAdminMenu();
                    flag = false;
        }else if(scanner.hasNextInt()){
            int user2change = scanner.nextInt();
                for(int i = 0 ; i < users.size(); i ++){
                    if(user2change == users.get(i).getUser_id()){
                        System.out.println("CHANGE HIS ROLE TO:");
                        System.out.println("PRESS 1 FOR SUPER ADMIN");
                        System.out.println("PRESS 2 FOR ADMIN");
                        System.out.println("PRESS 3 FOR USER");
                        int role = scanner.nextInt();
                        switch (role) {
                            case 1:
                                open();
                                try{
                                    PreparedStatement q = conn.prepareStatement("UPDATE USER SET ROLE_ID = "+role+" WHERE USER_ID = "+user2change+";");
                                    q.executeUpdate();
                                    flag = true;
                                    String fn = returnChangedUserFname(user2change);
                                    String ln = returnChangedUserLname(user2change);
                                    writer("UPDATE","BY: "+returnUsersFname()+" "+returnUsersLname()+" ROLE: S_ADMIN || TO USER_ID: "+user2change+" || FIRST_NAME: "+fn+", LAST_NAME "+ln+" || CHANGED ROLE TO: SUPER_ADMIN");
                                    System.out.println("A CHANGE HAS BEEN MADE!");
                                }catch(Exception e){System.out.println(e);}
                                finally{
                                    close();
                                }
                                break;
                            case 2:
                                open();
                                try{
                                    PreparedStatement q = conn.prepareStatement("UPDATE USER SET ROLE_ID = "+role+" WHERE USER_ID = "+user2change+";");
                                    q.executeUpdate();
                                    flag = true;
                                    String fn = returnChangedUserFname(user2change);
                                    String ln = returnChangedUserLname(user2change);
                                    writer("UPDATE","BY: "+returnUsersFname()+" "+returnUsersLname()+" ROLE: S_ADMIN || TO USER_ID: "+user2change+" || FIRST_NAME: "+fn+", LAST_NAME "+ln+" || CHANGED ROLE TO: ADMIN");
                                    System.out.println("A CHANGE HAS BEEN MADE!");
                                }catch(Exception e){System.out.println(e);}
                                finally{
                                    close();
                                }
                                break;
                            case 3:
                                open();
                                try{
                                    PreparedStatement q = conn.prepareStatement("UPDATE USER SET ROLE_ID = "+role+" WHERE USER_ID = "+user2change+";");
                                    q.executeUpdate();
                                    flag = true;
                                    String fn = returnChangedUserFname(user2change);
                                    String ln = returnChangedUserLname(user2change);
                                    writer("UPDATE","BY: "+returnUsersFname()+" "+returnUsersLname()+" ROLE: S_ADMIN || TO USER_ID: "+user2change+" || FIRST_NAME: "+fn+", LAST_NAME "+ln+" || CHANGED ROLE TO: USER");
                                    System.out.println("A CHANGE HAS BEEN MADE!");
                                }catch(Exception e){System.out.println(e);}
                                finally{
                                    close();
                                }
                                break;
                            default:
                                System.out.println("COULDN'T FIND USER WITH THAT ID");
                                //                            SuperAdminMenu();
                                int temp = scanner.nextInt();
                                flag = false;
                                break;
                        }
                            }
                        }
//                        for(int k = 0 ; k < users.size(); k ++){
//                            if(user2change != users.get(k).getUser_id()){
//                                 p = 1;
//                            }
//                        }
//                        if(p == 1){
//                            System.out.println("");
//                            System.out.println("COULDNT FIND THAT USER");
//                            System.out.println("");
//                            flag = false;
//                        }
        }
            }
    }
    
    
    //RETURN ALL THE PRODUCTS FOR ADMIN
    public ResultSet allProducts(){
        open();
        try{
             PreparedStatement q = conn.prepareStatement("SELECT * FROM PRODUCT");
             ResultSet result = q.executeQuery();
             ArrayList<Product> allProducts = new ArrayList<>();
             while(result.next()){
                 Product product = new Product();
                 product.setProduct_id(result.getInt(1));
                 product.setCode(result.getString(2));
                 product.setProduct_name(result.getString(3));
                 product.setCurrent_stock(result.getInt(4));
                 product.setDesc(result.getString(5));
                 product.setBrand(result.getString(6));
                 product.setCategory(result.getString(7));
                 
                 allProducts.add(product);    
             }
             System.out.println(allProducts);
             return result;
        }catch(Exception e){System.out.println(e);}
        finally{
                    close();
                }
                return null;
    }
    
    //RETURN ALL THE PRODUCTS FOR USERS
    public ResultSet allProductsUser(){
        open();
        try{
             PreparedStatement q = conn.prepareStatement("SELECT PRODUCT_ID,PRODUCT_NAME,DESCRIPTION,BRAND,CATEGORY FROM product;");
             ResultSet result = q.executeQuery();
             ArrayList<Product> allProducts = new ArrayList<>();
             while(result.next()){
                 Product product = new Product();
                 product.setProduct_id(result.getInt(1));
                 product.setProduct_name(result.getString(2));
                 product.setDesc(result.getString(3));
                 product.setBrand(result.getString(4));
                 product.setCategory(result.getString(5));
                 
                 allProducts.add(product);   
             }
//             System.out.println(allProducts);
            for(int i=0; i < allProducts.size();i++){
                System.out.println("ID:" +allProducts.get(i).getProduct_id()+" || PRODUCT_NAME: "+allProducts.get(i).getProduct_name()+" || DESCRIPTION: "+allProducts.get(i).getDesc()+" || BRAND: "+allProducts.get(i).getBrand()+" || CATEGORY: "+allProducts.get(i).getCategory());
            }
             return result;
        }catch(Exception e){System.out.println(e);}
        finally{
                    close();
                }
                return null;
    }
    
    

    
        //LOGIN    
        public String login() throws SQLException{
            open();
            String username;
            String passward;
            Scanner scanner = new Scanner(System.in);
            System.out.println("----------------------");
            System.out.println("Give username: ");
            username = scanner.next();
            System.out.println("----------------------");
            System.out.println("Give password: ");
            passward = scanner.next();
            try{
                PreparedStatement q = conn.prepareStatement("SELECT user_id,username,PASSWARD,fname,lname,ROLE FROM USER inner join roles on `USER`.`ROLE_ID` = `ROLES`.`ROLE_ID` WHERE USERNAME='"+username+"' AND PASSWARD='"+passward+"';");
                ResultSet result = q.executeQuery();
                if(result.next()){
                    System.out.println("");
                    System.out.println("Welcome "+username.toUpperCase());
                    System.out.println("----------------------");
                    returnedUser_id = result.getInt(1);
                    returnedRole = result.getString(COLUMN_USER_ROLE_ID);
                    return result.getString(COLUMN_USER_ROLE_ID);
                }
             }catch(SQLException e){System.out.println(e);}
            finally{
                close();
                writer("LOGIN",username.toUpperCase());
            }
             return null;
        }
            
    
    //MAKE A REGISTER
    public String register(){
        boolean flag=false;
        String role = null;
        String username;
        String passward;
        String fname;
        String lname;
        do{
         Scanner scanner = new Scanner(System.in);
         System.out.println("----------------------");
         System.out.println("Give username");
         username = scanner.next();
         System.out.println("----------------------");
         System.out.println("Give passward");
         System.out.println("----------------------");
         passward = scanner.next();
         System.out.println("Give first name");
         System.out.println("----------------------");
         fname = scanner.next();
         System.out.println("Give last name");
         System.out.println("----------------------");
         lname = scanner.next();
         int role_id = 3;
         open();
         if(checkifuserexist(username)==1){
            try{
                PreparedStatement q = conn.prepareStatement("INSERT INTO USER( USERNAME,PASSWARD,FNAME,LNAME,ROLE_ID) VALUES (?,?,?,?,?)");
                q.setString(1, username);
                q.setString(2, passward);
                q.setString(3, fname);
                q.setString(4, lname);
                q.setInt(5, role_id);
                q.executeUpdate();
                System.out.println("----------------------");
                System.out.println("A REGISTER HAD BEEN MADE!");
                System.out.println("WELOCOME "+username.toUpperCase());
                System.out.println("----------------------");
                flag=true;
                if(role_id==3){
                    role = "USER";
                }
                writer("REGISTER","USERNAME: "+username+" FIRST_NAME: "+fname+" LAST_NAME: "+lname+" || ROLE: USER");
                return role;
            }catch(Exception e){System.out.println("Couldnt made register "+e);}
            finally{
                close();
            }
         }
        }while(flag=false);
        return null;
     }
    
    //CHECK DOUBLICATES USERS
    public int checkifuserexist(String username){
        try{
            PreparedStatement q = conn.prepareStatement("SELECT * FROM USER WHERE USERNAME='"+username+"'");
            ResultSet result = q.executeQuery();
            if(result.next()){
                System.out.println("----------------------");
                System.out.println("CANT USE THAT USERNAME");
                System.out.println("----------------------");
                register();
                return 0;
            }
        }catch(Exception e){
            System.out.println(e);
        }
        return 1;
    }

    
    //MAKE AN ORDER,CASE ADMIN ADD TO CURRENT STOCK,CASE USER SUBTRACT FROM STOCK
    public void makeTransaction(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
        LocalDateTime now = LocalDateTime.now(); 
        int product_id = 0;
        int quantity = 0;
        Scanner scanner = new Scanner(System.in);
        int user_id = returnedUser_id;
        try{
            switch(returnedRole){
                case "ADMIN":
                allProducts();
                open();
                System.out.println("GIVE ID OF PRODUCT YOU WANT TO ORDER: ");
                product_id = scanner.nextInt();
                System.out.println("QUANTITY: ");
                quantity = scanner.nextInt();
                System.out.println("----------------------");
                PreparedStatement q = conn.prepareStatement("INSERT INTO PRODUCT_TR (PRODUCT_ID,QUANTITY,USER_ID,DATE_TR) VALUES ('"+product_id+"','"+quantity+"','"+user_id+"','"+dtf.format(now)+"');");
                q.executeUpdate();
                System.out.println("----------------------");
                PreparedStatement q1 = conn.prepareStatement("UPDATE PRODUCT SET CURRENT_STOCK = CURRENT_STOCK + "+quantity+" WHERE PRODUCT_ID = "+product_id+";");
                q1.executeUpdate();
                //LOG OF ORDER
                try{
                        open();
                    PreparedStatement order = conn.prepareStatement("SELECT PRODUCT_TR_ID,FNAME,LNAME,PRODUCT_NAME,CODE,QUANTITY,ROLE FROM PRODUCT_TR \n" +
                                                                    "INNER JOIN `USER` ON product_tr.USER_ID = user.USER_ID\n" +
                                                                    "INNER JOIN PRODUCT ON product_tr.PRODUCT_ID = PRODUCT.PRODUCT_ID\n" +
                                                                    "INNER JOIN ROLES ON USER.ROLE_ID = roles.ROLE_ID\n" +
                                                                    "WHERE USER.USER_ID = "+user_id+" ORDER BY PRODUCT_TR_ID DESC LIMIT 1;");
                    ResultSet orderResult = order.executeQuery();
                        while(orderResult.next()){
                                int i = orderResult.getInt(1);
                                String f = orderResult.getString(2);
                                String l = orderResult.getString(3);
                                String p_name = orderResult.getString(4);
                                String c = orderResult.getString(5);
                                int much = orderResult.getInt(6);
                                String role = orderResult.getString(7);
                                writer("ORDER"," BY: "+f.toUpperCase()+" "+l.toUpperCase()+" || TRANSACTION_ID: "+i+" || PRODUCT_NAME: "+p_name+", PRODUCT_CODE: "+c+" || QUANTITY: "+much+" || ROLE: "+role+"");
                        }
                    }catch(SQLException e){
                        System.out.println(e);
                    }finally{
                        close();
                    }
                  System.out.println("----------------------");
                  System.out.println("A ORDER HAD BEEN MADE!!!");
                  System.out.println("----------------------");
                break;
                case "USER":
                    allProductsUser();
                    open();
                    int stock = 0;
                    boolean flag = false;
                    while(flag == false){
                        System.out.println("----------------------");
                    System.out.println("GIVE ID OF PRODUCT YOU WANT TO ORDER: ");
                    if(scanner.hasNextInt()){
                    product_id = scanner.nextInt();
                        System.out.println("");
                    System.out.println("QUANTITY: ");
                    System.out.println("----------------------");
                    if(scanner.hasNextInt()){
                    quantity = scanner.nextInt();
                    PreparedStatement check = conn.prepareStatement("SELECT current_stock FROM warehouse.product where product_id = ?;");
                    check.setInt(1, product_id);
                    ResultSet result = check.executeQuery();
                    while(result.next()){
                        stock = result.getInt(1);
                    }
                    stock = stock - quantity;
                    if(stock < 0){
                        boolean flagLow = false;
                            while(flagLow == false){
                                System.out.println("----------------------");
                            System.out.println("THEIR WILL BE A DELAY CAUSE LOW STOCK PRODUCT");
                            System.out.println("DO YOU WANT TO PROCEED");
                            System.out.println("YES - NO");
                            System.out.println("----------------------");
                            String option = scanner.next();
                            switch(option.toUpperCase()){
                                case "YES":
                                    System.out.println("----------------------");
                                     PreparedStatement q2 = conn.prepareStatement("INSERT INTO PRODUCT_TR (PRODUCT_ID,QUANTITY,USER_ID,DATE_TR) VALUES ('"+product_id+"','"+quantity+"','"+user_id+"','"+dtf.format(now)+"');");
                                        q2.executeUpdate();
                                        System.out.println("----------------------");
                                        PreparedStatement q3 = conn.prepareStatement("UPDATE PRODUCT SET CURRENT_STOCK = CURRENT_STOCK - "+quantity+" WHERE PRODUCT_ID = "+product_id+";");
                                        q3.executeUpdate();
                                        flag = true;
                                        //LOG OF ORDER
                                        try{
                                            open();
                                        PreparedStatement order = conn.prepareStatement("SELECT PRODUCT_TR_ID,FNAME,LNAME,PRODUCT_NAME,CODE,QUANTITY,ROLE FROM PRODUCT_TR \n" +
                                                                                        "INNER JOIN `USER` ON product_tr.USER_ID = user.USER_ID\n" +
                                                                                        "INNER JOIN PRODUCT ON product_tr.PRODUCT_ID = PRODUCT.PRODUCT_ID\n" +
                                                                                        "INNER JOIN ROLES ON USER.ROLE_ID = roles.ROLE_ID\n" +
                                                                                        "WHERE USER.USER_ID = "+user_id+" ORDER BY PRODUCT_TR_ID DESC LIMIT 1;");
                                        ResultSet orderResult = order.executeQuery();
                                            while(orderResult.next()){
                                                    int i = orderResult.getInt(1);
                                                    String f = orderResult.getString(2);
                                                    String l = orderResult.getString(3);
                                                    String p_name = orderResult.getString(4);
                                                    String c = orderResult.getString(5);
                                                    int much = orderResult.getInt(6);
                                                    String role = orderResult.getString(7);
                                                    writer("ORDER"," BY: "+f.toUpperCase()+" "+l.toUpperCase()+" || TRANSACTION_ID: "+i+" || PRODUCT_NAME: "+p_name+", PRODUCT_CODE: "+c+" || QUANTITY: "+much+" || ROLE: "+role+"");
                                            }
                                        }catch(SQLException e){
                                            System.out.println(e);
                                        }finally{
                                            close();
                                        }
                                        System.out.println("----------------------");
                                      System.out.println("A ORDER HAD BEEN MADE!!!");
                                      System.out.println("----------------------");
                                      flagLow = true;
                                    break;
                                case "NO":
                                    flagLow = true;
                                    flag = true;
                                    break;
                                default:
                                    System.out.println("----------------");
                                    System.out.println("WRONG INPUT");
                                    flagLow = false;
                                    break;     
                            }
                        }
                    }else{
                    PreparedStatement q2 = conn.prepareStatement("INSERT INTO PRODUCT_TR (PRODUCT_ID,QUANTITY,USER_ID,DATE_TR) VALUES ('"+product_id+"','"+quantity+"','"+user_id+"','"+dtf.format(now)+"');");
                    q2.executeUpdate();
                    PreparedStatement q3 = conn.prepareStatement("UPDATE PRODUCT SET CURRENT_STOCK = CURRENT_STOCK - "+quantity+" WHERE PRODUCT_ID = "+product_id+";");
                    q3.executeUpdate();
                    //LOG OF ORDER
                    try{
                        open();
                    PreparedStatement order = conn.prepareStatement("SELECT PRODUCT_TR_ID,FNAME,LNAME,PRODUCT_NAME,CODE,QUANTITY,ROLE FROM PRODUCT_TR \n" +
                                                                    "INNER JOIN `USER` ON product_tr.USER_ID = user.USER_ID\n" +
                                                                    "INNER JOIN PRODUCT ON product_tr.PRODUCT_ID = PRODUCT.PRODUCT_ID\n" +
                                                                    "INNER JOIN ROLES ON USER.ROLE_ID = roles.ROLE_ID\n" +
                                                                    "WHERE USER.USER_ID = "+user_id+" ORDER BY PRODUCT_TR_ID DESC LIMIT 1;");
                    ResultSet orderResult = order.executeQuery();
                        while(orderResult.next()){
                                int i = orderResult.getInt(1);
                                String f = orderResult.getString(2);
                                String l = orderResult.getString(3);
                                String p_name = orderResult.getString(4);
                                String c = orderResult.getString(5);
                                int much = orderResult.getInt(6);
                                String role = orderResult.getString(7);
                                writer("ORDER"," BY: "+f.toUpperCase()+" "+l.toUpperCase()+" || TRANSACTION_ID: "+i+" || PRODUCT_NAME: "+p_name+", PRODUCT_CODE: "+c+" || QUANTITY: "+much+" || ROLE: "+role+"");
                        }
                    }catch(SQLException e){
                        System.out.println(e);
                    }finally{
                        close();
                    }
                    System.out.println("A ORDER HAD BEEN MADE!!!");
                    break;
                      }
                    }
                 }
              }
            }
        }catch(Exception e){
            System.out.println("DIDNT FIND THE PRODUCT YOU WANT TO ORDER!!! "+e);
            System.out.println(product_id);
        }
        finally{
            close();
        }

    }
    
    
    //TRANSACTION OF A USER
     public void allTransactionsUser(){
        try{
            open();
            PreparedStatement q = conn.prepareStatement("SELECT PRODUCT_TR_ID,DATE_TR,QUANTITY,PRODUCT_NAME,DESCRIPTION,BRAND,FNAME,LNAME FROM PRODUCT_TR \n" +
"                                                          INNER JOIN PRODUCT ON PRODUCT_TR.PRODUCT_ID = PRODUCT.PRODUCT_ID\n" +
"                                                          INNER JOIN USER ON PRODUCT_TR.USER_ID = USER.USER_ID\n" +
"                                                           INNER JOIN ROLES ON USER.ROLE_ID = ROLES.ROLE WHERE  USER.USER_ID="+returnedUser_id+";");
            ResultSet result = q.executeQuery();
                while(result.next()){
                    Product_Tr p_tr = new Product_Tr();
                    p_tr.setId(result.getInt(1));
                    p_tr.setDate(result.getString(2));
                    p_tr.setQuantity(result.getInt(3));
                    p_tr.setProduct_name(result.getString(4));
                    p_tr.setDescription(result.getString(5));
                    p_tr.setBrand(result.getString(6));
                    p_tr.setFname(result.getString(7));
                    p_tr.setLname(result.getString(8)); 

                     System.out.println("TRANSACTION_ID: "+p_tr.getId()+" || DATE: "+p_tr.getDate()+" QUANTITY: "+p_tr.getQuantity()+" || PRODUCT_NAME: "+p_tr.getProduct_name()+" || DESCRIPTION: "+p_tr.getDescription()+" BRAND: "+p_tr.getBrand()+" || NAME: "+p_tr.getFname()+" "+p_tr.getLname());
                  }
            if(!result.first()){
                System.out.println("YOU HAVE NO TRANSACTIONS");
            }
                
        }catch(Exception e){
            System.out.println(e);
        }
        finally{
            close();
        }
    }
     
    //ADDING NEW PRODUCT
    public void newProduct(){
        boolean flag = false;
        Scanner scanner = new Scanner(System.in);
        String code,product_name,desc,brand,cat;
        int quantity = 0;
        allProducts();
        try{
            open();
                    System.out.println("GIVE PRODUCT CODE: ");
                    code = scanner.next();
                    
                    scanner.nextLine();
                    System.out.println("GIVE PRODUCT NAME: ");
                    product_name = scanner.nextLine();
                    System.out.println("GIVE NUMBER OF UNITS: ");
                    while(flag == false){
                        if(scanner.hasNextInt()){
                            quantity = scanner.nextInt();
                            flag = true;
                        }else{
                            System.out.println("WRONG INPUT TRY AGAIN: ");
                            flag = false;
                            String temp = scanner.next();
                        }
                    }
                    System.out.println("GIVE BRAND: ");
                    brand = scanner.next();
                    System.out.println("GIVE CATEGORY: ");
                    cat = scanner.next();
                    
                    scanner.nextLine();
                    System.out.println("GIVE A DESCRIPTION: ");
                    desc = scanner.nextLine();
                    if(checkForDuplicateProduct(code)== 0){
                        PreparedStatement q = conn.prepareStatement("INSERT INTO PRODUCT (CODE,PRODUCT_NAME,CURRENT_STOCK,DESCRIPTION,BRAND,CATEGORY) VALUES ('"+code+"','"+product_name+"',"+quantity+",'"+desc+"','"+brand+"','"+cat+"');");
                        q.executeUpdate();
                        System.out.println("A INSERT HAD BEEN MADE!!!");
                        writer("INSERT_PRODUCT","BY: "+returnUsersFname()+" "+returnUsersLname()+" || PRODUCT_CODE: "+code+" ,PRODUCT_NAME: "+product_name+" ,QUANTITY: "+quantity+" ,BRAND: "+brand+" ,DESCRIPTION: "+desc+" ,CATEGORY: "+cat+"");
                    }else{
                        System.out.println("THE PRODUCT YOU TRY TO INSERT IS ALREADY ON THE WAREHOUSE");
                    }
        }catch(SQLException e){
            System.out.println(e);
        }finally{
            close();
        }
    
    }
    
    //CHECK FOR DUPLICATE PRODUCTS
    public int checkForDuplicateProduct(String code){
        
        try{
            PreparedStatement q = conn.prepareStatement("SELECT * FROM PRODUCT WHERE CODE='"+code+"'");
            ResultSet result = q.executeQuery();
            if(result.first()){
                return 1;
            }else{
                return 0;
            }
        }catch(Exception e){
            System.out.println(e);
            return 0;
        }
        
    }
    
    
    //DELETE A PRODUCT FROM WAREHOUSE
    public void deleteProduct(){
        Scanner scanner = new Scanner(System.in);
        int id = 0;
        int p_id = 0;
        String p_n = null;
        String c = null;
        String dec = null;
        boolean flag = false;
        boolean flag2 = false;
        allProducts();
        try{
            open();
            while(flag == false){
                System.out.println("\n");
                System.out.println("GIVE NUMBER OF ID YOU WANT TO DELETE: ");
                if(scanner.hasNextInt()){
                     id = scanner.nextInt();
                     flag = true;
     
                     Product p = new Product();
                     PreparedStatement q2 = conn.prepareStatement("SELECT * FROM PRODUCT WHERE PRODUCT_ID="+id);
                     ResultSet result = q2.executeQuery();
                     while(result.next()){
                         p.setProduct_id(result.getInt(1));
                         p.setCode(result.getString(2));
                         p.setProduct_name(result.getString(3));
                         p.setCurrent_stock(result.getInt(4));
                         p.setDesc(result.getString(5));
                         p.setBrand(result.getString(6));
                         p.setCategory(result.getString(7));
                         p_id = p.getProduct_id();
                         c = p.getCode();
                         p_n = p.getProduct_name();
                     }
                    if(p.getProduct_id()== 0){
                        System.out.println("");
                        System.out.println("THE PRODUCT YOU TRY TO DELETE IS NOT EXIST");
                        System.out.println("");
                    }else{
                            System.out.println("ARE YOU SURE YOU WANT TO DELETE THIS PRODUCT?");
                            System.out.println("");
                            System.out.println(p);
                            System.out.println("PRESS");
                            System.out.println("YES - NO");
                            System.out.println("");
                            while(flag2 == false){
                              if(scanner.hasNext()){
                                dec = scanner.next();
                                if(dec.toUpperCase().equals("YES")){
                                 PreparedStatement q = conn.prepareStatement("DELETE FROM PRODUCT WHERE PRODUCT_ID= ?");
                                    q.setInt(1, id);
                                    q.executeUpdate();
                                    System.out.println("THE PRODUCT HAS BEEN DELETED");
                                    System.out.println("");
                                    writer("DELETE_PRODUCT","BY: "+returnUsersFname()+" "+returnUsersLname()+" ,USER_ID: "+returnedUser_id+" || PRODUCT_ID: "+p_id+" ,PRODUCT_CODE: "+c+" ,PRODUCT_NAME: "+p_n+"");
                                    allProducts();
                                    flag2 = true;
                                }else if(dec.toUpperCase().equals("NO")){
                                    close();
                                    flag2 = true;
                                }else{
                                    System.out.println("WRONG INPUT,TRY AGAIN");
                                    System.out.println("");
                                    flag2 = false;
                                }
                              }
                            }
                    }
                }else{
                    System.out.println("WRONG INPUT: ");
                    flag = false;
                    String temp = scanner.next();
                }
            }
        }catch(Exception e){
            System.out.println(e);
        }finally{
            close();
        }
    }
    
    //UPDATE A PRODUCT 
    public void updateProduct(){
     Scanner scanner = new Scanner(System.in);
     int id = 0;
     boolean flagid = false;
     boolean flagResultId = false;
     boolean flagoption = false;
     Product p = new Product();
     Product p1 = new Product();
     String option = null;
     String change = null;
     allProducts();
     
     try{
         open();
         while(flagid == false){
         PreparedStatement q = conn.prepareStatement("SELECT PRODUCT_ID FROM PRODUCT");
         ResultSet products = q.executeQuery();
         System.out.println("GIVE ID OF PRODUCT YOU WANT TO UPDATE");
         if(scanner.hasNextInt()){
             id = scanner.nextInt();
             while(products.next()){
                if( products.getInt(1) == id){
                    flagid = true;
//                    System.out.println("DONE");
                    flagResultId = true;
                }
             }
             if(flagResultId == false){
                 System.out.println("PRODUCT DOES NOT EXIST");
             }
         }else{
             System.out.println("WRONG INPUT");
             String temp = scanner.next();
             flagid = false;
             flagResultId = false;
              }
         }
         
         PreparedStatement q2 = conn.prepareStatement("SELECT * FROM PRODUCT WHERE PRODUCT_ID ="+id);
         ResultSet product = q2.executeQuery();
         while(product.next()){
             p.setProduct_id(product.getInt(1));
             p.setCode(product.getString(2));
             p.setProduct_name(product.getString(3));
             p.setCurrent_stock(product.getInt(4));
             p.setDesc(product.getString(5));
             p.setBrand(product.getString(6));
             p.setCategory(product.getString(7));
         }
         System.out.println("");
         System.out.println("PRODUCT_ID: "+p.getProduct_id()+" || CODE: "+p.getCode()+" || PRODUCT_NAME: "+p.getProduct_name()+" || BRAND: "+p.getBrand()+" || DESCRIPTION: "+p.getDesc()+" || CATEGORY: "+p.getCategory()+"");
         System.out.println("");
         while(flagoption == false){
                System.out.println("GIVE WHAT COLUMN YOU WANT TO UPDATE: ");
                System.out.println("****for example product_name****");
                System.out.println("");
                option = scanner.next();
                switch(option.toUpperCase()){
                    case "CODE":
                        System.out.println("CURRENT CODE: "+p.getCode());
                        System.out.print("NEW CODE: ");
                        change = scanner.next();
                        PreparedStatement q3 = conn.prepareStatement("UPDATE product SET `CODE` = '"+change+"' WHERE (`PRODUCT_ID` = '"+id+"');");
                        q3.executeUpdate();
                         PreparedStatement q4 = conn.prepareStatement("SELECT * FROM PRODUCT WHERE PRODUCT_ID ="+id);
                          ResultSet newProductCode = q4.executeQuery();
                          while(newProductCode.next()){
                                p1.setProduct_id(newProductCode.getInt(1));
                                p1.setCode(newProductCode.getString(2));
                                p1.setProduct_name(newProductCode.getString(3));
                                p1.setCurrent_stock(newProductCode.getInt(4));
                                p1.setDesc(newProductCode.getString(5));
                                p1.setBrand(newProductCode.getString(6));
                                p1.setCategory(newProductCode.getString(7));
                            }
                            System.out.println("");
                            System.out.println("**NEW**");
                            System.out.println("PRODUCT_ID: "+p1.getProduct_id()+" || CODE: "+p1.getCode()+" || PRODUCT_NAME: "+p1.getProduct_name()+" || BRAND: "+p1.getBrand()+" || DESCRIPTION: "+p1.getDesc()+" || CATEGORY: "+p1.getCategory()+"");
                            System.out.println("");
                            writer("UPDATE_PRODUCT","BY: "+returnUsersFname()+" "+returnUsersLname()+" || ROLE: ADMIN || PRODUCT_CODE: "+p1.getCode()+" || CHANGED PRODUCT_CODE FROM "+p.getCode()+" TO "+p1.getCode()+"");
                        flagoption = true;
                        break;
                    case "PRODUCT_NAME":
                        System.out.println("CURRENT PRODUCT_NAME: "+p.getProduct_name());
                        System.out.print("NEW CODE: ");
                        change = scanner.next();
                        PreparedStatement q5 = conn.prepareStatement("UPDATE product SET `PRODUCT_NAME` = '"+change+"' WHERE (`PRODUCT_ID` = '"+id+"');");
                        q5.executeUpdate();
                         PreparedStatement q6 = conn.prepareStatement("SELECT * FROM PRODUCT WHERE PRODUCT_ID ="+id);
                          ResultSet newProductProductName = q6.executeQuery();
                          while(newProductProductName.next()){
                                p1.setProduct_id(newProductProductName.getInt(1));
                                p1.setCode(newProductProductName.getString(2));
                                p1.setProduct_name(newProductProductName.getString(3));
                                p1.setCurrent_stock(newProductProductName.getInt(4));
                                p1.setDesc(newProductProductName.getString(5));
                                p1.setBrand(newProductProductName.getString(6));
                                p1.setCategory(newProductProductName.getString(7));
                            }
                            System.out.println("");
                            System.out.println("**NEW**");
                            System.out.println("PRODUCT_ID: "+p1.getProduct_id()+" || CODE: "+p1.getCode()+" || PRODUCT_NAME: "+p1.getProduct_name()+" || BRAND: "+p1.getBrand()+" || DESCRIPTION: "+p1.getDesc()+" || CATEGORY: "+p1.getCategory()+"");
                            System.out.println("");
                            writer("UPDATE_PRODUCT","BY: "+returnUsersFname()+" "+returnUsersLname()+" || ROLE: ADMIN || PRODUCT_CODE: "+p1.getCode()+" ||CHANGED PRODUCT_NAME FROM "+p.getProduct_name()+" TO "+p1.getProduct_name()+"");
                        flagoption = true;
                        break;
                    case "DESCRIPtION":
                        System.out.println("CURRENT DESCRIPTION: "+p.getDesc());
                        scanner.nextLine();
                        System.out.print("NEW CODE: ");
                        change = scanner.nextLine();
                        PreparedStatement qDesc = conn.prepareStatement("UPDATE product SET `DESCRIPTION` = '"+change+"' WHERE (`PRODUCT_ID` = '"+id+"');");
                        qDesc.executeUpdate();
                         PreparedStatement qNewDesc = conn.prepareStatement("SELECT * FROM PRODUCT WHERE PRODUCT_ID ="+id);
                          ResultSet newProductDesc = qNewDesc.executeQuery();
                          while(newProductDesc.next()){
                                p1.setProduct_id(newProductDesc.getInt(1));
                                p1.setCode(newProductDesc.getString(2));
                                p1.setProduct_name(newProductDesc.getString(3));
                                p1.setCurrent_stock(newProductDesc.getInt(4));
                                p1.setDesc(newProductDesc.getString(5));
                                p1.setBrand(newProductDesc.getString(6));
                                p1.setCategory(newProductDesc.getString(7));
                            }
                            System.out.println("");
                            System.out.println("**NEW**");
                            System.out.println("PRODUCT_ID: "+p1.getProduct_id()+" || CODE: "+p1.getCode()+" || PRODUCT_NAME: "+p1.getProduct_name()+" || BRAND: "+p1.getBrand()+" || DESCRIPTION: "+p1.getDesc()+" || CATEGORY: "+p1.getCategory()+"");
                            System.out.println("");
                            writer("UPDATE_PRODUCT","BY: "+returnUsersFname()+" "+returnUsersLname()+" || ROLE: ADMIN || PRODUCT_CODE: "+p1.getCode()+" ||CHANGED DESCRIPTION FROM "+p.getDesc()+" TO "+p1.getDesc()+"");
                        flagoption = true;
                        break;
                    case "BRAND":
                        System.out.println("CURRENT BRAND: "+p.getBrand());
                        System.out.print("NEW BRAND: ");
                        change = scanner.next();
                        PreparedStatement qBrand = conn.prepareStatement("UPDATE product SET `BRAND` = '"+change+"' WHERE (`PRODUCT_ID` = '"+id+"');");
                        qBrand.executeUpdate();
                         PreparedStatement qNewBrand = conn.prepareStatement("SELECT * FROM PRODUCT WHERE PRODUCT_ID ="+id);
                          ResultSet newProductBrand = qNewBrand.executeQuery();
                          while(newProductBrand.next()){
                                p1.setProduct_id(newProductBrand.getInt(1));
                                p1.setCode(newProductBrand.getString(2));
                                p1.setProduct_name(newProductBrand.getString(3));
                                p1.setCurrent_stock(newProductBrand.getInt(4));
                                p1.setDesc(newProductBrand.getString(5));
                                p1.setBrand(newProductBrand.getString(6));
                                p1.setCategory(newProductBrand.getString(7));
                            }
                            System.out.println("");
                            System.out.println("**NEW**");
                            System.out.println("PRODUCT_ID: "+p1.getProduct_id()+" || CODE: "+p1.getCode()+" || PRODUCT_NAME: "+p1.getProduct_name()+" || BRAND: "+p1.getBrand()+" || DESCRIPTION: "+p1.getDesc()+" || CATEGORY: "+p1.getCategory()+"");
                            System.out.println("");
                            writer("UPDATE_PRODUCT","BY: "+returnUsersFname()+" "+returnUsersLname()+" || ROLE: ADMIN || PRODUCT_CODE: "+p1.getCode()+" ||CHANGED BRAND FROM "+p.getBrand()+" TO "+p1.getBrand()+"");
                        flagoption = true;
                        break;
                    case "CATEGORY":
                        System.out.println("CURRENT CATEGORY: "+p.getCategory());
                        System.out.print("NEW CATEGORY: ");
                        change = scanner.next();
                        PreparedStatement qCategory = conn.prepareStatement("UPDATE product SET `CATEGORY` = '"+change+"' WHERE (`PRODUCT_ID` = '"+id+"');");
                        qCategory.executeUpdate();
                         PreparedStatement qNewCategory = conn.prepareStatement("SELECT * FROM PRODUCT WHERE PRODUCT_ID ="+id);
                          ResultSet newProductCategory = qNewCategory.executeQuery();
                          while(newProductCategory.next()){
                                p1.setProduct_id(newProductCategory.getInt(1));
                                p1.setCode(newProductCategory.getString(2));
                                p1.setProduct_name(newProductCategory.getString(3));
                                p1.setCurrent_stock(newProductCategory.getInt(4));
                                p1.setDesc(newProductCategory.getString(5));
                                p1.setBrand(newProductCategory.getString(6));
                                p1.setCategory(newProductCategory.getString(7));
                            }
                            System.out.println("");
                            System.out.println("**NEW**");
                            System.out.println("PRODUCT_ID: "+p1.getProduct_id()+" || CODE: "+p1.getCode()+" || PRODUCT_NAME: "+p1.getProduct_name()+" || BRAND: "+p1.getBrand()+" || DESCRIPTION: "+p1.getDesc()+" || CATEGORY: "+p1.getCategory()+"");
                            System.out.println("");
                            writer("UPDATE_PRODUCT","BY: "+returnUsersFname()+" "+returnUsersLname()+" || ROLE: ADMIN || PRODUCT_CODE: "+p1.getCode()+" ||CHANGED CATEGORY FROM "+p.getCategory()+" TO "+p1.getCategory()+"");
                        flagoption = true;
                        break;
                    default:
                        System.out.println("WRONG INPUT");
                        flagoption = false;
                        break;
                }
         }
     }catch(Exception e){
         System.out.println(e);
     }finally{
         close();
     }
     
    }
    
    //SEARCH FOR USER
    public void searchProductUser(){
        Scanner scanner = new Scanner(System.in);
        String key = null;
        try{
            open();
            System.out.println("GIVE KEYWORD: ");
            key = scanner.next();
            
            PreparedStatement q = conn.prepareStatement("SELECT PRODUCT_ID,CODE,PRODUCT_NAME,DESCRIPTION,BRAND,CATEGORY FROM product WHERE CODE LIKE '%"+key+"%' OR PRODUCT_NAME LIKE '%"+key+"%' OR DESCRIPTION LIKE '%"+key+"%' OR BRAND LIKE '%"+key+"%' OR CATEGORY LIKE '%"+key+"%';");
            ResultSet result = q.executeQuery();
            while(result.next()){
                Product p = new Product();
                p.setProduct_id(result.getInt(1));
                p.setCode(result.getString(2));
                p.setProduct_name(result.getString(3));
                p.setDesc(result.getString(4));
                p.setBrand(result.getString(5));
                p.setCategory(result.getString(6));
                System.out.println("");
                System.out.println("------------------------------------------------------------------------------");
                System.out.println("ID:" +p.getProduct_id()+" || PRODUCT_NAME: "+p.getProduct_name()+" || DESCRIPTION: "+p.getDesc()+" || BRAND: "+p.getBrand()+" || CATEGORY: "+p.getCategory());
                System.out.println("");
            }
        }catch(Exception e){
            System.out.println("COULDNT FIND ANY REGISTER");
            System.out.println(e);
        }finally{
            close();
        }
    }
    //SEARCH FOR ADMIN
    public void searchProductAdmin(){
        Scanner scanner = new Scanner(System.in);
        String key = null;
        try{
            open();
            System.out.println("GIVE KEYWORD: ");
            key = scanner.next();
            
            PreparedStatement q = conn.prepareStatement("SELECT * FROM product WHERE CODE LIKE '%"+key+"%' OR PRODUCT_NAME LIKE '%"+key+"%' OR DESCRIPTION LIKE '%"+key+"%' OR BRAND LIKE '%"+key+"%' OR CATEGORY LIKE '%"+key+"%';");
//            q.setString(1, key);
//            q.setString(2, key);
//            q.setString(3, key);
//            q.setString(4, key);
//            q.setString(5, key);
            ResultSet result = q.executeQuery();
            while(result.next()){
                Product p = new Product();
                p.setProduct_id(result.getInt(1));
                p.setCode(result.getString(2));
                p.setProduct_name(result.getString(3));
                p.setCurrent_stock(result.getInt(4));
                p.setDesc(result.getString(5));
                p.setBrand(result.getString(6));
                p.setCategory(result.getString(7));
                System.out.println("------------------------------------------------------------------------------");
                System.out.println(p);
            }
        }catch(Exception e){
            System.out.println("COULDNT FIND ANY REGISTER");
            System.out.println(e);
        }finally{
            close();
        }
    }
    //SEARCH USER FOR S_ADMIN
    public void searchUserSadmin(){
        Scanner scanner = new Scanner(System.in);
        String key = null;
        String role = null;
        try{
            open();
            System.out.println("GIVE KEYWORD: ");
            key = scanner.next();
            
            PreparedStatement q = conn.prepareStatement("SELECT * FROM USER INNER JOIN roles ON USER.ROLE_ID = ROLES.ROLE WHERE USERNAME LIKE '%"+key+"%' OR FNAME LIKE '%"+key+"%' OR LNAME LIKE '%"+key+"%' OR ROLE LIKE '"+key+"';");
            ResultSet result = q.executeQuery();
            while(result.next()){
                User u = new User();
                u.setUser_id(result.getInt(1));
                u.setUsername(result.getString(2));
                u.setPw(result.getString(3));
                u.setFname(result.getString(4));
                u.setLname(result.getString(5));
                u.setRole_id(result.getInt(6));
                switch(u.getRole_id()){
                    case 1:
                        role = "SUPER_ADMIN";
                        break;
                    case 2:
                        role = "ADMIN";
                        break;
                    case 3:
                        role = "USER";
                        break;
                }
                System.out.println("");
                System.out.println("------------------------------------------------------------------------------");
                System.out.println("ID:" +u.getUser_id()+" || USERNAME: "+u.getUsername()+" || FIRST_NAME: "+u.getFname()+" || LAST_NAME: "+u.getLname()+" || ROLE: "+role);
                System.out.println("");
            }
        }catch(Exception e){
            System.out.println("COULDNT FIND ANY REGISTER");
            System.out.println(e);
        }finally{
            close();
        }
    }
    //SEARCH TRANSACTION
    public void searchTransactionSAdmin(){
         Scanner scanner = new Scanner(System.in);
         String key = null;
        try{
            open();
            System.out.println("GIVE KEYWORD");
            key = scanner.next();
            PreparedStatement q = conn.prepareStatement("SELECT PRODUCT_TR_ID,DATE_TR,QUANTITY,FNAME,LNAME,ROLE,CODE,PRODUCT_NAME,DESCRIPTION,BRAND,CATEGORY FROM product_tr  "
                    + "                                  INNER JOIN USER ON product_tr.USER_ID = user.USER_ID\n" +
                                                        "INNER JOIN product ON product_tr.PRODUCT_ID = product.PRODUCT_ID "
                    + "                                  INNER JOIN ROLES ON USER.ROLE_ID = ROLES.ROLE"
                    + "                                  WHERE PRODUCT_TR_ID LIKE '%"+key+"%' OR FNAME LIKE '%"+key+"%' OR LNAME LIKE '%"+key+"%' OR PRODUCT_NAME LIKE '%"+key+"%' OR DESCRIPTION LIKE '%"+key+"%' OR BRAND LIKE '%"+key+"%' OR CATEGORY LIKE '%"+key+"%' OR ROLE LIKE '%"+key+"%';");
            ResultSet result = q.executeQuery();
            while(result.next()){
                System.out.println("");
                System.out.println("TRANSACTION_ID: "+result.getInt(1)+" || DATE: "+result.getString(2)+" || QUANTITY: "+result.getInt(3)+" || FIRST_NAME: "+result.getString(4)+" || LAST_NAME: "+result.getString(5)+" || ROLE: "+result.getString(6)+" || PRODUCT_CODE: "+result.getString(7)+" || PRODUCT_NAME: "+result.getString(8)+" || DESCRIPTION: "+result.getString(9)+" || BRAND: "+result.getString(10)+" || CATEGORY: "+result.getString(11)+"");
                System.out.println("");
            }
        }catch(SQLException e){
            System.out.println(e);
        }finally{
            close();
        }
    }
    
    //PRODUCTS LOW STOCK
    public void lowStockProducts(){
        
    
        try{
            open();
            PreparedStatement q = conn.prepareStatement("SELECT * FROM PRODUCT WHERE CURRENT_STOCK < 5");
            ResultSet result = q.executeQuery();
            while(result.next()){
                Product p = new Product();
                p.setProduct_id(result.getInt(1));
                p.setCode(result.getString(2));
                p.setProduct_name(result.getString(3));
                p.setCurrent_stock(result.getInt(4));
                p.setDesc(result.getString(5));
                p.setBrand(result.getString(6));
                p.setCategory(result.getString(7));
                System.out.println("");
                System.out.println("PRODUCT_ID: "+p.getProduct_id()+" || PRODUCT_CODE: "+p.getCode()+" || PRODUCT_NAME: "+p.getProduct_name()+" || DESCRIPTION: "+p.getDesc()+" || BRAND: "+p.getBrand()+" || CATEGORY: "+p.getCategory()+" || CURRENT_STOCK: "+p.getCurrent_stock());
                System.out.println("");
            }
            if(!result.first()){
                System.out.println("THEIR NOT LOW STOCK PRODUCTS");
            }
        }catch(Exception e){
            System.out.println(e);
        }finally{
            close();
        }
    }
    
    //UPDATE USERS INFO
    public void updateUserInfo(){
        Scanner scanner = new Scanner(System.in);
        String option = null;
        boolean flag = false;
        String change = null;
        int user_id = returnedUser_id;
        try{
            open();
            PreparedStatement q = conn.prepareStatement("SELECT * FROM USER WHERE USER_ID="+user_id);
            ResultSet result = q.executeQuery();
            User u = new User();
            while(result.next()){
                u.setUser_id(result.getInt(1));
                u.setUsername(result.getString(2));
                u.setPw(result.getString(3));
                u.setFname(result.getString(4));
                u.setLname(result.getString(5));
                u.setRole_id(result.getInt(6));
                System.out.println("USERNAME: "+u.getUsername()+" ||PASSWORD: "+u.getPw()+" ||FIRST_NAME: "+u.getFname()+" ||LAST_NAME: "+u.getLname());
            }
            while(flag == false){
                System.out.println("");
                System.out.println("GIVE WHAT COLUMN YOU WANT TO UPDATE: ");
                System.out.println("USERNAME - PASSWORD - FIRST_NAME - LAST_NAME");
                System.out.println("****for example first_name****");
                System.out.println("");
                option = scanner.next();
                switch(option.toUpperCase()){
                    case "USERNAME":
                        System.out.println("CURRENT USERNAME: "+u.getUsername());
                        System.out.print("GIVE NEW USERNAME: ");
                        change = scanner.next();
                        PreparedStatement q1 = conn.prepareStatement("UPDATE USER SET `USERNAME` = '"+change+"' WHERE (`USER_ID` ="+returnedUser_id+")");
                        q1.executeUpdate();
                        PreparedStatement q2 = conn.prepareStatement("SELECT * FROM USER WHERE USER_ID="+user_id);
                        ResultSet resultUSERNAME = q2.executeQuery();
                        User uNEWUSERNAME = new User();
                        while(resultUSERNAME.next()){
                            uNEWUSERNAME.setUser_id(resultUSERNAME.getInt(1));
                            uNEWUSERNAME.setUsername(resultUSERNAME.getString(2));
                            uNEWUSERNAME.setPw(resultUSERNAME.getString(3));
                            uNEWUSERNAME.setFname(resultUSERNAME.getString(4));
                            uNEWUSERNAME.setLname(resultUSERNAME.getString(5));
                            uNEWUSERNAME.setRole_id(resultUSERNAME.getInt(6));
                            System.out.println("");
                            System.out.println("**NEW**");
                            System.out.println("USERNAME: "+uNEWUSERNAME.getUsername()+" ||PASSWORD: "+uNEWUSERNAME.getPw()+" ||FIRST_NAME: "+uNEWUSERNAME.getFname()+" ||LAST_NAME: "+uNEWUSERNAME.getLname());
                        }
                        writer("UPDATE_INFO","BY: "+returnUsersFname()+" "+returnUsersLname()+" || CHANGED USERNAME FROM: "+u.getUsername()+" TO: "+change+"");
                        flag = true;
                        break;
                    case "PASSWORD":
                        System.out.println("CURRENT PASSWORD: "+u.getPw());
                        System.out.print("GIVE NEW PASSWORD: ");
                        change = scanner.next();
                        PreparedStatement q3 = conn.prepareStatement("UPDATE USER SET `PASSWARD` = '"+change+"' WHERE (`USER_ID` ="+user_id+")");
                        q3.executeUpdate();
                        PreparedStatement q4 = conn.prepareStatement("SELECT * FROM USER WHERE USER_ID="+user_id);
                        ResultSet resultPASSWORD = q4.executeQuery();
                        User uNEWPASSWORD = new User();
                        while(resultPASSWORD.next()){
                            uNEWPASSWORD.setUser_id(resultPASSWORD.getInt(1));
                            uNEWPASSWORD.setUsername(resultPASSWORD.getString(2));
                            uNEWPASSWORD.setPw(resultPASSWORD.getString(3));
                            uNEWPASSWORD.setFname(resultPASSWORD.getString(4));
                            uNEWPASSWORD.setLname(resultPASSWORD.getString(5));
                            uNEWPASSWORD.setRole_id(resultPASSWORD.getInt(6));
                            System.out.println("");
                            System.out.println("**NEW**");
                            System.out.println("USERNAME: "+uNEWPASSWORD.getUsername()+" ||PASSWORD: "+uNEWPASSWORD.getPw()+" ||FIRST_NAME: "+uNEWPASSWORD.getFname()+" ||LAST_NAME: "+uNEWPASSWORD.getLname());
                        }
                        writer("UPDATE_INFO","BY: "+returnUsersFname()+" "+returnUsersLname()+" || CHANGED PASSWORD FROM: "+u.getPw()+" TO: "+change+"");
                        flag = true;
                        break;
                    case "FIRST_NAME":
                        System.out.println("CURRENT FIRST_NAME: "+u.getFname());
                        System.out.print("GIVE NEW FIRST_NAME: ");
                        change = scanner.next();
                        PreparedStatement qFN = conn.prepareStatement("UPDATE USER SET `FNAME` = '"+change+"' WHERE (`USER_ID` ="+user_id+")");
                        qFN.executeUpdate();
                        PreparedStatement qNFN = conn.prepareStatement("SELECT * FROM USER WHERE USER_ID="+user_id);
                        ResultSet resultFIRSTNAME = qNFN.executeQuery();
                        User uNEWFIRSTNAME = new User();
                        while(resultFIRSTNAME.next()){
                            uNEWFIRSTNAME.setUser_id(resultFIRSTNAME.getInt(1));
                            uNEWFIRSTNAME.setUsername(resultFIRSTNAME.getString(2));
                            uNEWFIRSTNAME.setPw(resultFIRSTNAME.getString(3));
                            uNEWFIRSTNAME.setFname(resultFIRSTNAME.getString(4));
                            uNEWFIRSTNAME.setLname(resultFIRSTNAME.getString(5));
                            uNEWFIRSTNAME.setRole_id(resultFIRSTNAME.getInt(6));
                            System.out.println("");
                            System.out.println("**NEW**");
                            System.out.println("USERNAME: "+uNEWFIRSTNAME.getUsername()+" ||PASSWORD: "+uNEWFIRSTNAME.getPw()+" ||FIRST_NAME: "+uNEWFIRSTNAME.getFname()+" ||LAST_NAME: "+uNEWFIRSTNAME.getLname());
                        }
                        writer("UPDATE_INFO","BY: "+returnUsersFname()+" "+returnUsersLname()+" || CHANGED FIRST_NAME FROM: "+u.getFname()+" TO: "+change+"");
                        flag = true;
                        break;
                    case "LAST_NAME":
                        System.out.println("CURRENT LAST_NAME: "+u.getLname());
                        System.out.print("GIVE NEW LAST_NAME: ");
                        change = scanner.next();
                        PreparedStatement qLN = conn.prepareStatement("UPDATE USER SET `LNAME` = '"+change+"' WHERE (`USER_ID` ="+user_id+")");
                        qLN.executeUpdate();
                        PreparedStatement qNLN = conn.prepareStatement("SELECT * FROM USER WHERE USER_ID="+user_id);
                        ResultSet resultLASTNAME = qNLN.executeQuery();
                        User uNEWLASTNAME = new User();
                        while(resultLASTNAME.next()){
                            uNEWLASTNAME.setUser_id(resultLASTNAME.getInt(1));
                            uNEWLASTNAME.setUsername(resultLASTNAME.getString(2));
                            uNEWLASTNAME.setPw(resultLASTNAME.getString(3));
                            uNEWLASTNAME.setFname(resultLASTNAME.getString(4));
                            uNEWLASTNAME.setLname(resultLASTNAME.getString(5));
                            uNEWLASTNAME.setRole_id(resultLASTNAME.getInt(6));
                            System.out.println("");
                            System.out.println("**NEW**");
                            System.out.println("USERNAME: "+uNEWLASTNAME.getUsername()+" ||PASSWORD: "+uNEWLASTNAME.getPw()+" ||FIRST_NAME: "+uNEWLASTNAME.getFname()+" ||LAST_NAME: "+uNEWLASTNAME.getLname());
                        }
                        writer("UPDATE_INFO","BY: "+returnUsersFname()+" "+returnUsersLname()+" || CHANGED LAST_NAME FROM: "+u.getLname()+" TO: "+change+"");
                        flag = true;
                        break;
                    default:
                        System.out.println("WRONG INPUT TRY AGAIN");
                        flag = false;
                        break;
                }
            }
        }catch(Exception e){System.out.println(e);}
        
        
    }
    
    public void deleteProfile(){
        Scanner scanner = new Scanner(System.in);
        boolean flag = false;
        String option = null;
        int role_id = 0;
        String role = null;
        String fname = null;
        String lname = null;
        String un = null;
        try{
            while(flag == false){
                open();
                System.out.println("ARE YOU SURE?");
                System.out.println("YES - NO");
                option = scanner.next();
                switch(option.toUpperCase()){
                    case "YES":
                        User u = new User();
                        PreparedStatement q2 = conn.prepareStatement("SELECT * FROM USER WHERE USER_ID=?");
                        q2.setInt(1, returnedUser_id);
                        ResultSet result = q2.executeQuery();
                        while(result.next()){
                            u.setUsername(result.getString(2));
                            u.setFname(result.getString(4));
                            u.setLname(result.getString(5));
                            u.setRole_id(result.getInt(6));
                            un = u.getUsername();
                            fname = u.getFname();
                            lname = u.getLname();
                            role_id = u.getRole_id();                          
                        }
                        PreparedStatement q = conn.prepareStatement("DELETE FROM USER WHERE USER_ID=?");
                        q.setInt(1, returnedUser_id);
                        q.executeUpdate();
                        System.out.println("GOODBYE");
                        switch(role_id){
                            case 1:
                                role = "S_ADMIN";
                                break;
                            case 2:
                                role = "ADMIN";
                                break;
                            case 3:
                                role = "USER";
                                break;
                            default:
                                break;
                        }
                        writer("DELETE_PROFILE","FIRST_NAME: "+fname+" ,LAST_NAME: "+lname+" || USERNAME: "+un+" || ROLE: "+role+"");
                        flag = true;
                        break;
                    case "NO":
                        flag = true;
                        break;
                    default:
                        System.out.println("WRONG INPUT");
                        System.out.println("");
                        flag = false;
                        break;
                }
            }
        }catch(SQLException e){
            System.out.println(e);
        }finally{
            close();
        }
    }
    
    public void deleteUser(){
        Scanner scanner = new Scanner (System.in);
        int user_id = 0;
        String un = null;
        String fname = null;
        String lname = null;
        String role = null;
        int option = 0;
        int exist = 0;
        String done = null;
        boolean flag = false;
        User u = new User();
        ArrayList <Integer> ids = new ArrayList();
        try{
            open();
            PreparedStatement q = conn.prepareStatement("SELECT USER_ID,USERNAME,FNAME,LNAME,ROLE FROM warehouse.user\n" +
                                                        "INNER JOIN ROLES ON `USER`.`ROLE_ID` = `ROLES`.`ROLE_ID`\n" +
                                                        "where USER.role_id = 2 or USER.role_id = 3 \n" +
                                                        ";");
            ResultSet result = q.executeQuery();
            while(result.next()){
              u.setUser_id(result.getInt(1));
              u.setUsername(result.getString(2));
              u.setFname(result.getString(3));
              u.setLname(result.getString(4));
              u.setRole(result.getString(5));
              ids.add(u.getUser_id());
              System.out.println("");
              System.out.println("USER_ID: "+u.getUser_id()+" || USERNAME: "+u.getUsername()+" || FIRST_NAME: "+u.getFname()+" || LAST_NAME: "+u.getLname()+" || ROLE: "+u.getRole()+"");
            }
            System.out.println("");
            while(flag == false){
                System.out.println("GIVE ID OF USER YOU WANT TO DELETE");
                if(scanner.hasNextInt()){
                    option = scanner.nextInt();
                    for(int i=0; i<ids.size(); i++){
                        if(ids.get(i) == option){
                            PreparedStatement q2 = conn.prepareStatement("SELECT USER_ID,USERNAME,FNAME,LNAME,ROLE FROM warehouse.user\n" +
                                                        "INNER JOIN ROLES ON `USER`.`ROLE_ID` = `ROLES`.`ROLE_ID`\n" +
                                                        "where USER.USER_ID= ?\n" +
                                                        ";");
                                q2.setInt(1,option);
                                ResultSet result1 = q2.executeQuery();
                                while(result1.next()){
                                  u.setUser_id(result1.getInt(1));
                                  u.setUsername(result1.getString(2));
                                  u.setFname(result1.getString(3));
                                  u.setLname(result1.getString(4));
                                  u.setRole(result1.getString(5));
                                  un = u.getUsername();
                                  fname = u.getFname();
                                  lname = u.getLname();
                                  role = u.getRole();
                                }
                            System.out.println("ARE YOU SURE?");
                            System.out.println("YES - NO");
                            done = scanner.next();
                            exist = 1;
                            switch(done.toUpperCase()){
                                case "YES":
                                    PreparedStatement q1 = conn.prepareStatement("DELETE FROM USER WHERE USER_ID=?");
                                    q1.setInt(1, option);
                                    q1.executeUpdate();
                                    writer("DELETE_USER","BY "+returnUsersFname()+" "+returnUsersLname()+" || ROLE: S_ADMIN || DELETE_USER  USERNAME: "+un+" ,FIRST_NAME: "+fname+" ,LAST_NAME: "+lname+" ,ROLE: "+role+"");
                                    System.out.println("USER HAS BEEN DELETED");
                                    flag = true;
                                    break;
                                case "NO":
                                    flag = true;
                                    break;
                                default:
                                    flag = false;
                                    break;
                            }
                        }
//                        if(exist == 0){
//                            System.out.println("USER_ID DOES NOT EXIST");
//                            flag = false;
//                        }
                    }
                }else{
                    System.out.println("WRONG INPUT");
                    String temp = scanner.next();
                    flag = false;
                }
            }
        }catch(Exception e){
            System.out.println(e);
        }finally{
            close();
        }
    }
    
    
}
