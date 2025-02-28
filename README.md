# Cathago-Project-Submission

This project repository contains a web-based document scanning and matching application.

# Navigation
* [Technologies Used](#technologies-used)
* [Application Overview](#application-overview)
* [User Panel](#user-panel)
* [Admin Panel](#admin-panel)
* [Security and Access Control](#security-and-access-control)
* [Setup Instructions](#setup-instructions)
* [Algorithms and Database Design](#Algorithms-and-Database-Design)
* [Code Explanation](#Code-Explanation)
* [API Endpoints and Testing](#API-Endpoints-and-Testing)


## Technologies Used:

* **Front-End:** HTML5, CSS3, VanillaJS (Fetch API)
* **Back-End:** Java Servlets, Classes, Interfaces
* **Database:** MySQL

## Application Overview:

This application provides a credit-based document scanning and matching system with separate panels for users and administrators.

## User Panel:

### Login and Registration:

* Users can register and log in using the `Get-started.html` page.
* After successful login, users are redirected to the `Home.html` page.

  User SignIn
![Results/User login or register.png](https://github.com/Venkatjakkampudi-7/Cathago-Project-Submission/blob/875c0d1c9e3ca522f508d466d893726df877f483/Results/User%20login%20or%20register.png)
   User Registration
![Registration](https://github.com/Venkatjakkampudi-7/Cathago-Project-Submission/blob/4424190fcf79e50d0b5906b2c3a96d7bb9d386f8/Results/Registration%20for%20user.png)

### Home Page Functionality:

* **Credit-Based Document Scanner and Matcher:**
    * Users can upload plain text files for scanning and matching.
    * Each scan or rematch deducts 1 credit.
    * Users receive 20 credits daily (starter pack).
    * Viewing past scan outcomes and document comparisons does not deduct credits.

    Home page
    ![Home page](https://github.com/Venkatjakkampudi-7/Cathago-Project-Submission/blob/36e692fde4e50e102481971387f280687a8c1ddd/Results/Home%20screen.png)

   Status bar
     ![status bar](https://github.com/Venkatjakkampudi-7/Cathago-Project-Submission/blob/36e692fde4e50e102481971387f280687a8c1ddd/Results/Top%20nav%20bar%20for%20credit%20count.png)

   Document scanner and matcher
  ![Document scanner](https://github.com/Venkatjakkampudi-7/Cathago-Project-Submission/blob/36e692fde4e50e102481971387f280687a8c1ddd/Results/the%20scan%20and%20match%20form.png)
   
   Document scanner and matcher
  ![document scanner and matcher](https://github.com/Venkatjakkampudi-7/Cathago-Project-Submission/blob/36e692fde4e50e102481971387f280687a8c1ddd/Results/Scaner%20and%20matching.png)

    Rematch option
    ![rematch option](https://github.com/Venkatjakkampudi-7/Cathago-Project-Submission/blob/36e692fde4e50e102481971387f280687a8c1ddd/Results/Option%20to%20rematch.png)

     Past scans
    ![past scans](https://github.com/Venkatjakkampudi-7/Cathago-Project-Submission/blob/36e692fde4e50e102481971387f280687a8c1ddd/Results/Past%20scans%20list.png)
    
* **Credit Management:**
    * If credits are exhausted, users can request additional credits (default 5 credits) from the admin.
    * Admins can grant or reject credit requests.
    * Users must wait for admin approval before scanning again.
    * If a request is rejected, users can request credits again.
 
    Credit request
    ![credit request](https://github.com/Venkatjakkampudi-7/Cathago-Project-Submission/blob/4424190fcf79e50d0b5906b2c3a96d7bb9d386f8/Results/Credit%20request%20button.png)

     Credit exhust and admin approval
     ![Credit exhust and admin approval](https://github.com/Venkatjakkampudi-7/Cathago-Project-Submission/blob/36e692fde4e50e102481971387f280687a8c1ddd/Results/Message%20from%20server%20for%20credit%20request.png)
    
* **Past Scans:**
    * Users can view their past scan history and results.
    * Users can download their scan history.
    * A button toggles the visibility of past scans.
      
   Past scans 
   ![pastscans](https://github.com/Venkatjakkampudi-7/Cathago-Project-Submission/blob/4424190fcf79e50d0b5906b2c3a96d7bb9d386f8/Results/Button%20to%20close%20past%20scans.png)

   Downloading scan history
   ![Scan history download](https://github.com/Venkatjakkampudi-7/Cathago-Project-Submission/blob/36e692fde4e50e102481971387f280687a8c1ddd/Results/User%20downloading%20their%20scan%20history.png)

   Dowloaded form of the scan history
   ![Scanhistory-1](https://github.com/Venkatjakkampudi-7/Cathago-Project-Submission/blob/36e692fde4e50e102481971387f280687a8c1ddd/Results/Scan%20history%20outcome.png)
   ![scanhistory-2](https://github.com/Venkatjakkampudi-7/Cathago-Project-Submission/blob/36e692fde4e50e102481971387f280687a8c1ddd/Results/Scan%20history%20outcome-2.png)
    
* **Document Comparison:**
    * Users can compare the uploaded document with the matched document side-by-side.
    * Separate scrollbars allow for individual document viewing.

    Outcomes of document matching
    ![outcomes](https://github.com/Venkatjakkampudi-7/Cathago-Project-Submission/blob/36e692fde4e50e102481971387f280687a8c1ddd/Results/Matching%20documents.png)

   Seperate scroll on both documents
  ![seperate scroll](https://github.com/Venkatjakkampudi-7/Cathago-Project-Submission/blob/36e692fde4e50e102481971387f280687a8c1ddd/Results/Seperate%20scroll%20for%20each%20document.png)

    Document comparision
     ![document comparision](https://github.com/Venkatjakkampudi-7/Cathago-Project-Submission/blob/4424190fcf79e50d0b5906b2c3a96d7bb9d386f8/Results/Comparing%20long%20documents.png)
     ![Document compaeision](https://github.com/Venkatjakkampudi-7/Cathago-Project-Submission/blob/4424190fcf79e50d0b5906b2c3a96d7bb9d386f8/Results/Document%20comparision-2.png)
    
* **Profile and Sign Out:**
    * Users can view their profile.
    * A sign-out button ends the session and redirects to the login page.

    Profile btn
    ![profile btn](https://github.com/Venkatjakkampudi-7/Cathago-Project-Submission/blob/36e692fde4e50e102481971387f280687a8c1ddd/Results/Profile%20Drop%20down%20button.png)

    Profile
    ![user profile](https://github.com/Venkatjakkampudi-7/Cathago-Project-Submission/blob/36e692fde4e50e102481971387f280687a8c1ddd/Results/Profile%20side%20bar.png)
     
* **Rematch:**
    * Users have the option to rematch a past scan if initial results were not found.

## Admin Panel:

### Login:

* Admins have a separate login portal.

 Admin Sign In
 ![Admin login](https://github.com/Venkatjakkampudi-7/Cathago-Project-Submission/blob/4424190fcf79e50d0b5906b2c3a96d7bb9d386f8/Results/Admin%20login.png)

Admin Panel
![Admin panel](https://github.com/Venkatjakkampudi-7/Cathago-Project-Submission/blob/4424190fcf79e50d0b5906b2c3a96d7bb9d386f8/Results/Admin%20dashboard%20nav%20bar.png)


### Dashboard Page:

* **Top Users:** Displays users with the highest credit usage.
* **Top Searches:** Displays the most frequently searched topics and keywords.
* **Scan History:** Displays the overall scan history.

  Top users
  ![top users](https://github.com/Venkatjakkampudi-7/Cathago-Project-Submission/blob/36e692fde4e50e102481971387f280687a8c1ddd/Results/Top%20users%20by%20usage.png)

  Top searches
  ![top searches](https://github.com/Venkatjakkampudi-7/Cathago-Project-Submission/blob/36e692fde4e50e102481971387f280687a8c1ddd/Results/Most%20searched%20words%20and%20topics.png)

### User Management Page:

* Admins can:
    * Manually adjust user credits.
    * Change user subscriptions.
    * Activate or block user accounts.
    * View user profiles, scan history, and credit request history.
    * Search for users by first or last name.

  User Panel
  ![user panel](https://github.com/Venkatjakkampudi-7/Cathago-Project-Submission/blob/36e692fde4e50e102481971387f280687a8c1ddd/Results/User%20management.png)

  User profile
  ![user profile for admin](https://github.com/Venkatjakkampudi-7/Cathago-Project-Submission/blob/36e692fde4e50e102481971387f280687a8c1ddd/Results/User%20profile%20view%20for%20admin.png)

  User scan history
  ![schistory](https://github.com/Venkatjakkampudi-7/Cathago-Project-Submission/blob/36e692fde4e50e102481971387f280687a8c1ddd/Results/Scan%20history.png)

  User past credit requests
  ![request list](https://github.com/Venkatjakkampudi-7/Cathago-Project-Submission/blob/4424190fcf79e50d0b5906b2c3a96d7bb9d386f8/Results/Credit%20request%20history%20of%20user.png)

  User search
  ![user search](https://github.com/Venkatjakkampudi-7/Cathago-Project-Submission/blob/4424190fcf79e50d0b5906b2c3a96d7bb9d386f8/Results/Fetching%20users.png)

  Mannual credit adjustment by admin
  ![credit adjustment](https://github.com/Venkatjakkampudi-7/Cathago-Project-Submission/blob/36e692fde4e50e102481971387f280687a8c1ddd/Results/Mannual%20credit%20adjusment%20by%20admin.png)
  
  Admin managing the user subscription
  ![subsription management](https://github.com/Venkatjakkampudi-7/Cathago-Project-Submission/blob/36e692fde4e50e102481971387f280687a8c1ddd/Results/User%20subscription%20management.png)

  Account status
  ![account status](https://github.com/Venkatjakkampudi-7/Cathago-Project-Submission/blob/36e692fde4e50e102481971387f280687a8c1ddd/Results/User%20account%20status%20management.png)

  User search
  ![User search](https://github.com/Venkatjakkampudi-7/Cathago-Project-Submission/blob/36e692fde4e50e102481971387f280687a8c1ddd/Results/Search%20bar%20in%20admin.png)

### Credit Management Page:

* **Credit Requests:**
    * Displays credit requests from users.
    * Admins can grant or reject requests (default 5 credits, adjustable by admin).
    * Admins can search for credit requests.
 
  Credit requests
  ![Credit requests](https://github.com/Venkatjakkampudi-7/Cathago-Project-Submission/blob/4424190fcf79e50d0b5906b2c3a96d7bb9d386f8/Results/Credit%20request%20at%20admin.png)

  Credit requests
  ![credit request none](https://github.com/Venkatjakkampudi-7/Cathago-Project-Submission/blob/36e692fde4e50e102481971387f280687a8c1ddd/Results/credit%20Request%20panel%20admin.png)

  Credit update
  ![Credit update](https://github.com/Venkatjakkampudi-7/Cathago-Project-Submission/blob/4424190fcf79e50d0b5906b2c3a96d7bb9d386f8/Results/Credit%20request%20update%20by%20admin.png)
    
* **Packs (Subscriptions):**
    * Admins can manage subscription packs (currently only the starter pack with 20 daily credits).
    * Admins can activate/deactivate packs and AI power for each pack.
    * Admins can create new packs.
    * Admins can search for packs.
 
  Pack panel
  ![packs](https://github.com/Venkatjakkampudi-7/Cathago-Project-Submission/blob/36e692fde4e50e102481971387f280687a8c1ddd/Results/Subscription%20handling%20for%20admin.png)

  Creating new pack
  ![new pack](https://github.com/Venkatjakkampudi-7/Cathago-Project-Submission/blob/36e692fde4e50e102481971387f280687a8c1ddd/Results/creating%20new%20subscription%20by%20admin.png)

## Security and Access Control:

* Users cannot access the admin panel, and admins cannot access the user panel.
* Attempts to access unauthorized panels result in redirection to the appropriate panel.
* Admins logging in through the user login are redirected to the admin dashboard.
* Users logging in through the admin login are redirected to the user home screen.

## Setup Instructions

To set up and run the Cathago Project Submission application, please follow these steps:

**1. Prerequisites:**

* **Apache Tomcat Server:** You will need a running instance of Apache Tomcat Server. You can use XAMPP or any other Tomcat manager.
* **MySQL 8:** Ensure you have MySQL 8 installed and running.
* **Java 8+:** You must have Java Development Kit (JDK) version 8 or later installed.
* **Modern Web Browser:** A modern web browser (Chrome, Firefox, Edge, Safari) is required.
* **NetBeans or Microsoft Visual Studio Code:** (Optional) If you intend to modify the source code.

**2. Database Setup:**

* **Create Database:**
    * Log in to your MySQL server using MySQL Workbench, the terminal, or bash.
    * Create a database named `textanalyser`:
        ```sql
        CREATE DATABASE textanalyser;
        USE textanalyser;
        ```
* **Import Database Backup:**
    * Import the provided database backup file (`database/cathago_database.sql`) into the `textanalyser` database.
    * Example using the command line:
        ```bash
        mysql -u <username> -p textanalyser < database/cathago_database.sql
        ```
        * Replace `<username>` with your MySQL username.
* **ER Diagram:**
    * An Entity-Relationship (ER) diagram (`database/cathago_er_diagram.png`) is included to provide a visual representation of the database schema.
* **Configure Database Connection:**
    * Modify the database connection details in the `backend_processes/DBConnection.java` file within your project's backend. Update the following parameters:
        * `DB_URL`: The URL of your MySQL server (e.g., `jdbc:mysql://localhost:3306/textanalyser`).
        * `DB_USER`: Your MySQL username.
        * `DB_PASSWORD`: Your MySQL password.
    * **Important:** If your MySQL server uses different credentials than those in the provided `DBConnection.java`, you must update the username and password in this file.

**3. Application Deployment:**

* **Deploy the WAR File:**
    * A pre-built WAR file (`cathago.war`) is provided. You can directly deploy it to your Tomcat server.
    * Copy the `cathago.war` file to the `webapps` directory of your Tomcat installation.
    * Start or restart your Tomcat server.
    * Tomcat will automatically deploy the application.

**4. Configuration:**

* **Upload Directory:**
    * The application uses the following default paths:
        * Documents: `D:\Cathago Test\App-data\Documents`
        * Bert Model: `D:\Cathago Test\NLP Model\bert.onnx`
    * If you need to change these paths, modify the following Java files:
        * Document path: `backend_processes/Preprocessor.java`, `backend_processes/Documents.java`
        * Bert model path: `backend_processes/semantic_processor.java`
    * Make sure the Tomcat user has read and write permissions to the document upload directory.

**5. Bert Model Setup:**

* **Download the Model:**
    * Download the `bert.onnx` model from the following link:
        * `https://huggingface.co/google-bert/bert-base-uncased/blob/main/model.onnx`
    * Alternatively, you can use Python to download and save the model with any necessary transformations.
* **Place the Model:**
    * Place the downloaded `bert.onnx` file in the `NLP Model` folder within your project directory.
    * The final path should be `D:\Cathago Test\NLP Model\bert.onnx` (or your chosen path).
    * Refer to the file `how to setup bert model` for further instructions.

**6. Access the Application:**

* **User Panel:**
    * Open your web browser and navigate to `http://localhost:8080/Text_Analyser/The-App/Get-started.html`.
* **Admin Panel:**
    * Navigate to `http://localhost:8080/Text_Analyser/Admin-App/Get-started.html`.
* **Default Index Page:**
    * By default, accessing `http://localhost:8080/Text_Analyser/` will redirect you to the user panel login/registration page: `http://localhost:8080/Text_Analyser/The-App/Get-started.html`.

**7. First Time Use:**

* Register a user account.
* Log in as the registered user to use the user panel.
* Log in as the admin to use the admin panel.
* The admin credentials will be in the database. If you need to create a new admin, insert a new record into the admin table.

**Troubleshooting:**

* **Database Connection Issues:** Check your database connection details in `backend_processes/DBConnection.java`.
* **Deployment Errors:** Check the Tomcat logs for any errors.
* **File Upload Issues:** Verify the upload directory permissions and configuration.
* **Bert Model Issues:** Ensure the `bert.onnx` file is correctly placed and accessible.
* **Path issues:** If you change the default paths, ensure they are correct in the java files.

**Note:** Replace placeholders like `<username>` and paths with your actual values.

## Code Explanation

### Front-End (User Panel)

* **`assets/js/profile.js`**:
    * Handles user profile-related functionality. This includes displaying user profile information and enabling users to update their profile details.
* **`assets/js/Authentication.js`**:
    * Manages user authentication processes. This includes user login and registration, ensuring secure access to the application.
* **`assets/js/Documentscanner.js`**:
    * Implements the core document scanning and matching functionality. This script allows users to upload documents, initiate the scanning process, and view matching results.
* **`assets/js/Get-started.js`**:
    * Provides JavaScript functionality for the `Get-started.html` page, which serves as the sign-in and registration portal for users.
* **`assets/js/Report_dowload.js`**:
    * Handles the download of reports, allowing users to save scan and match data.
* **`assets/js/style.js`**:
    * Contains additional JavaScript code for styling and enhancing user interface interactions.

### Front-End (Admin Panel)

* **`assets/js/Authentication.js`**:
    * Manages admin authentication processes, ensuring secure access to the admin panel.
* **`assets/js/Dashboard.js`**:
    * Handles the functionality of the admin dashboard, including displaying analytics such as top users, top searches, and scan history.
* **`assets/js/credit-manager.js`**:
    * Manages credit requests from users and the administration of subscription packs.
* **`assets/js/user-manager.js`**:
    * Manages user accounts, including adjusting credits, changing subscriptions, and viewing user histories.

### Back-End (Java)

* **`Admin_Backend` Package**:
    * Contains classes that manage admin-specific functionalities, including analytics, credit management, pack management, and user management.
* **`Backend.Auth` Package**:
    * Handles authentication, session management, and data retrieval for both users and admins. This includes classes for login, registration, profile management, and data processing.
* **`Document_Processor` Package**:
    * Implements the document processing logic, including the integration of the NLP model. This package contains classes for pre-processing documents, integrating the `bert.onnx` model (`Semantic_Processor.java`), and processing similarity results.
* **`backend_processes` Package**:
    * Contains core classes for database interaction (`DBConnection.java`), security, and data management. This package includes classes for managing credits, users, and packs.

### Key Back-End Classes

* **`DBConnection.java`**:
    * Handles the establishment and management of database connections to the MySQL database.
* **`Login.java`, `Register.java`, `Signout.java`**:
    * Manage user and admin authentication, including login, registration, and session termination.
* **`ScanUpload.java`**:
    * Handles the uploading of documents to the server.
* **`Semantic_Processor.java`**:
    * Integrates the `bert.onnx` model for semantic document matching.
* **`Credits.java`**:
    * Manages user credit balances and transactions.
* **`Users.java`**:
    * Manages user data and account information.
* **`Pack.java`**:
    * Manages subscription packs and their configurations.
* **`Analytics.java`**:
    * Retrieves and processes analytics data for the admin dashboard.
* **`Credit_Manager.java`**:
    * Manages credit requests from users and processes them.
* **`User_Manager.java`**:
    * Manages user accounts, including modifications and data retrieval.
* **`Pre_Processor.java`**:
    * Handles the pre processing of the uploaded documents, cleaning and preparing them for the NLP model.

### Communication

* **Fetch API**:
    * The front-end utilizes the Fetch API to communicate with the back-end Java servlets. This allows for asynchronous data retrieval and updates, enhancing the user experience.

### Testing

* **Test Files**:
    * The project includes test files (`Preprocessor_test.java`, `mainprocessor_test.java`, etc.) to ensure the reliability and functionality of critical back-end components.

## Algorithms and Database Design

### Algorithms

* **Cosine Similarity:**
    * The application utilizes the cosine similarity algorithm to determine the similarity between documents.
    * The `Pre_Processor.java` file plays a crucial role in this process. It breaks down documents into keywords, calculates their frequencies, and stores this data in the database.
    * Initially, the top 100 documents are selected based on the frequency data stored in the database.
    * Then, cosine similarity is applied again to these 100 documents, and the top 20 documents are chosen.
    * Finally, only documents with a similarity score of 50% or higher are presented to the user.
* **BERT Model Integration:**
    * If the BERT model is enabled for the subscription pack, the `Semantic_Processor.java` file is used.
    * This file tokenizes the documents and calculates similarity based on semantic understanding, enhancing the accuracy of document matching.
* **Regular Similarity Processing:**
    * The `Similarity_Processor.java` file handles the standard cosine similarity calculations.

### Database Design

* **Significance:**
    * The project's database design is implemented without the use of cron jobs or database triggers.
    * All credit management and outcome processing are handled within the application's Java code, ensuring control and maintainability.
* **Database Tables:**
    * `users`: Stores user account information.
    * `subscription_slab`: Manages subscription packs and their details.
    * `log`: Logs application activities and events.
    * `credit_request`: Stores user credit requests.
    * `scanlist`: Stores information about user scans.
    * `doc_data`: Stores document keyword frequencies.
    * `semantic_score`: Stores semantic similarity scores from the BERT model.
    * `similarity_score`: Stores cosine similarity scores.
    * `stopwords`: Stores a list of stopwords used in document processing.
    * `rescanlist`: Stores information about user rescans.

  Database ER Diagram
  ![Er diagram](https://github.com/Venkatjakkampudi-7/Cathago-Project-Submission/blob/973110f47cd0cb5291b026a3641bb1bff765e2cc/Database%20Desgin%20and%20Er%20diagrams/cathago%20database%20design.png)

### Data Flow

1.  **Document Upload:**
    * Users upload documents through the front-end.
2.  **Preprocessing:**
    * The `Pre_Processor.java` file processes the uploaded document, extracts keywords, and calculates frequencies.
    * The keyword frequencies are stored in the `doc_data` table.
3.  **Similarity Calculation:**
    * The `Similarity_Processor.java` file calculates cosine similarity based on the stored keyword frequencies.
    * if the subscription pack has the bert model enabled, then the `Semantic_Processor.java` file is used.
    * Similarity scores are stored in the `similarity_score` or `semantic_score` tables.
4.  **Result Display:**
    * The application retrieves the top matching documents from the database and displays them to the user.
5.  **Credit Management:**
    * Credit transactions are handled within the Java servlets, updating the `users` and `log` tables as needed.
    * Credit requests are stored and managed within the `credit_request` table.

## API Endpoints and Testing

This application's back-end provides a comprehensive set of API endpoints for both user and admin functionalities. These endpoints have been thoroughly tested using Postman to ensure their reliability and performance.

### API Endpoint List

* **User Authentication:**
    * `http://localhost:8080/Text_Analyser/Login` (POST): Returns the user's login status, privilege level, and account status.
* **Admin Analytics:**
    * `http://localhost:8080/Text_Analyser/Analytics?req=1` (GET): Returns a list of top users based on credit usage.
    * `http://localhost:8080/Text_Analyser/Analytics?req=2` (GET): Returns a list of top searched topics and keywords.
    * `http://localhost:8080/Text_Analyser/Analytics?req=3` (GET): Returns the scan history.
* **Pack Management:**
    * `http://localhost:8080/Text_Analyser/Pack_Manager?req=1` (GET): Returns details of all subscription packs.
    * `http://localhost:8080/Text_Analyser/Pack_Manager?req=2&skey='+skey` (GET): Returns details of a specific searched subscription pack.
    * `http://localhost:8080/Text_Analyser/Pack_Manager` (POST): Creates a new subscription pack.
    * `http://localhost:8080/Text_Analyser/Pack_Manager` (PATCH): Updates an existing subscription pack.
    * `http://localhost:8080/Text_Analyser/Pack_Manager?req=1` (GET): Returns data used to match user packs.
* **Credit Management:**
    * `http://localhost:8080/Text_Analyser/Credit_Manager?req=1` (GET): Returns a list of credit requests.
    * `http://localhost:8080/Text_Analyser/Credit_Manager?req=2&skey='+skey` (GET): Returns a specific searched credit request.
    * `http://localhost:8080/Text_Analyser/Credit_Manager` (PATCH): Processes and updates a credit request.
* **User Management:**
    * `http://localhost:8080/Text_Analyser/User_Manager?req=3&userid='+uid` (GET): Returns the profile of a specific user.
    * `http://localhost:8080/Text_Analyser/User_Manager?req=4&userid='+uid` (GET): Returns the scan list of a specific user.
    * `http://localhost:8080/Text_Analyser/User_Manager?req=5&userid='+uid` (GET): Returns the credit request list of a specific user.
    * `http://localhost:8080/Text_Analyser/User_Manager?req=1` (GET): Returns a list of all users.
    * `http://localhost:8080/Text_Analyser/User_Manager?req=2&skey='+skey` (GET): Returns a list of searched users.
    * `http://localhost:8080/Text_Analyser/User_Manager` (PATCH): Processes and updates a user's profile.
* **Document Scanning and Matching:**
    * `http://localhost:8080/Text_Analyser/ScanUpload` (POST): Initiates a document scan and returns matching documents.
    * `http://localhost:8080/Text_Analyser/ScanUpload` (PATCH): Initiates a document rematch and returns matching documents.
    * `http://localhost:8080/Text_Analyser/ScanUpload?scanid='+sid` (GET): Returns the outcomes of a specific document based on its previous results.
    * `http://localhost:8080/Text_Analyser/Match_Comparisions?doc1=' + encodeURIComponent(doc1) + '&doc2=' + encodeURIComponent(doc2)` (GET): Returns data for comparing two documents.
* **User Profile and Credits:**
    * `http://localhost:8080/Text_Analyser/Profile?req=3` (GET): Returns the number of credits remaining for the logged-in user.
    * `http://localhost:8080/Text_Analyser/Credit_request` (POST): Submits a new credit request to the admin.
    * `http://localhost:8080/Text_Analyser/Profile?req=1` (GET): Returns the logged-in user's profile details.
    * `http://localhost:8080/Text_Analyser/Profile?req=2` (GET): Returns the previous scans of the logged-in user.
* **Session Management:**
    * `http://localhost:8080/Text_Analyser/Signout` (GET): Signs out the user and invalidates the session.

### Testing with Postman

Postman was used to test each of these endpoints, verifying:

* Correct request and response formats.
* Accurate data retrieval and manipulation.
* Proper handling of different request types (GET, POST, PATCH).
* Appropriate error handling and status codes.

### Database Connection Management and Security

To ensure data integrity and prevent potential issues such as data leaks or thread sleeping, the following measures were implemented:

* **MySQL Process Monitoring:**
    * The `mysql show processlist` command was used extensively during testing to monitor active database connections.
    * This ensured that all connections were properly closed after use and that no threads were left in a sleep state, which could lead to resource exhaustion or data leaks.
* **Java Resource Management:**
    * All Java code interacting with the database is carefully structured using `try-with-resources` blocks.
    * This guarantees that database connections and prepared statements are automatically closed, even in the event of exceptions.
    * Proper connection closing prevents open connections, which could lead to data corruption or security vulnerabilities.
    * This robust error handling and resource management ensures the application's stability and security.
