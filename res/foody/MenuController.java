
package sample.foody;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MenuController implements Initializable {
    public static int total;
    public MenuModel menuModel =new MenuModel();


    @FXML
    private TableView<ModelTable> table;
    @FXML
    private TableColumn<ModelTable, String> MenuIdCol;

    @FXML
    private TableColumn<ModelTable, String> MenuNameCol;

    @FXML
    private TableColumn<ModelTable, String> PriceCol;

    @FXML
    private TableColumn<ModelTable, String> QuantityCol;

    @FXML
    private Label totalAmount,disc,Amount,discount,alt;

    @FXML
    private PasswordField otptxt;

    @FXML
    private PasswordField newpasstxt;

    @FXML
    private TextField statetxt;

    @FXML
    private TextField citytxt;

    @FXML
    private TextField pincodetxt,code;

    @FXML
    private TextArea landtxt;
    @FXML
    private TableView<ModelTable1> table1;

    @FXML
    private TableColumn<ModelTable1, String> OrderidCol1;

    @FXML
    private TableColumn<ModelTable1, String> MenuNameCol1;

    @FXML
    private TableColumn<ModelTable1, String> QuantityCol1;

    @FXML
    private TableColumn<ModelTable1, String> OrderStatusCol1;
    private String mail1,otps;
    private int disc_perc=0;



    Connection con; //connection for table

    public static int i;

    boolean type;


    ObservableList<ModelTable> obList= FXCollections.observableArrayList();
    ObservableList<ModelTable1> obList1= FXCollections.observableArrayList();


    public MenuController(){
        con=SqlConnection.Connector();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        totalAmount.setText("0");

        i= LoginController.cust_id;//customer id which is primary key
        if(menuModel.isDbConnected()){
            System.out.println("Db connected");
        }else{
            System.out.println("Db not connected");
        }


        MenuIdCol.setCellValueFactory(new PropertyValueFactory<>("menuid"));
        MenuNameCol.setCellValueFactory(new PropertyValueFactory<>("menuname"));
        PriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        QuantityCol1.setCellValueFactory(new PropertyValueFactory<>("quantity_item"));
        tableConnection();
        table.setItems(obList);
        table.refresh();
        table.getSelectionModel().clearSelection();
        calculate();
        getAddress();

        OrderidCol1.setCellValueFactory(new PropertyValueFactory<>("orderno"));
        MenuNameCol1.setCellValueFactory(new PropertyValueFactory<>("menuname"));
        QuantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity_item"));
        OrderStatusCol1.setCellValueFactory(new PropertyValueFactory<>("status"));
        tableConnection1();
        table1.setItems(obList1);
        table1.refresh();


    }


    public void deleteItem(ActionEvent event){
        ModelTable tableIndex = (ModelTable)table.getSelectionModel().getSelectedItem();
        int tempMenuid = -1;
        try{
            tempMenuid = tableIndex.getMenuid();
        }catch(Exception e){
            infoBox1("no item selected!", null, "Error");

        }

        if(tempMenuid >= 0){
            String query = "DELETE FROM orders WHERE  ( menu_id = ? and customer_id=? and order_status='ADDED_TO_CART') ";
            PreparedStatement pst;
            try {
                pst = con.prepareStatement(query);
                pst.setInt(1, tempMenuid);
                pst.setInt(2, i);
                pst.execute();
                table.getItems().remove(tableIndex);
                table.refresh();
                table.getSelectionModel().clearSelection();

                calculate();

            } catch (SQLException ex) {
                Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
            }catch(Exception e){
                infoBox1("no item selected!", null, "Error");
            }


        } else {
            System.out.println("no selction made");
        }
    }





    public void tableConnection(){
        table.getItems().clear();
        try {

            String query="SELECT menu.price as Price ,menu.menu_id,menu.menu_name as Name,quantity FROM orders JOIN menu ON orders.menu_id=menu.menu_id WHERE orders.customer_id="+i+" and order_status='ADDED_TO_CART'";
            ResultSet rs =con.createStatement().executeQuery(query);
            while(rs.next()){
                obList.add(new ModelTable(rs.getString("Name"), rs.getInt("menu_id"),rs.getInt("quantity"), rs.getInt("Price")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void tableConnection1(){

        try {

            String query="SELECT orders.order_id as OrderId,menu.menu_name as Name,quantity, order_status FROM orders JOIN menu ON orders.menu_id=menu.menu_id WHERE orders.customer_id="+i+" and (order_status='PAYMENT_CONFIRMED' OR order_status='DELIVERED') ";
            ResultSet rs =con.createStatement().executeQuery(query);
            while(rs.next()){
                obList1.add(new ModelTable1( rs.getInt("OrderId"), rs.getString("Name"), rs.getInt("quantity"),rs.getString("order_status")) );
            }
        } catch (SQLException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void apply_code(ActionEvent event) {
        String promo = code.getText();
        System.out.println(promo);
        switch (promo) {
            case "FOODY30":
                disc.setText("Discount (30%) :");
                alt.setText("CODE APPLIED!");
                disc_perc = 30;
                break;
            case "FOODY15":
                disc.setText("Discount (15%) :");
                alt.setText("CODE APPLIED!");
                disc_perc = 15;

                break;
            case "FOODY50":
                alt.setText("CODE APPLIED!");
                disc.setText("Discount (50%) :");
                disc_perc = 50;

                break;
            default:
                disc.setText("Discount (0%)  :");
                alt.setText("INVALID CODE!!");
                disc_perc = 0;
                break;
        }
        calculate();
    }

    public void calculate(){
        try {
            String query="SELECT sum(menu.price*quantity) as totalamount FROM orders JOIN menu ON orders.menu_id=menu.menu_id WHERE orders.customer_id="+i+" and order_status='ADDED_TO_CART'";
            ResultSet rs =con.createStatement().executeQuery(query);
            while(rs.next()){

                int amount=rs.getInt("totalamount");

                Amount.setText(Integer.toString(amount));
                System.out.println("amount "+amount);
                System.out.println("disc perc "+disc_perc);
                int disc_amt = (disc_perc*amount)/100;
                System.out.println("disc amt "+disc_amt);
                discount.setText(Integer.toString(disc_amt));
                 total=amount-disc_amt;
                System.out.println("amount "+total);
                totalAmount.setText(Integer.toString(total));

            }
        } catch (SQLException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //code below is load the payment screen
    @FXML
    public void PaymentScreen(ActionEvent event) throws Exception  {
        Stage primaryStage =new Stage();
        primaryStage.initStyle(StageStyle.UNDECORATED);
        Parent root =FXMLLoader.load(getClass().getResource("Payment.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Hide this current window (if this is what you want)
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }
    @FXML
    protected void send(ActionEvent event) throws SQLException {
        otps =otp.otpp();
        PreparedStatement preparedStatement = null ;
        ResultSet resultSet =null;
        String query="select email_id from customer where customer_id=? ";
        try {
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, MenuController.i);
            resultSet=preparedStatement.executeQuery();
            if(resultSet.next()){
                mail1 =resultSet.getString("email_id");
            }
            System.out.println(otps);
            System.out.println(mail1);
            mail.sendMail(mail1,otps);
            System.out.println("finnn");
        }
        catch (SQLException ex) {

            System.out.println(""+ex);
        }finally{
            preparedStatement.close();
            resultSet.close();
        }
    }
    public void UpdatePassword(ActionEvent event){

        String otp=otptxt.getText();
        String newpass=newpasstxt.getText();
        if(!(otp.isEmpty() || newpass.isEmpty())){
            if(otps.equals(otp)){
                type=infoBox("Do you really want to change the password",null,"Alter!");
                if(type){
                    menuModel.update_password(newpasstxt.getText());
                    infoBox1("Password changed sucessfully", null,"Success");
                    otptxt.clear();
                    newpasstxt.clear();
                }
            }else{
                infoBox1("Please enter your OTP correctly!!",null,"failed to change password");
            }
        }else{
            infoBox1("please Enter the Required Details", null,"Alert!");
        }

    }
    public void getAddress(){
        String query="SELECT state, city, landmark, pincode FROM  customer where customer_id="+i;
        PreparedStatement pst;
        ResultSet rs;
        try {
            pst =con.prepareStatement(query);
            rs=pst.executeQuery();
            if(rs.next()){
                statetxt.setText(rs.getString("state"));
                citytxt.setText(rs.getString("city"));
                landtxt.setText(rs.getString("landmark"));
                int pincode=rs.getInt("pincode");
                pincodetxt.setText(Integer.toString(pincode));
            }
        } catch (SQLException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void putAddress(ActionEvent event){
        if(!(statetxt.getText().isEmpty()||citytxt.getText().isEmpty()||landtxt.getText().isEmpty()||pincodetxt.getText().isEmpty())){
            type=infoBox("do you really want to update the Address",null,"Alert!");
            if(type){

                String query="Update customer SET state=? ,city=? ,landmark=? ,pincode=? where customer_id="+i;
                PreparedStatement pst;
                try {
                    pst =con.prepareStatement(query);
                    pst.setString(1,statetxt.getText());
                    pst.setString(2,citytxt.getText());
                    pst.setString(3,landtxt.getText());
                    pst.setInt(4,Integer.parseInt(pincodetxt.getText()));
                    pst.execute();
                    statetxt.clear();
                    citytxt.clear();
                    landtxt.clear();
                    pincodetxt.clear();
                    getAddress();
                    infoBox1("Address Sucessfully updated",null,"success");
                } catch (SQLException ex) {
                    Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }else{
                infoBox1("update cancelled",null,"cancelled");
                statetxt.clear();
                citytxt.clear();
                landtxt.clear();
                pincodetxt.clear();
                getAddress();
            }
        }else{
            infoBox1("please fill the address fields", null,"Update failed");
        }
    }
    public void paymentScreen(ActionEvent event) throws AWTException, IOException {
        bill.ss();
        if(menuModel.isItemInCart()){
            type=infoBox("Do you really want Confirm order",null,"Alert!");
            if(type){
                menuModel.update_status_to_confirmed();
                menuModel.copy_to_payment();
                try {
                    Node node = (Node)event.getSource();
                    Stage dialogStage = (Stage) node.getScene().getWindow();
                    dialogStage.close();
                    Scene scene;
                    scene = new Scene(FXMLLoader.load(getClass().getResource("../view/Payment.fxml")));
                    dialogStage.setScene(scene);
                    dialogStage.show();
                } catch (IOException ex) {
                    Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
                }

            }else{
                System.out.println("order is not placed");
            }
        }else{
            infoBox1("No items in the cart",null,"Alert!");
        }



    }

    public static boolean infoBox(String infoMessage, String headerText, String title){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText(infoMessage);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.getButtonTypes();

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            // ... user chose OK button
            return true;
        } else {
            // ... user chose CANCEL or closed the dialog
            return false;
        }

    }

    public static void infoBox1(String infoMessage, String headerText, String title){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(infoMessage);
        alert.showAndWait();
    }
    public  void Logout(ActionEvent event){
        System.exit(0);
    }
    public  void Logout1(ActionEvent event){
        System.exit(0);
    }

    public void Menu101(ActionEvent event){
        try {
            if(menuModel.check_if_added_to_cart(101)){
                type =infoBox("Item is already in the cart.\nDo you really want to add another one?",null,"Alert!" );
                // btn1.setDisable(true);
                if(type){
                    menuModel.increment_qnt(101);
                    tableConnection();
                    table.refresh();
                    calculate();
                    infoBox1("Item added to the cart!",null,"Success" );
                }
            }else{
                menuModel.cart_add(101);
                tableConnection();
                table.refresh();
                calculate();
                infoBox1("Item added to the cart!",null,"Success" );
                // btn1.setDisable(true);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void Menu102(ActionEvent event){
        try {
            if(menuModel.check_if_added_to_cart(102)){
                type =infoBox("Item is already in the cart.\nDo you really want to add another one?",null,"Alert!" );
                // btn1.setDisable(true);
                if(type){
                    menuModel.increment_qnt(102);
                    tableConnection();
                    table.refresh();
                    calculate();
                    infoBox1("Item added to the cart!",null,"Success" );
                }
            }else{
                menuModel.cart_add(102);
                tableConnection();
                table.refresh();
                calculate();
                infoBox1("Item added to the cart!",null,"Success" );
                // btn1.setDisable(true);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void Menu103(ActionEvent event){
        try {
            if(menuModel.check_if_added_to_cart(103)){
                type =infoBox("Item is already in the cart.\nDo you really want to add another one?",null,"Alert!" );
                // btn1.setDisable(true);
                if(type){
                    menuModel.increment_qnt(103);
                    tableConnection();
                    table.refresh();
                    calculate();
                    infoBox1("Item added to the cart!",null,"Success" );
                }
            }else{
                menuModel.cart_add(103);
                tableConnection();
                table.refresh();
                calculate();
                infoBox1("Item added to the cart!",null,"Success" );
                // btn1.setDisable(true);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void Menu104(ActionEvent event){
        try {
            if(menuModel.check_if_added_to_cart(104)){
                type =infoBox("Item is already in the cart.\nDo you really want to add another one?",null,"Alert!" );
                // btn1.setDisable(true);
                if(type){
                    menuModel.increment_qnt(104);
                    tableConnection();
                    table.refresh();
                    calculate();
                    infoBox1("Item added to the cart!",null,"Success" );
                }
            }else{
                menuModel.cart_add(104);
                tableConnection();
                table.refresh();
                calculate();
                infoBox1("Item added to the cart!",null,"Success" );
                // btn1.setDisable(true);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void Menu105(ActionEvent event){
        try {
            if(menuModel.check_if_added_to_cart(105)){
                type =infoBox("Item is already in the cart.\nDo you really want to add another one?",null,"Alert!" );
                // btn1.setDisable(true);
                if(type){
                    menuModel.increment_qnt(105);
                    tableConnection();
                    table.refresh();
                    calculate();
                    infoBox1("Item added to the cart!",null,"Success" );
                }
            }else{
                menuModel.cart_add(105);
                tableConnection();
                table.refresh();
                calculate();
                infoBox1("Item added to the cart!",null,"Success" );
                // btn1.setDisable(true);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void Menu106(ActionEvent event){
        try {
            if(menuModel.check_if_added_to_cart(106)){
                type =infoBox("Item is already in the cart.\nDo you really want to add another one?",null,"Alert!" );
                // btn1.setDisable(true);
                if(type){
                    menuModel.increment_qnt(106);
                    tableConnection();
                    table.refresh();
                    calculate();
                    infoBox1("Item added to the cart!",null,"Success" );
                }
            }else{
                menuModel.cart_add(106);
                tableConnection();
                table.refresh();
                calculate();
                infoBox1("Item added to the cart!",null,"Success" );
                // btn1.setDisable(true);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void Menu107(ActionEvent event){
        try {
            if(menuModel.check_if_added_to_cart(107)){
                type =infoBox("Item is already in the cart.\nDo you really want to add another one?",null,"Alert!" );
                // btn1.setDisable(true);
                if(type){
                    menuModel.increment_qnt(107);
                    tableConnection();
                    table.refresh();
                    calculate();
                    infoBox1("Item added to the cart!",null,"Success" );
                }
            }else{
                menuModel.cart_add(107);
                tableConnection();
                table.refresh();
                calculate();
                infoBox1("Item added to the cart!",null,"Success" );
                // btn1.setDisable(true);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void Menu108(ActionEvent event){
        try {
            if(menuModel.check_if_added_to_cart(108)){
                type =infoBox("Item is already in the cart.\nDo you really want to add another one?",null,"Alert!" );
                // btn1.setDisable(true);
                if(type){
                    menuModel.increment_qnt(108);
                    tableConnection();
                    table.refresh();
                    calculate();
                    infoBox1("Item added to the cart!",null,"Success" );
                }
            }else{
                menuModel.cart_add(108);
                tableConnection();
                table.refresh();
                calculate();
                infoBox1("Item added to the cart!",null,"Success" );
                // btn1.setDisable(true);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void Menu109(ActionEvent event){
        try {
            if(menuModel.check_if_added_to_cart(109)){
                type =infoBox("Item is already in the cart.\nDo you really want to add another one?",null,"Alert!" );
                // btn1.setDisable(true);
                if(type){
                    menuModel.increment_qnt(109);
                    tableConnection();
                    table.refresh();
                    calculate();
                    infoBox1("Item added to the cart!",null,"Success" );
                }
            }else{
                menuModel.cart_add(109);
                tableConnection();
                table.refresh();
                calculate();
                infoBox1("Item added to the cart!",null,"Success" );
                // btn1.setDisable(true);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void Menu110(ActionEvent event){
        try {
            if(menuModel.check_if_added_to_cart(110)){
                type =infoBox("Item is already in the cart.\nDo you really want to add another one?",null,"Alert!" );
                // btn1.setDisable(true);
                if(type){
                    menuModel.increment_qnt(110);
                    tableConnection();
                    table.refresh();
                    calculate();
                    infoBox1("Item added to the cart!",null,"Success" );
                }
            }else{
                menuModel.cart_add(110);
                tableConnection();
                table.refresh();
                calculate();
                infoBox1("Item added to the cart!",null,"Success" );
                // btn1.setDisable(true);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void Menu111(ActionEvent event){
        try {
            if(menuModel.check_if_added_to_cart(111)){
                type =infoBox("Item is already in the cart.\nDo you really want to add another one?",null,"Alert!" );
                // btn1.setDisable(true);
                if(type){
                    menuModel.increment_qnt(111);
                    tableConnection();
                    table.refresh();
                    calculate();
                    infoBox1("Item added to the cart!",null,"Success" );
                }
            }else{
                menuModel.cart_add(111);
                tableConnection();
                table.refresh();
                calculate();
                infoBox1("Item added to the cart!",null,"Success" );
                // btn1.setDisable(true);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void Menu112(ActionEvent event){
        try {
            if(menuModel.check_if_added_to_cart(112)){
                type =infoBox("Item is already in the cart.\nDo you really want to add another one?",null,"Alert!" );
                // btn1.setDisable(true);
                if(type){
                    menuModel.increment_qnt(112);
                    tableConnection();
                    table.refresh();
                    calculate();
                    infoBox1("Item added to the cart!",null,"Success" );
                }
            }else{
                menuModel.cart_add(112);
                tableConnection();
                table.refresh();
                calculate();
                infoBox1("Item added to the cart!",null,"Success" );
                // btn1.setDisable(true);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void Menu201(ActionEvent event){
        try {
            if(menuModel.check_if_added_to_cart(201)){
                type =infoBox("Item is already in the cart.\nDo you really want to add another one?",null,"Alert!" );
                // btn1.setDisable(true);
                if(type){
                    menuModel.increment_qnt(201);
                    tableConnection();
                    table.refresh();
                    calculate();
                    infoBox1("Item added to the cart!",null,"Success" );
                }
            }else{
                menuModel.cart_add(201);
                tableConnection();
                table.refresh();
                calculate();
                infoBox1("Item added to the cart!",null,"Success" );
                // btn1.setDisable(true);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void Menu202(ActionEvent event){
        try {
            if(menuModel.check_if_added_to_cart(202)){
                type =infoBox("Item is already in the cart.\nDo you really want to add another one?",null,"Alert!" );
                // btn1.setDisable(true);
                if(type){
                    menuModel.increment_qnt(202);
                    tableConnection();
                    table.refresh();
                    calculate();
                    infoBox1("Item added to the cart!",null,"Success" );
                }
            }else{
                menuModel.cart_add(202);
                tableConnection();
                table.refresh();
                calculate();
                infoBox1("Item added to the cart!",null,"Success" );
                // btn1.setDisable(true);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void Menu203(ActionEvent event){
        try {
            if(menuModel.check_if_added_to_cart(203)){
                type =infoBox("Item is already in the cart.\nDo you really want to add another one?",null,"Alert!" );
                // btn1.setDisable(true);
                if(type){
                    menuModel.increment_qnt(203);
                    tableConnection();
                    table.refresh();
                    calculate();
                    infoBox1("Item added to the cart!",null,"Success" );
                }
            }else{
                menuModel.cart_add(203);
                tableConnection();
                table.refresh();
                calculate();
                infoBox1("Item added to the cart!",null,"Success" );
                // btn1.setDisable(true);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void Menu204(ActionEvent event){
        try {
            if(menuModel.check_if_added_to_cart(204)){
                type =infoBox("Item is already in the cart.\nDo you really want to add another one?",null,"Alert!" );
                // btn1.setDisable(true);
                if(type){
                    menuModel.increment_qnt(204);
                    tableConnection();
                    table.refresh();
                    calculate();
                    infoBox1("Item added to the cart!",null,"Success" );
                }
            }else{
                menuModel.cart_add(204);
                tableConnection();
                table.refresh();
                calculate();
                infoBox1("Item added to the cart!",null,"Success" );
                // btn1.setDisable(true);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void Menu205(ActionEvent event){
        try {
            if(menuModel.check_if_added_to_cart(205)){
                type =infoBox("Item is already in the cart.\nDo you really want to add another one?",null,"Alert!" );
                // btn1.setDisable(true);
                if(type){
                    menuModel.increment_qnt(205);
                    tableConnection();
                    table.refresh();
                    calculate();
                    infoBox1("Item added to the cart!",null,"Success" );
                }
            }else{
                menuModel.cart_add(205);
                tableConnection();
                table.refresh();
                calculate();
                infoBox1("Item added to the cart!",null,"Success" );
                // btn1.setDisable(true);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void Menu206(ActionEvent event){
        try {
            if(menuModel.check_if_added_to_cart(206)){
                type =infoBox("Item is already in the cart.\nDo you really want to add another one?",null,"Alert!" );
                // btn1.setDisable(true);
                if(type){
                    menuModel.increment_qnt(206);
                    tableConnection();
                    table.refresh();
                    calculate();
                    infoBox1("Item added to the cart!",null,"Success" );
                }
            }else{
                menuModel.cart_add(206);
                tableConnection();
                table.refresh();
                calculate();
                infoBox1("Item added to the cart!",null,"Success" );
                // btn1.setDisable(true);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void Menu207(ActionEvent event){
        try {
            if(menuModel.check_if_added_to_cart(207)){
                type =infoBox("Item is already in the cart.\nDo you really want to add another one?",null,"Alert!" );
                // btn1.setDisable(true);
                if(type){
                    menuModel.increment_qnt(207);
                    tableConnection();
                    table.refresh();
                    calculate();
                    infoBox1("Item added to the cart!",null,"Success" );
                }
            }else{
                menuModel.cart_add(207);
                tableConnection();
                table.refresh();
                calculate();
                infoBox1("Item added to the cart!",null,"Success" );
                // btn1.setDisable(true);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void Menu208(ActionEvent event){
        try {
            if(menuModel.check_if_added_to_cart(208)){
                type =infoBox("Item is already in the cart.\nDo you really want to add another one?",null,"Alert!" );
                // btn1.setDisable(true);
                if(type){
                    menuModel.increment_qnt(208);
                    tableConnection();
                    table.refresh();
                    calculate();
                    infoBox1("Item added to the cart!",null,"Success" );
                }
            }else{
                menuModel.cart_add(208);
                tableConnection();
                table.refresh();
                calculate();
                infoBox1("Item added to the cart!",null,"Success" );
                // btn1.setDisable(true);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void Menu209(ActionEvent event){
        try {
            if(menuModel.check_if_added_to_cart(209)){
                type =infoBox("Item is already in the cart.\nDo you really want to add another one?",null,"Alert!" );
                // btn1.setDisable(true);
                if(type){
                    menuModel.increment_qnt(209);
                    tableConnection();
                    table.refresh();
                    calculate();
                    infoBox1("Item added to the cart!",null,"Success" );
                }
            }else{
                menuModel.cart_add(209);
                tableConnection();
                table.refresh();
                calculate();
                infoBox1("Item added to the cart!",null,"Success" );
                // btn1.setDisable(true);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void Menu210(ActionEvent event){
        try {
            if(menuModel.check_if_added_to_cart(210)){
                type =infoBox("Item is already in the cart.\nDo you really want to add another one?",null,"Alert!" );
                // btn1.setDisable(true);
                if(type){
                    menuModel.increment_qnt(210);
                    tableConnection();
                    table.refresh();
                    calculate();
                    infoBox1("Item added to the cart!",null,"Success" );
                }
            }else{
                menuModel.cart_add(210);
                tableConnection();
                table.refresh();
                calculate();
                infoBox1("Item added to the cart!",null,"Success" );
                // btn1.setDisable(true);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void Menu211(ActionEvent event){
        try {
            if(menuModel.check_if_added_to_cart(211)){
                type =infoBox("Item is already in the cart.\nDo you really want to add another one?",null,"Alert!" );
                // btn1.setDisable(true);
                if(type){
                    menuModel.increment_qnt(211);
                    tableConnection();
                    table.refresh();
                    calculate();
                    infoBox1("Item added to the cart!",null,"Success" );
                }
            }else{
                menuModel.cart_add(211);
                tableConnection();
                table.refresh();
                calculate();
                infoBox1("Item added to the cart!",null,"Success" );
                // btn1.setDisable(true);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void Menu212(ActionEvent event){
        try {
            if(menuModel.check_if_added_to_cart(212)){
                type =infoBox("Item is already in the cart.\nDo you really want to add another one?",null,"Alert!" );
                // btn1.setDisable(true);
                if(type){
                    menuModel.increment_qnt(212);
                    tableConnection();
                    table.refresh();
                    calculate();
                    infoBox1("Item added to the cart!",null,"Success" );
                }
            }else{
                menuModel.cart_add(212);
                tableConnection();
                table.refresh();
                calculate();
                infoBox1("Item added to the cart!",null,"Success" );
                // btn1.setDisable(true);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    public void Menu301(ActionEvent event){
        try {
            if(menuModel.check_if_added_to_cart(301)){
                type =infoBox("Item is already in the cart.\nDo you really want to add another one?",null,"Alert!" );
                // btn1.setDisable(true);
                if(type){
                    menuModel.increment_qnt(301);
                    tableConnection();
                    table.refresh();
                    calculate();
                    infoBox1("Item added to the cart!",null,"Success" );
                }
            }else{
                menuModel.cart_add(301);
                tableConnection();
                table.refresh();
                calculate();
                infoBox1("Item added to the cart!",null,"Success" );
                // btn1.setDisable(true);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void Menu302(ActionEvent event){
        try {
            if(menuModel.check_if_added_to_cart(302)){
                type =infoBox("Item is already in the cart.\nDo you really want to add another one?",null,"Alert!" );
                // btn1.setDisable(true);
                if(type){
                    menuModel.increment_qnt(302);
                    tableConnection();
                    table.refresh();
                    calculate();
                    infoBox1("Item added to the cart!",null,"Success" );
                }
            }else{
                menuModel.cart_add(302);
                tableConnection();
                table.refresh();
                calculate();
                infoBox1("Item added to the cart!",null,"Success" );
                // btn1.setDisable(true);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void Menu303(ActionEvent event){
        try {
            if(menuModel.check_if_added_to_cart(303)){
                type =infoBox("Item is already in the cart.\nDo you really want to add another one?",null,"Alert!" );
                // btn1.setDisable(true);
                if(type){
                    menuModel.increment_qnt(303);
                    tableConnection();
                    table.refresh();
                    calculate();
                    infoBox1("Item added to the cart!",null,"Success" );
                }
            }else{
                menuModel.cart_add(303);
                tableConnection();
                table.refresh();
                calculate();
                infoBox1("Item added to the cart!",null,"Success" );
                // btn1.setDisable(true);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void Menu304(ActionEvent event){
        try {
            if(menuModel.check_if_added_to_cart(304)){
                type =infoBox("Item is already in the cart.\nDo you really want to add another one?",null,"Alert!" );
                // btn1.setDisable(true);
                if(type){
                    menuModel.increment_qnt(304);
                    tableConnection();
                    table.refresh();
                    calculate();
                    infoBox1("Item added to the cart!",null,"Success" );
                }
            }else{
                menuModel.cart_add(304);
                tableConnection();
                table.refresh();
                calculate();
                infoBox1("Item added to the cart!",null,"Success" );
                // btn1.setDisable(true);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void Menu305(ActionEvent event){
        try {
            if(menuModel.check_if_added_to_cart(305)){
                type =infoBox("Item is already in the cart.\nDo you really want to add another one?",null,"Alert!" );
                // btn1.setDisable(true);
                if(type){
                    menuModel.increment_qnt(305);
                    tableConnection();
                    table.refresh();
                    calculate();
                    infoBox1("Item added to the cart!",null,"Success" );
                }
            }else{
                menuModel.cart_add(305);
                tableConnection();
                table.refresh();
                calculate();
                infoBox1("Item added to the cart!",null,"Success" );
                // btn1.setDisable(true);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void Menu306(ActionEvent event){
        try {
            if(menuModel.check_if_added_to_cart(306)){
                type =infoBox("Item is already in the cart.\nDo you really want to add another one?",null,"Alert!" );
                // btn1.setDisable(true);
                if(type){
                    menuModel.increment_qnt(306);
                    tableConnection();
                    table.refresh();
                    calculate();
                    infoBox1("Item added to the cart!",null,"Success" );
                }
            }else{
                menuModel.cart_add(306);
                tableConnection();
                table.refresh();
                calculate();
                infoBox1("Item added to the cart!",null,"Success" );
                // btn1.setDisable(true);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void Menu307(ActionEvent event){
        try {
            if(menuModel.check_if_added_to_cart(307)){
                type =infoBox("Item is already in the cart.\nDo you really want to add another one?",null,"Alert!" );
                // btn1.setDisable(true);
                if(type){
                    menuModel.increment_qnt(307);
                    tableConnection();
                    table.refresh();
                    calculate();
                    infoBox1("Item added to the cart!",null,"Success" );
                }
            }else{
                menuModel.cart_add(307);
                tableConnection();
                table.refresh();
                calculate();
                infoBox1("Item added to the cart!",null,"Success" );
                // btn1.setDisable(true);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void Menu308(ActionEvent event){
        try {
            if(menuModel.check_if_added_to_cart(308)){
                type =infoBox("Item is already in the cart.\nDo you really want to add another one?",null,"Alert!" );
                // btn1.setDisable(true);
                if(type){
                    menuModel.increment_qnt(308);
                    tableConnection();
                    table.refresh();
                    calculate();
                    infoBox1("Item added to the cart!",null,"Success" );
                }
            }else{
                menuModel.cart_add(308);
                tableConnection();
                table.refresh();
                calculate();
                infoBox1("Item added to the cart!",null,"Success" );
                // btn1.setDisable(true);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void Menu309(ActionEvent event){
        try {
            if(menuModel.check_if_added_to_cart(309)){
                type =infoBox("Item is already in the cart.\nDo you really want to add another one?",null,"Alert!" );
                // btn1.setDisable(true);
                if(type){
                    menuModel.increment_qnt(309);
                    tableConnection();
                    table.refresh();
                    calculate();
                    infoBox1("Item added to the cart!",null,"Success" );
                }
            }else{
                menuModel.cart_add(309);
                tableConnection();
                table.refresh();
                calculate();
                infoBox1("Item added to the cart!",null,"Success" );
                // btn1.setDisable(true);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void Menu310(ActionEvent event){
        try {
            if(menuModel.check_if_added_to_cart(310)){
                type =infoBox("Item is already in the cart.\nDo you really want to add another one?",null,"Alert!" );
                // btn1.setDisable(true);
                if(type){
                    menuModel.increment_qnt(310);
                    tableConnection();
                    table.refresh();
                    calculate();
                    infoBox1("Item added to the cart!",null,"Success" );
                }
            }else{
                menuModel.cart_add(310);
                tableConnection();
                table.refresh();
                calculate();
                infoBox1("Item added to the cart!",null,"Success" );
                // btn1.setDisable(true);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void Menu311(ActionEvent event){
        try {
            if(menuModel.check_if_added_to_cart(311)){
                type =infoBox("Item is already in the cart.\nDo you really want to add another one?",null,"Alert!" );
                // btn1.setDisable(true);
                if(type){
                    menuModel.increment_qnt(311);
                    tableConnection();
                    table.refresh();
                    calculate();
                    infoBox1("Item added to the cart!",null,"Success" );
                }
            }else{
                menuModel.cart_add(311);
                tableConnection();
                table.refresh();
                calculate();
                infoBox1("Item added to the cart!",null,"Success" );
                // btn1.setDisable(true);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void Menu312(ActionEvent event){
        try {
            if(menuModel.check_if_added_to_cart(312)){
                type =infoBox("Item is already in the cart.\nDo you really want to add another one?",null,"Alert!" );
                // btn1.setDisable(true);
                if(type){
                    menuModel.increment_qnt(312);
                    tableConnection();
                    table.refresh();
                    calculate();
                    infoBox1("Item added to the cart!",null,"Success" );
                }
            }else{
                menuModel.cart_add(312);
                tableConnection();
                table.refresh();
                calculate();
                infoBox1("Item added to the cart!",null,"Success" );
                // btn1.setDisable(true);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void Menu401(ActionEvent event){
        try {
            if(menuModel.check_if_added_to_cart(401)){
                type =infoBox("Item is already in the cart.\nDo you really want to add another one?",null,"Alert!" );
                // btn1.setDisable(true);
                if(type){
                    menuModel.increment_qnt(401);
                    tableConnection();
                    table.refresh();
                    calculate();
                    infoBox1("Item added to the cart!",null,"Success" );
                }
            }else{
                menuModel.cart_add(401);
                tableConnection();
                table.refresh();
                calculate();
                infoBox1("Item added to the cart!",null,"Success" );
                // btn1.setDisable(true);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void Menu402(ActionEvent event){
        try {
            if(menuModel.check_if_added_to_cart(402)){
                type =infoBox("Item is already in the cart.\nDo you really want to add another one?",null,"Alert!" );
                // btn1.setDisable(true);
                if(type){
                    menuModel.increment_qnt(402);
                    tableConnection();
                    table.refresh();
                    calculate();
                    infoBox1("Item added to the cart!",null,"Success" );
                }
            }else{
                menuModel.cart_add(402);
                tableConnection();
                table.refresh();
                calculate();
                infoBox1("Item added to the cart!",null,"Success" );
                // btn1.setDisable(true);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void Menu403(ActionEvent event){
        try {
            if(menuModel.check_if_added_to_cart(403)){
                type =infoBox("Item is already in the cart.\nDo you really want to add another one?",null,"Alert!" );
                // btn1.setDisable(true);
                if(type){
                    menuModel.increment_qnt(403);
                    tableConnection();
                    table.refresh();
                    calculate();
                    infoBox1("Item added to the cart!",null,"Success" );
                }
            }else{
                menuModel.cart_add(403);
                tableConnection();
                table.refresh();
                calculate();
                infoBox1("Item added to the cart!",null,"Success" );
                // btn1.setDisable(true);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void Menu404(ActionEvent event){
        try {
            if(menuModel.check_if_added_to_cart(404)){
                type =infoBox("Item is already in the cart.\nDo you really want to add another one?",null,"Alert!" );
                // btn1.setDisable(true);
                if(type){
                    menuModel.increment_qnt(404);
                    tableConnection();
                    table.refresh();
                    calculate();
                    infoBox1("Item added to the cart!",null,"Success" );
                }
            }else{
                menuModel.cart_add(404);
                tableConnection();
                table.refresh();
                calculate();
                infoBox1("Item added to the cart!",null,"Success" );
                // btn1.setDisable(true);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void Menu405(ActionEvent event){
        try {
            if(menuModel.check_if_added_to_cart(405)){
                type =infoBox("Item is already in the cart.\nDo you really want to add another one?",null,"Alert!" );
                // btn1.setDisable(true);
                if(type){
                    menuModel.increment_qnt(405);
                    tableConnection();
                    table.refresh();
                    calculate();
                    infoBox1("Item added to the cart!",null,"Success" );
                }
            }else{
                menuModel.cart_add(405);
                tableConnection();
                table.refresh();
                calculate();
                infoBox1("Item added to the cart!",null,"Success" );
                // btn1.setDisable(true);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void Menu406(ActionEvent event){
        try {
            if(menuModel.check_if_added_to_cart(406)){
                type =infoBox("Item is already in the cart.\nDo you really want to add another one?",null,"Alert!" );
                // btn1.setDisable(true);
                if(type){
                    menuModel.increment_qnt(406);
                    tableConnection();
                    table.refresh();
                    calculate();
                    infoBox1("Item added to the cart!",null,"Success" );
                }
            }else{
                menuModel.cart_add(406);
                tableConnection();
                table.refresh();
                calculate();
                infoBox1("Item added to the cart!",null,"Success" );
                // btn1.setDisable(true);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void Menu407(ActionEvent event){
        try {
            if(menuModel.check_if_added_to_cart(407)){
                type =infoBox("Item is already in the cart.\nDo you really want to add another one?",null,"Alert!" );
                // btn1.setDisable(true);
                if(type){
                    menuModel.increment_qnt(407);
                    tableConnection();
                    table.refresh();
                    calculate();
                    infoBox1("Item added to the cart!",null,"Success" );
                }
            }else{
                menuModel.cart_add(407);
                tableConnection();
                table.refresh();
                calculate();
                infoBox1("Item added to the cart!",null,"Success" );
                // btn1.setDisable(true);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void Menu408(ActionEvent event){
        try {
            if(menuModel.check_if_added_to_cart(408)){
                type =infoBox("Item is already in the cart.\nDo you really want to add another one?",null,"Alert!" );
                // btn1.setDisable(true);
                if(type){
                    menuModel.increment_qnt(408);
                    tableConnection();
                    table.refresh();
                    calculate();
                    infoBox1("Item added to the cart!",null,"Success" );
                }
            }else{
                menuModel.cart_add(408);
                tableConnection();
                table.refresh();
                calculate();
                infoBox1("Item added to the cart!",null,"Success" );
                // btn1.setDisable(true);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void Menu409(ActionEvent event){
        try {
            if(menuModel.check_if_added_to_cart(409)){
                type =infoBox("Item is already in the cart.\nDo you really want to add another one?",null,"Alert!" );
                // btn1.setDisable(true);
                if(type){
                    menuModel.increment_qnt(409);
                    tableConnection();
                    table.refresh();
                    calculate();
                    infoBox1("Item added to the cart!",null,"Success" );
                }
            }else{
                menuModel.cart_add(409);
                tableConnection();
                table.refresh();
                calculate();
                infoBox1("Item added to the cart!",null,"Success" );
                // btn1.setDisable(true);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void Menu410(ActionEvent event){
        try {
            if(menuModel.check_if_added_to_cart(410)){
                type =infoBox("Item is already in the cart.\nDo you really want to add another one?",null,"Alert!" );
                // btn1.setDisable(true);
                if(type){
                    menuModel.increment_qnt(410);
                    tableConnection();
                    table.refresh();
                    calculate();
                    infoBox1("Item added to the cart!",null,"Success" );
                }
            }else{
                menuModel.cart_add(410);
                tableConnection();
                table.refresh();
                calculate();
                infoBox1("Item added to the cart!",null,"Success" );
                // btn1.setDisable(true);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void Menu411(ActionEvent event){
        try {
            if(menuModel.check_if_added_to_cart(411)){
                type =infoBox("Item is already in the cart.\nDo you really want to add another one?",null,"Alert!" );
                // btn1.setDisable(true);
                if(type){
                    menuModel.increment_qnt(411);
                    tableConnection();
                    table.refresh();
                    calculate();
                    infoBox1("Item added to the cart!",null,"Success" );
                }
            }else{
                menuModel.cart_add(411);
                tableConnection();
                table.refresh();
                calculate();
                infoBox1("Item added to the cart!",null,"Success" );
                // btn1.setDisable(true);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void Menu412(ActionEvent event){
        try {
            if(menuModel.check_if_added_to_cart(412)){
                type =infoBox("Item is already in the cart.\nDo you really want to add another one?",null,"Alert!" );
                // btn1.setDisable(true);
                if(type){
                    menuModel.increment_qnt(412);
                    tableConnection();
                    table.refresh();
                    calculate();
                    infoBox1("Item added to the cart!",null,"Success" );
                }
            }else{
                menuModel.cart_add(412);
                tableConnection();
                table.refresh();
                calculate();
                infoBox1("Item added to the cart!",null,"Success" );
                // btn1.setDisable(true);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }



}
