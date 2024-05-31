import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class DashBoardForm extends JFrame {
    private JPanel dashboardPanel;
    private JLabel lbAdmin;
    private JButton btnRegister;

    public DashBoardForm() {

        setTitle("Dashboard");
        setContentPane(dashboardPanel); // display the Panel
        setMinimumSize(new Dimension(500,429));
        setSize(1200,700);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // when we click on a CLOSE button,
        // the application ll be terminated


         // returns true if we have users in the DB,  otherwise returns false
      boolean hasRegisteredUsers  =  connectToDatabase();
      // if we have users in the DB we need to display LF:
        if (hasRegisteredUsers){
            LoginForm loginFrom = new LoginForm(this);
            User user =  loginFrom.user;

            if (user!=null){

                lbAdmin.setText("User: "+user.name);
                setLocationRelativeTo(null);
                setVisible(true);

            } else {
                dispose();
            }
        }
        else{
            RegistrationForm regForm = new RegistrationForm(this);
            User user = regForm.user;

            if (user!=null){

                lbAdmin.setText("User: "+user.name);
                setLocationRelativeTo(null);
                setVisible(true);

            } else {
                dispose();
            }

        }


        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RegistrationForm registrationForm = new RegistrationForm(DashBoardForm.this);
                User user = registrationForm.user;

                if (user != null) {
                    JOptionPane.showMessageDialog(DashBoardForm.this,
                            "New user: "+user.name,
                            "Successfull registration",
                            JOptionPane.INFORMATION_MESSAGE
                            );

                }

            }
        });
    }

    private boolean connectToDatabase(){
        boolean hasRegisteredUsers = false;

        final String MYSQL_SERVER_URL = "jdbc:mysql://localhost/";
        final String DB_URL = "jdbc:mysql://localhost/mystore";
        final String USERNAME = "root";
        final String PASSWORD = "as56df";

        try {

            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            Statement statement = conn.createStatement();
           // statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM users"); // the statement
            // allows us to find the number of registered users
            // if the number is higher than zero, we ll set the variable "hasRegisteredUsers" = true

            if (resultSet.next()){
                int numUsers = resultSet.getInt(1);
                if (numUsers>0){
                    hasRegisteredUsers = true;
                }

            }

            statement.close();
            conn.close();

        } catch (Exception e) {
           e.printStackTrace();
        }

        return  hasRegisteredUsers;

    }

    public static void main(String[] args) {
        DashBoardForm myForm = new DashBoardForm();
    }
}
