import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class HospitalManagementSystem {
    private JFrame frame;
    private JTextField nameField, ageField, contactField;
    private JComboBox<String> genderBox;
    private Connection conn;

    public HospitalManagementSystem() {
        frame = new JFrame("Hospital Management System");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(5, 2));

        JLabel nameLabel = new JLabel("Name:");
        nameField = new JTextField();
        JLabel ageLabel = new JLabel("Age:");
        ageField = new JTextField();
        JLabel genderLabel = new JLabel("Gender:");
        genderBox = new JComboBox<>(new String[]{"Male", "Female"});
        JLabel contactLabel = new JLabel("Contact:");
        contactField = new JTextField();
        JButton addButton = new JButton("Add Patient");
        JButton viewButton = new JButton("View Patients");

        addButton.addActionListener(e -> addPatient());
        viewButton.addActionListener(e -> viewPatients());

        frame.add(nameLabel); frame.add(nameField);
        frame.add(ageLabel); frame.add(ageField);
        frame.add(genderLabel); frame.add(genderBox);
        frame.add(contactLabel); frame.add(contactField);
        frame.add(addButton); frame.add(viewButton);

        frame.setVisible(true);
        connectDatabase();
    }

    private void connectDatabase() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/hospital", "root", "password");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Database Connection Failed!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addPatient() {
        String name = nameField.getText();
        int age = Integer.parseInt(ageField.getText());
        String gender = genderBox.getSelectedItem().toString();
        String contact = contactField.getText();

        try {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO patients (name, age, gender, contact) VALUES (?, ?, ?, ?)");
            stmt.setString(1, name);
            stmt.setInt(2, age);
            stmt.setString(3, gender);
            stmt.setString(4, contact);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(frame, "Patient Added Successfully!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Error Adding Patient!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void viewPatients() {
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM patients");
            StringBuilder result = new StringBuilder("Patient List:\n");
            while (rs.next()) {
                result.append(rs.getInt("id")).append(" - ")
                        .append(rs.getString("name")).append(", ")
                        .append(rs.getInt("age")).append(" years, ")
                        .append(rs.getString("gender")).append(", Contact: ")
                        .append(rs.getString("contact")).append("\n");
            }
            JOptionPane.showMessageDialog(frame, result.toString());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Error Fetching Data!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new HospitalManagementSystem();
    }
}


