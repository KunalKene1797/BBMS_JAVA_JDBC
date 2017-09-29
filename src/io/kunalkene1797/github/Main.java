package io.kunalkene1797.github;
import java.io.IOException;
import java.sql.*;
import java.util.Scanner;

public class Main {
    public static final String url = "jdbc:postgresql://localhost:5432/BBMS";
    public static Scanner in = new Scanner(System.in);
    public static Connection cn = null;
    public static Statement stmt = null;
    public static void main(String[] args) throws SQLException, IOException {
        //Objects
        cn = DriverManager.getConnection(url,"postgres","pebavoxi1797");
        boolean login = false;
        if (cn!=null){
            System.out.println("Connection Established!");
        }

        //Login System
            System.out.println("~ Let's get you logged in into the system ~");
            System.out.print("Username: ");
            String uname = in.next();
            System.out.print("Password: ");
            String pword = in.next();
        stmt = cn.createStatement();
        ResultSet rs = stmt.executeQuery("select uname,pword from admin");
        while(rs.next()){
            String dbu = rs.getString("uname");
            String dbp = rs.getString("pword");
            if (uname.equals("admin") && pword.equals("admin")){
                login = true;
                mainmenu();
                System.out.println("Login Successful!");
            }
        }
        if (!login){
            System.out.println("Wrong Credentials!");
            System.out.printf("Hit Enter to Try Again");
            System.in.read();
            main(args);
        }
    }
    public static void mainmenu() throws IOException, SQLException {
        //Objects
        Scanner in = new Scanner(System.in);
        //Main Menu
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("~~~~~~~~~~ Welcome to the System ~~~~~~~~~~");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("Admin Panel: What would you like to perform?");
        System.out.println("1. Check Blood Bank Status");
        System.out.println("2. Buy Blood for Patient");
        System.out.println("3. Donate Blood for Donor");
        System.out.println("4. Exit");
        int c = in.nextInt();
        switch(c){
            case 1:
                check_status();
                break;
            case 2:
                buy();
                break;
            case 3:
                donate();
                break;
            case 4:
                System.exit(0);
                break;
            default:
                System.out.println("Wrong Choice!");
                System.in.read();
                mainmenu();

        }
    }
    public static void check_status() throws SQLException, IOException {
        stmt = cn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT bname,bquantity,bprice FROM bloodbank");
        while (rs.next()){
            String bname = rs.getString("bname");
            String bquantity = rs.getString("bquantity");
            int bprice = rs.getInt("bprice");
            System.out.println("~~~~~~~~~~~~~~~~~~~~~");
            System.out.println("Blood Group: "+bname);
            System.out.println("Quantity: "+bquantity);
            System.out.println("brpice: "+bprice);
            System.out.println("~~~~~~~~~~~~~~~~~~~~~");
            System.out.println("Press Enter to Display Next Blood Group");
            System.in.read();
        }
    }

    public static void donate() throws SQLException, IOException {
        //objects
        Scanner in = new Scanner(System.in);

        //donate menu
        System.out.println("Which blood group would you like to donate?");
        System.out.println("1. A+");
        System.out.println("2. A-");
        System.out.println("3. B+");
        System.out.println("4. B-");
        System.out.println("5. O+");
        System.out.println("6. O-");
        System.out.println("7. AB+");
        System.out.println("8. AB-");
        int c = in.nextInt();
        switch (c){
            case 1:
                add_blood(0);
                break;
            case 2:
                add_blood(1);
                break;
            case 3:
                add_blood(2);
                break;
            case 4:
                add_blood(3);
                break;
            case 5:
                add_blood(4);
                break;
            case 6:
                add_blood(5);
                break;
            case 7:
                add_blood(6);
                break;
            case 8:
                add_blood(7);
            break;
            default:
                System.out.println("Invalid Option");
                System.in.read();
                donate();
        }
        System.out.println("Thanks for your Donation! Have a Great Day!");
        stmt = cn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT bquantity from bloodbank where bid ="+(c-1));
        while (rs.next()){
            int newquantity = rs.getInt("bquantity");
            System.out.println("The New Quantity is "+ newquantity);
        }
        System.in.read();
        mainmenu();
    }

    public static void add_blood(int bid) throws SQLException {
        stmt = cn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT bname from bloodbank where bid = "+bid);
        while (rs.next()){
            String urbname = rs.getString("bname");
            System.out.println("Your Choice: "+ urbname);
        }
        System.out.print("Enter Quantity: ");
        int q = in.nextInt();
        stmt = cn.createStatement();
        rs = stmt.executeQuery("SELECT bquantity from bloodbank where bid ="+bid);
        while (rs.next()){
            int eq = rs.getInt("bquantity");
            q = eq + q;
        }
        stmt.executeUpdate("UPDATE bloodbank VALUES SET bquantity =" + q + "WHERE bid ="+bid);

    }

    public static void buy() throws IOException, SQLException {
        //objects
        Scanner in = new Scanner(System.in);

        //buy menu
        System.out.println("Which blood group would you like to buy?");
        System.out.println("1. A+");
        System.out.println("2. A-");
        System.out.println("3. B+");
        System.out.println("4. B-");
        System.out.println("5. O+");
        System.out.println("6. O-");
        System.out.println("7. AB+");
        System.out.println("8. AB-");
        int c = in.nextInt();
        switch (c){
            case 1:
                buy_blood(0);
                break;
            case 2:
                buy_blood(1);
                break;
            case 3:
                buy_blood(2);
                break;
            case 4:
                buy_blood(3);
                break;
            case 5:
                buy_blood(4);
                break;
            case 6:
                buy_blood(5);
                break;
            case 7:
                buy_blood(6);
                break;
            case 8:
                buy_blood(7);
                break;
            default:
                System.out.println("Invalid Option");
                System.in.read();
                buy();
        }
    }
    public static void buy_blood(int bid) throws SQLException, IOException {
        stmt = cn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT bname from bloodbank where bid ="+bid);
        while (rs.next()){
            String bgname = rs.getString("bname");
            System.out.println("The Blood Group to Buy: "+bgname);
        }
        System.out.println("Enter the Quantity to Buy in Liters: ");
        int buyq = in.nextInt();
        rs = stmt.executeQuery("SELECT * from bloodbank where bid ="+bid);
        int newq =0;
        while (rs.next()){
            int bq = rs.getInt("bquantity");
            int bp = rs.getInt("bprice");
            System.out.println("Price per Liter: "+bp);
            System.out.println("Current Quantity: "+bq);
            bp = bp * buyq;
            newq =  bq - buyq;
            System.out.println("Bill Generated of Rs. "+bp);
            System.out.println("~~~~~~~~~~~~~~~~~~~~~");
        }
        stmt.executeUpdate("UPDATE bloodbank VALUES SET bquantity ="+newq+"WHERE bid ="+bid);
        rs = stmt.executeQuery("SELECT * from bloodbank where bid = "+bid);
        while (rs.next()){
            int bq = rs.getInt("bquantity");
            System.out.println("Remaining Blood Quantity: "+bq);
            System.out.println("~~~~~~~~~~~~~~~~~~~~~");
        }
        System.out.println("Press Enter to Continue");
        System.in.read();
        mainmenu();
    }
}