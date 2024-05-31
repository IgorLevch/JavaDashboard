import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class RegistrationForm extends JDialog{
    private JTextField tfName;
    private JTextField tfEmail;
    private JTextField tfAddress;
    private JTextField tfPhone;
    private JPasswordField pfPassword;
    private JPasswordField pfConfirmPassword;
    private JButton btnRegister;
    private JButton btnCancel;
    private JPanel registedPanel;


    public RegistrationForm(JFrame parent) {  //JFrame покажет данный Dialog
        super(parent); // a parent JFrame constructor
        setTitle("Crete a new account");  // a title to this dialogue
        setContentPane(registedPanel);  // to display a panel inside this dialogue
        setMinimumSize(new Dimension(450,474)); // minimal size of the dialogue
        setModal(true);
        setLocationRelativeTo(parent); // to display the dialogue in the middle of the Frame
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);


        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        setVisible(true);
    }

    private void registerUser() {
        //need to read all the fields:
        String name = tfName.getText();
        String email = tfEmail.getText();
        String phone = tfPhone.getText();
        String address = tfAddress.getText();
        String password = String.valueOf(pfPassword.getPassword());
        String confirmPassword = String.valueOf(pfConfirmPassword.getPassword());


        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty() || password.isEmpty()){
            JOptionPane.showMessageDialog(this,
                    "Please, enter all fields",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);

            return;

        }

        if (!password.equals(confirmPassword)){

            JOptionPane.showMessageDialog(this,
                    "Confirm password doesn't mathc",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);

            return;
        }
        user = addUserToDatabase(name,email,phone,address,password);
            if (user !=null){
                dispose();
            } else{
                JOptionPane.showMessageDialog(this,
                        "Failed to register a new user",
                        "Try again",
                        JOptionPane.ERROR_MESSAGE);
            }
    }
    public User user;
    private User addUserToDatabase(String name,String email,String phone,String address,String password){
        User user = null;
        final String DB_URL = "jdbc:mysql://localhost/mystore";
        final String USERNAME = "root";
        final String PASSWORD = "as56df";

        try{

            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

            Statement stmt = conn.createStatement();
            String sql = "INSERT INTO users(name, email, phone, address, password)" +
                    "VALUES (?,?,?,?,?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, phone);
            preparedStatement.setString(4, address);
            preparedStatement.setString(5, password);

           int addedRows = preparedStatement.executeUpdate();

            if(addedRows>0){

                user = new User();
                user.name = name;
                user.email = email;
                user.phone = phone;
                user.address = address;
                user.password = password;
            }

            stmt.close();
            conn.close();
        }   catch(Exception e){

            System.out.println("Database connection failed!");
        }
        return user;
    }

    public static void main(String[] args) {
        RegistrationForm myForm  = new RegistrationForm(null);

        User user = myForm.user;
        if (user != null){
            System.out.println("Successfull registration of : " + user.name);

        }
        else{
            System.out.println("Registration cancelled");
        }


    }



}
