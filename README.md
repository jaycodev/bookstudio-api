<h1 align="center">BookStudio - Library Management System</h1>

<p align="center">
  <img src="https://github.com/jason-vila/bookstudio/blob/main/src/main/webapp/images/bookstudio-logo.png" width="250" alt="BookStudio Logo" />
</p>

## Screenshots

### 📊 Dashboard Interface
<p align="center">
  <img src="https://github.com/jason-vila/bookstudio/blob/main/src/main/webapp/images/dashboard-view.png" width="800" alt="Dashboard Preview" />
</p>

### 📚 Editing a Book
<p align="center">
  <img src="https://github.com/jason-vila/bookstudio/blob/main/src/main/webapp/images/book-view.png" width="800" alt="Book Editing Preview" />
</p>

### ⚖️ Loans Interface
<p align="center">
  <img src="https://github.com/jason-vila/bookstudio/blob/main/src/main/webapp/images/loans-view.png" width="800" alt="Loans Preview" />
</p>

### 🔑 Login Interface
<p align="center">
  <img src="https://github.com/jason-vila/bookstudio/blob/main/src/main/webapp/images/login-view.png" width="800" alt="Login Preview" />
</p>

## 📃 Description

BookStudio is a comprehensive web-based library management system designed for managing book loans at a fictional university library. The primary focus is on streamlining the lending process with clear status tracking (loaned/returned).

The system provides functionalities such as:

- **User Authentication**:  
  - Login  
  - Forgot Password  
  - Reset Password  

- **Dashboard**:  
  - Provides an overview of key data from the system's tables, including loan statistics and report charts.  

- **Management Views (CRUDs)** for:  
  - Loans  
  - Books  
  - Authors  
  - Publishers  
  - Courses  
  - Students  
  - Users  

- **Profile  Management**:  
  - Users can view and update their profile information.  

- **Report Generation**:  
  - For each view (data table), you can generate a report in PDF or Excel format, making data export and analysis straightforward.  

- **Loan Tickets**:  
  - Each time a new loan is registered, the system automatically generates a ticket or receipt confirming the transaction.  

The system includes **light and dark modes**.

There are two user roles:
- **Administrator**
- **Librarian**

## 🎯 Purpose

This project was developed for personal growth as I am starting in web development. It serves as a learning platform to apply various technologies and design patterns in a real-world scenario.

## 🏗️ Architecture

The **BookStudio** project follows the **Model-View-Controller (MVC)** architecture to maintain a clean separation of concerns. Here's the breakdown of the key components:

1. **Model:**
   - Contains the business logic and data models (e.g., Books, Loans, Authors, Students).
   - Interacts with the database through **DAO** classes.

2. **View:**
   - The user interface is built with **JSP** and located in `/src/main/webapp`.
   - Reusable components like `header`, `sidebar`, and `buttonTheme` are in the `includes` folder.
   - Page-specific scripts are located in `/webapp/js`.

3. **Controller:**
   - **Servlets** handle HTTP requests, interact with the **services** layer, and forward results to the appropriate JSP for rendering.

4. **Database:**
   - The MySQL database is accessed using the DAO pattern.
   - Database connections are managed by the `DbConnection` class.

5. **Services:**
   - Contains business logic and interacts with the DAO layer to process data.

6. **Utils (Backend):**
   - Contains utility classes like **DbConnection** and **SessionFilter** for managing database connections and user sessions.

7. **Utils (Frontend):**
   - JavaScript helper functions for frontend utilities, such as **color-modes.js** (for light/dark mode), **datatables-setup.js** (for DataTables), and **toast.js** (for alerts).

8. **AJAX Requests:**
   - Asynchronous requests are handled between the client and server to improve user experience.
   - JavaScript files in `/webapp/js` manage these requests.

## 📂 Folder Structure

The folder structure is organized as follows:

```
/src/main/java
  /com
    /bookstudio
      /dao                  --> Data Access Objects (DB interaction)
        /impl               --> Implementations of DAO interfaces
      /models               --> Data models representing the entities
      /services             --> Business logic handling
      /servlets             --> HTTP request handling
      /utils                --> Utility classes (e.g., DbConnection, SessionFilter)

/src/main/webapp
  /images                --> Static images (logo, screenshots)
  /css                   --> Stylesheets (CSS files)
  /js                    --> JavaScript files for page-specific logic, including AJAX requests
  /utils                 --> Utility JS files (e.g., color-modes.js, datatables-setup.js)
  /WEB-INF
    /includes            --> Reusable JSP components (header, sidebar, buttonTheme, etc.)
  .jsp                   --> Main JSP pages
```

- **`WEB-INF/includes`**: This folder contains reusable JSP components (e.g., `header`, `sidebar`, `buttonTheme`). These are included in the main JSPs to maintain a consistent structure across the application without code duplication.

## ⚙️ Installation and Setup

1. **Clone the Repository:**
   ```bash
   git clone https://github.com/jason-vila/bookstudio.git
   ```

2. **Import the Project in Eclipse EE:**
   - Open Eclipse EE.
   - Navigate to `File > Import > Existing Maven Projects`.
   - Browse to the cloned repository folder and import the project.

3. **Install and Configure Tomcat 8.5 in Eclipse EE:**
   - In Eclipse EE, navigate to `Window > Preferences > Server > Runtime Environments`.
   - Click on `Add Server` and select **Apache Tomcat v8.5**.
   - Click on **Download and Install**.
   - Accept the terms and choose the installation directory for Tomcat.
   - Click `Finish`.
   - Once Tomcat is added, right-click on the project in the Project Explorer, select `Run As > Run on Server` to deploy the project to Tomcat.

4. **Configure the Database:**  
   - Ensure MySQL is installed and running.  
   - **Run the SQL script** found at:  
     ```
     /database/bookstudio_db.sql
     ```
     This script will create the schema `bookstudio_db` and populate it with initial data, including **administrator** and **librarian** users.  
   - Update the connection parameters in `DbConnection.java` with your MySQL credentials (username, password, etc.).  
   - **Default Credentials** (once you run the script):  
     - **Administrator:**  
       - **Username:** `Admin`  
       - **Password:** `Admin123@`  
     - **Librarian:**  
       - **Username:** `Biblio`  
       - **Password:** `Biblio123@`  

5. **Run the Application:**
   - Start Tomcat 8.5 from Eclipse EE or deploy the project to your Tomcat server.
   - Access the application in your browser at `http://localhost:8080/bookstudio` (or your configured context path).

## 🔌 Database Connection Configuration

The MySQL connection is configured in:
```
/src/main/java/com/bookstudio/utils/DbConnection.java
```

Example of an optimized connection configuration:

```java
package com.bookstudio.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost/bookstudio_db?useSSL=false&useTimezone=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "your_password";
    
    public static Connection getConexion() {
        Connection con = null;
        try {
            Class.forName(DRIVER);
            
            con = DriverManager.getConnection(URL, USER, PASSWORD);
            
        } catch (ClassNotFoundException e) {
            System.out.println("Error: Driver not installed! " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Error: Database connection failed! " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: General exception: " + e.getMessage());
        }

        return con;
    }
}
```
## 🚀 Usage

- **Login:** Use the login page to authenticate.
- **Dashboard:** Once logged in, access the dashboard to manage loans, books, authors, courses, publishers, students, users, and your profile.
- **User Roles:** The system supports two roles (Administrator and Librarian) with appropriate permissions for each role.

## 🛠️ Technologies and Dependencies

### Web Libraries (Loaded via CDN)
- **jQuery**: v3.7.1  
- **Moment.js**: v2.29.1  
- **Bootstrap Bundle**: v5.3.0 (includes Popper.js functionality)  
- **DataTables**:  
  - Core: v2.1.8  
  - Bootstrap 5 integration: v2.1.8  
  - Responsive extension: v3.0.3  
  - Responsive Bootstrap 5 integration: v3.0.3  
- **Popper.js**: v2.11.6 (if not using Bootstrap Bundle)  
- **Bootstrap-select**: v1.14.0-beta3 (with Spanish localization via defaults-es_ES)  
- **Cropper.js**: v1.5.12  
- **jsPDF**: v2.5.1  
- **ExcelJS**: v4.3.0  

### Maven Dependencies
- **Gson** (for JSON processing): v2.8.9  
- **Standard Taglibs**: v1.1.2  
- **MySQL Connector/J**: v8.0.33  
- **Protobuf Java**: v3.25.1  
- **JavaMail (javax.mail)**: v1.6.2  

### Additional Technologies
- **Programming Language**: Java (code is in English; content is in Spanish)
- **Web Components**: Servlets, DAO, Ajax, and jQuery
- **Build Tool**: Maven
- **Application Server**: Tomcat 8.5
- **Database**: MySQL (access via JDBC)

## 🤝 Contributing

Contributions are welcome! For detailed instructions on how to contribute to this project, please read our [CONTRIBUTING.md](CONTRIBUTING.md) guide.

## 📜 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
