import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.*;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class RG extends JFrame {

    static class Car {
        int id;
        String model;
        String plateNumber;
        boolean isAvailable;

        public Car(int id, String model, String plateNumber) {
            this.id = id;
            this.model = model;
            this.plateNumber = plateNumber;
            this.isAvailable = true;
        }

        public String toString() {
            return id + " - " + model + " (" + plateNumber + ")" + (isAvailable ? " [Available]" : " [Rented]");
        }
    }

    private final List<Car> cars = new ArrayList<>();
    private final DefaultListModel<String> carListModel = new DefaultListModel<>();
    private final JList<String> carList = new JList<>(carListModel);
    private final JLabel carImageLabel = new JLabel();

    private final int DAILY_RATE = 3000;
    private final int HOURLY_RATE = 500;
    private final String LOG_FILE = "rental_log.txt";
    private final String[] CITIES = {"Delhi", "Ghaziabad", "Greater Noida", "Gurgaon", "Haridwar", "Jaipur", "Meerut", "Sonipat"};

    public RG() {
        setTitle("RentGO - Car Rental System");
        setSize(900, 520);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Add sample cars
        cars.add(new Car(1, "Toyota Innova", "UP12AB1234"));
        cars.add(new Car(2, "Honda Civic", "HR05XY9876"));
        cars.add(new Car(3, "Hyundai i20", "DL01CD5678")); // Blocked plate
        cars.add(new Car(4, "Mahindra XUV 700", "UP07PQ4321"));
        cars.add(new Car(5, "Hyundai Creta", "RJ14GH2345"));
        cars.add(new Car(6, "Mahindra Thar", "UP16YK9167"));

        // Title
        JLabel titleLabel = new JLabel("Available Cars - RentGO", JLabel.CENTER);
        titleLabel.setFont(new Font("Verdana", Font.BOLD, 24));
        titleLabel.setOpaque(true);
        titleLabel.setBackground(new Color(0, 102, 204));
        titleLabel.setForeground(Color.white);
        titleLabel.setBorder(new LineBorder(Color.black, 2));
        titleLabel.setPreferredSize(new Dimension(0, 50));
        add(titleLabel, BorderLayout.NORTH);

        refreshCarList();

        carList.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        carList.setSelectionBackground(new Color(0, 153, 51));
        carList.setSelectionForeground(Color.white);
        carList.setFixedCellHeight(30);
        JScrollPane listScroll = new JScrollPane(carList);
        listScroll.setPreferredSize(new Dimension(400, 400));
        listScroll.setBorder(BorderFactory.createTitledBorder("Car List"));
        add(listScroll, BorderLayout.WEST);

        carImageLabel.setHorizontalAlignment(JLabel.CENTER);
        carImageLabel.setPreferredSize(new Dimension(450, 350));
        carImageLabel.setBorder(BorderFactory.createTitledBorder("Car Image"));
        carImageLabel.setText("No Car Selected");
        add(carImageLabel, BorderLayout.CENTER);

        carList.addListSelectionListener(e -> updateCarImage());

        // Buttons panel
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(230, 230, 250));
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton rentButton = new JButton("Rent Selected Car");
        JButton returnButton = new JButton("Return Selected Car");
        JButton viewLogsButton = new JButton("View Rental Logs");
        JButton supportButton = new JButton("Customer Support");
        JButton fleetButton = new JButton("Fleet Management");

        rentButton.setBackground(new Color(0, 153, 76));
        returnButton.setBackground(new Color(204, 51, 51));
        viewLogsButton.setBackground(new Color(0, 102, 204));
        supportButton.setBackground(new Color(102, 51, 153));
        fleetButton.setBackground(new Color(255, 140, 0));

        Color fg = Color.white;
        Font btnFont = new Font("Tahoma", Font.BOLD, 14);
        rentButton.setForeground(fg); rentButton.setFont(btnFont);
        returnButton.setForeground(fg); returnButton.setFont(btnFont);
        viewLogsButton.setForeground(fg); viewLogsButton.setFont(btnFont);
        supportButton.setForeground(fg); supportButton.setFont(btnFont);
        fleetButton.setForeground(fg); fleetButton.setFont(btnFont);

        rentButton.addActionListener(e -> rentSelectedCar());
        returnButton.addActionListener(e -> returnSelectedCar());
        viewLogsButton.addActionListener(e -> viewRentalLogs());
        supportButton.addActionListener(e -> showCustomerSupport());
        fleetButton.addActionListener(e -> showFleetManagement());

        bottomPanel.add(rentButton);
        bottomPanel.add(returnButton);
        bottomPanel.add(viewLogsButton);
        bottomPanel.add(supportButton);
        bottomPanel.add(fleetButton);

        add(bottomPanel, BorderLayout.SOUTH);

        getContentPane().setBackground(new Color(245, 245, 255));
    }

    private void refreshCarList() {
        carListModel.clear();
        for (Car car : cars) {
            carListModel.addElement(car.toString());
        }
    }

    private void updateCarImage() {
        int selectedIndex = carList.getSelectedIndex();
        if (selectedIndex == -1) {
            carImageLabel.setIcon(null);
            carImageLabel.setText("No Car Selected");
            return;
        }

        Car selectedCar = cars.get(selectedIndex);
        String imagePath = "images/" + selectedCar.id + ".jpg";

        File imgFile = new File(imagePath);
        if (imgFile.exists()) {
            ImageIcon icon = new ImageIcon(imagePath);
            Image scaledImage = icon.getImage().getScaledInstance(
                    carImageLabel.getWidth(),
                    carImageLabel.getHeight(),
                    Image.SCALE_SMOOTH
            );
            carImageLabel.setIcon(new ImageIcon(scaledImage));
            carImageLabel.setText(""); // Clear text
        } else {
            carImageLabel.setIcon(null);
            carImageLabel.setText("Image not found: " + imagePath);
        }
    }

    private void rentSelectedCar() {
        int selectedIndex = carList.getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(this, "Please select a car to rent.");
            return;
        }

        Car selectedCar = cars.get(selectedIndex);
        if (!selectedCar.isAvailable) {
            JOptionPane.showMessageDialog(this, "This car is already rented.");
            return;
        }

        if (selectedCar.plateNumber.toUpperCase().startsWith("DL")) {
            JOptionPane.showMessageDialog(this, "This car is restricted from renting due to pollution control.");
            return;
        }

        String name = JOptionPane.showInputDialog(this, "Enter your full name:");
        if (name == null || name.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name is required.");
            return;
        }

        String dobStr = JOptionPane.showInputDialog(this, "Enter your Date of Birth (YYYY-MM-DD):");
        if (dobStr == null || dobStr.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Date of Birth is required.");
            return;
        }

        LocalDate dob;
        try {
            dob = LocalDate.parse(dobStr);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Invalid Date format. Use YYYY-MM-DD.");
            return;
        }

        int age = Period.between(dob, LocalDate.now()).getYears();
        if (age < 18) {
            JOptionPane.showMessageDialog(this, "You must be at least 18 years old to rent a car.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "You entered DOB: " + dob + "\nAre you sure this is correct?",
                "Confirm DOB", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        // Aadhaar Number
        String aadhaar = JOptionPane.showInputDialog(this, "Enter your 12-digit Aadhaar Number:");
        if (aadhaar == null || aadhaar.trim().isEmpty() || !aadhaar.matches("\\d{12}")) {
            JOptionPane.showMessageDialog(this, "A valid 12-digit Aadhaar number is required.");
            return;
        }

        // Driving License Number
        String license = JOptionPane.showInputDialog(this, "Enter your Driving License Number:");
        if (license == null || license.trim().isEmpty() || !license.matches("^[A-Z]{2}[0-9]{2}\\d{11}$")) {
            JOptionPane.showMessageDialog(this, "Invalid Driving License format.");
            return;
        }

        String daysStr = JOptionPane.showInputDialog(this, "Enter number of days to rent:");
        int days;
        try {
            days = Integer.parseInt(daysStr);
            if (days < 0) throw new NumberFormatException();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid number of days.");
            return;
        }

        String hoursStr = JOptionPane.showInputDialog(this, "Enter number of hours to rent (0-23):");
        int hours;
        try {
            hours = Integer.parseInt(hoursStr);
            if (hours < 0 || hours > 23 || (days == 0 && hours == 0)) throw new NumberFormatException();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid number of hours.");
            return;
        }

        String selectedCity = (String) JOptionPane.showInputDialog(this,
                "Select your city:", "Choose City",
                JOptionPane.PLAIN_MESSAGE, null, CITIES, CITIES[0]);

        if (selectedCity == null) {
            JOptionPane.showMessageDialog(this, "City is required.");
            return;
        }

        String[] paymentOptions = {"Cash", "Credit/Debit Card", "UPI"};
        String paymentMethod = (String) JOptionPane.showInputDialog(this,
                "Choose your payment method:", "Payment Options",
                JOptionPane.PLAIN_MESSAGE, null, paymentOptions, paymentOptions[0]);

        if (paymentMethod == null) {
            JOptionPane.showMessageDialog(this, "Payment method is required.");
            return;
        }

        double totalPrice = (days * DAILY_RATE) + (hours * HOURLY_RATE);

        int payConfirm = JOptionPane.showConfirmDialog(this,
                "Total price: Rs." + totalPrice + "\nProceed with payment?",
                "Confirm Payment", JOptionPane.YES_NO_OPTION);

        if (payConfirm != JOptionPane.YES_OPTION) return;

        // Log rental
        try (FileWriter fw = new FileWriter(LOG_FILE, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println("Rented: " + selectedCar.model + " | Plate: " + selectedCar.plateNumber +
                    " | Name: " + name + " | City: " + selectedCity +
                    " | Duration: " + days + " days " + hours + " hours" +
                    " | Payment: " + paymentMethod +
                    " | Date: " + LocalDate.now());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error writing to log file.");
        }

        selectedCar.isAvailable = false;
        refreshCarList();
        JOptionPane.showMessageDialog(this, "Car rented successfully!");
    }

    private void returnSelectedCar() {
        int selectedIndex = carList.getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(this, "Please select a car to return.");
            return;
        }

        Car selectedCar = cars.get(selectedIndex);
        if (selectedCar.isAvailable) {
            JOptionPane.showMessageDialog(this, "This car is not currently rented.");
            return;
        }

        selectedCar.isAvailable = true;
        refreshCarList();
        JOptionPane.showMessageDialog(this, "Car returned successfully!");
    }

    private void viewRentalLogs() {
        try (BufferedReader br = new BufferedReader(new FileReader(LOG_FILE))) {
            StringBuilder logs = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                logs.append(line).append("\n");
            }

            if (logs.length() == 0) logs.append("No rental logs found.");

            JTextArea textArea = new JTextArea(logs.toString());
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(600, 400));
            JOptionPane.showMessageDialog(this, scrollPane, "Rental Logs", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "No rental logs found.");
        }
    }

    private void showCustomerSupport() {
        JOptionPane.showMessageDialog(this,
                "Customer Support:\n" +
                        "Email: support@rentgo.com\n" +
                        "Phone: +91-9876543210\n" +
                        "Working Hours: 9 AM - 6 PM",
                "Customer Support", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showFleetManagement() {
        JOptionPane.showMessageDialog(this,
                "Fleet Management is currently under development.",
                "Fleet Management", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(null, "🚗 Welcome to RentGO!", "Welcome", JOptionPane.INFORMATION_MESSAGE);
            new RG().setVisible(true);
        });
    }
}
