package saba;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.*;

public class LibrarySystem extends JFrame implements ActionListener {
    private CardLayout cardLayout;
    private JPanel cardPanel, loginPanel, adminPanel, userPanel;
    private JTextField adminUsernameField, adminPasswordField, userUsernameField, userPasswordField;
    private JButton adminLoginButton, userLoginButton, logoutButton;
    private JLabel titleLabel, studentLabel, bookLabel, issueDateLabel, returnDateLabel, studentNameLabel;
    private JTextField studentField, bookField, issueDateField, returnDateField, studentNameField;
    private JButton issueButton, returnButton;

    private Connection connection;

    public LibrarySystem() {
        setTitle("Library System");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        initLoginPanel();
        initAdminPanel();
        initUserPanel();

        cardPanel.add(loginPanel, "login");
        cardPanel.add(adminPanel, "admin");
        cardPanel.add(userPanel, "user");

        connection = connectToDatabase();

        add(cardPanel);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                dispose();
                closeDatabaseConnection();
                System.exit(0);
            }
        });

        setVisible(true);
    }

    private void initLoginPanel() {
        loginPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel titleLabel = new JLabel("Library System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));

        adminUsernameField = new JTextField(15);
        adminPasswordField = new JPasswordField(15);
        userUsernameField = new JTextField(15);
        userPasswordField = new JPasswordField(15);

        adminLoginButton = new JButton("Admin Login");
        userLoginButton = new JButton("User Login");

        adminLoginButton.addActionListener(this);
        userLoginButton.addActionListener(this);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        loginPanel.add(titleLabel, gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 1;
        loginPanel.add(new JLabel("Admin Username:"), gbc);

        gbc.gridx = 1;
        loginPanel.add(adminUsernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        loginPanel.add(new JLabel("Admin Password:"), gbc);

        gbc.gridx = 1;
        loginPanel.add(adminPasswordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        loginPanel.add(new JLabel("User Username:"), gbc);

        gbc.gridx = 1;
        loginPanel.add(userUsernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        loginPanel.add(new JLabel("User Password:"), gbc);

        gbc.gridx = 1;
        loginPanel.add(userPasswordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        loginPanel.add(adminLoginButton, gbc);

        gbc.gridy = 6;
        loginPanel.add(userLoginButton, gbc);
    }

    private void initAdminPanel() {
        adminPanel = new JPanel();
        adminPanel.setLayout(new FlowLayout());

        studentLabel = new JLabel("Student ID: ");
        bookLabel = new JLabel("Book ID: ");
        issueDateLabel = new JLabel("Issue Date (yyyy-MM-dd): ");
        returnDateLabel = new JLabel("Return Date (yyyy-MM-dd): ");
        studentNameLabel = new JLabel("Student Name: ");

        studentField = new JTextField(10);
        bookField = new JTextField(10);
        issueDateField = new JTextField(10);
        returnDateField = new JTextField(10);
        studentNameField = new JTextField(10);

        issueButton = new JButton("Issue Book");
        returnButton = new JButton("Return Book");
        logoutButton = new JButton("Logout");

        issueButton.addActionListener(this);
        returnButton.addActionListener(this);
        logoutButton.addActionListener(this);

        // Set background colors for buttons
        issueButton.setBackground(new Color(30, 130, 76)); // Green
        issueButton.setForeground(Color.WHITE);

        returnButton.setBackground(new Color(192, 57, 43)); // Red
        returnButton.setForeground(Color.WHITE);

        logoutButton.setBackground(new Color(52, 152, 219)); // Blue
        logoutButton.setForeground(Color.WHITE);

        adminPanel.add(studentLabel);
        adminPanel.add(studentField);
        adminPanel.add(studentNameLabel);
        adminPanel.add(studentNameField);
        adminPanel.add(bookLabel);
        adminPanel.add(bookField);
        adminPanel.add(issueDateLabel);
        adminPanel.add(issueDateField);
        adminPanel.add(returnDateLabel);
        adminPanel.add(returnDateField);
        adminPanel.add(issueButton);
        adminPanel.add(returnButton);
        adminPanel.add(logoutButton);
    }

    private void initUserPanel() {
        userPanel = new JPanel();
        userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));

        JLabel userLabel = new JLabel("Welcome, User!");
        userLabel.setFont(new Font("Arial", Font.BOLD, 20));

        // Dummy list of books (you can replace this with your actual book details)
        String[] books = {"Book 1 - Author 1", "Book 2 - Author 2", "Book 3 - Author 3"};

        JComboBox<String> bookComboBox = new JComboBox<>(books);
        JButton showBookDetailsButton = new JButton("Show Book Details");

        showBookDetailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the selected book from the combo box
                String selectedBook = (String) bookComboBox.getSelectedItem();

                // Display the details of the selected book (you can customize this part)
                JOptionPane.showMessageDialog(userPanel, "Book Details:\n" + selectedBook,
                        "Book Details", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        logoutButton = new JButton("Logout");
        logoutButton.addActionListener(this);

        userPanel.add(userLabel);
        userPanel.add(bookComboBox);
        userPanel.add(showBookDetailsButton);
        userPanel.add(logoutButton);
    }


    public Connection connectToDatabase() {
        try {
            String url = "jdbc:mysql://localhost:3307/saba";
            String username = "root";
            String password = "sabakhan";
            Connection con = DriverManager.getConnection(url, username, password);
            return con;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void closeDatabaseConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == adminLoginButton) {
            cardLayout.show(cardPanel, "admin");
        } else if (e.getSource() == userLoginButton) {
            cardLayout.show(cardPanel, "user");
        } else if (e.getSource() == logoutButton) {
            cardLayout.show(cardPanel, "login");
        } else if (e.getSource() == issueButton) {
            issueBook();
        } else if (e.getSource() == returnButton) {
            returnBook();
        }
    }

    public void issueBook() {
        int studentID = Integer.parseInt(studentField.getText());
        int bookID = Integer.parseInt(bookField.getText());
        String issueDateStr = issueDateField.getText();
        String studentName = studentNameField.getText();

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date issueDate = dateFormat.parse(issueDateStr);

            String query = "INSERT INTO transactions (student_id, student_name, book_id, issue_date) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, studentID);
            preparedStatement.setString(2, studentName);
            preparedStatement.setInt(3, bookID);
            preparedStatement.setDate(4, new java.sql.Date(issueDate.getTime()));
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException | ParseException ex) {
            ex.printStackTrace();
        }
    }

    public void returnBook() {
        int studentID = Integer.parseInt(studentField.getText());
        int bookID = Integer.parseInt(bookField.getText());
        String returnDateStr = returnDateField.getText();

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date returnDate = dateFormat.parse(returnDateStr);

            String query = "UPDATE transactions SET return_date = ? WHERE student_id = ? AND book_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setDate(1, new java.sql.Date(returnDate.getTime()));
            preparedStatement.setInt(2, studentID);
            preparedStatement.setInt(3, bookID);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException | ParseException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new LibrarySystem();
    }
}

