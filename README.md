# RentGO — Car Rental Management System

RentGO is a Java-based desktop application designed to streamline vehicle rentals and inventory tracking. Built with a clean Graphical User Interface (GUI), the system allows users to browse an active vehicle fleet, process real-time rentals or returns, and manage a secure transactional log history.

---

## 🛠️ Tech Stack & Tools

* **Language:** Java
* **GUI Framework:** Java Swing / AWT (Abstract Window Toolkit)
* **IDE:** IntelliJ IDEA

---

##  Key Features

* **Dynamic Fleet Inventory Panel:** Browse a live list of vehicles complete with model names, unique registration plate numbers, and real-time availability tags (`[Available]` / `[Rented]`).
* **Instant Visual Preview Canvas:** Selecting any car from the inventory list instantly renders a high-resolution image of that specific vehicle model on the main dashboard.
* **Streamlined User Onboarding:** Features interactive `JOptionPane` dialog prompts that guide users to securely enter registration details (Full Name, Age, City) when checking out a vehicle.
* **Comprehensive Rental Log Engine:** Automatically generates and displays structured, human-readable audit trails tracking customer demographics, chosen payment pathways (Cash or Credit/Debit), exact system timestamps, and calculated total rental costs.

---

##  System Walkthrough & User Interface

The application follows an end-to-end operational workflow:

### 1. Welcome & Initialization
Upon launching the application, users are greeted with a clear modal welcome dialog to initialize the platform session.

### 2. Vehicle Selection & Interactive Checkout
* Users can view the complete active fleet (e.g., Toyota Innova, Hyundai Creta, Mahindra Thar).
* Clicking a vehicle instantly updates the media canvas with the corresponding car image.
* Selecting **"Rent Selected Car"** triggers an explicit text-input prompt requiring user verification before updating the vehicle's availability state.

### 3. Transaction Logging & History
Clicking **"View Rental Logs"** opens a dedicated text-rendering terminal component within the UI. This dashboard outputs structured customer session summaries, tracking:
* **Customer Profile:** Name, Age, and Home City.
* **Financial Breakdowns:** Final booking costs calculated relative to rental duration.
* **Audit Trail:** Exact calendar dates, times, and vehicle metadata for seamless tracking.

---

##  How to Run the Project Locally

### Prerequisites
* Ensure you have the **Java Development Kit (JDK 8 or higher)** installed on your machine.
* An IDE such as **IntelliJ IDEA** is recommended.
