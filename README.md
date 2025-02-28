# Cathago-Project-Submission

This project repository contains a web-based document scanning and matching application.

# Navigation
* [Technologies Used](#technologies-used)
* [Application Overview](#application-overview)
* [User Panel](#user-panel)
* [Admin Panel](#admin-panel)
* [Security and Access Control](#security-and-access-control)
* [Setup Instructions](#setup-instructions)


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
