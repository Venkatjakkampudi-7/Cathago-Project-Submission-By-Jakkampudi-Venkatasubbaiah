# Cathago-Project-Submission

This project repository contains a web-based document scanning and matching application.

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
* [Results/User login or register.png](https://github.com/Venkatjakkampudi-7/Cathago-Project-Submission/blob/875c0d1c9e3ca522f508d466d893726df877f483/Results/User%20login%20or%20register.png)

### Home Page Functionality:

* **Credit-Based Document Scanner and Matcher:**
    * Users can upload plain text files for scanning and matching.
    * Each scan or rematch deducts 1 credit.
    * Users receive 20 credits daily (starter pack).
    * Viewing past scan outcomes and document comparisons does not deduct credits.
* **Credit Management:**
    * If credits are exhausted, users can request additional credits (default 5 credits) from the admin.
    * Admins can grant or reject credit requests.
    * Users must wait for admin approval before scanning again.
    * If a request is rejected, users can request credits again.
* **Past Scans:**
    * Users can view their past scan history and results.
    * A button toggles the visibility of past scans.
* **Document Comparison:**
    * Users can compare the uploaded document with the matched document side-by-side.
    * Separate scrollbars allow for individual document viewing.
* **Profile and Sign Out:**
    * Users can view their profile.
    * A sign-out button ends the session and redirects to the login page.
* **Rematch:**
    * Users have the option to rematch a past scan if initial results were not found.

## Admin Panel:

### Login:

* Admins have a separate login portal.

### Dashboard Page:

* **Top Users:** Displays users with the highest credit usage.
* **Top Searches:** Displays the most frequently searched topics and keywords.
* **Scan History:** Displays the overall scan history.

### User Management Page:

* Admins can:
    * Manually adjust user credits.
    * Change user subscriptions.
    * Activate or block user accounts.
    * View user profiles, scan history, and credit request history.
    * Search for users by first or last name.

### Credit Management Page:

* **Credit Requests:**
    * Displays credit requests from users.
    * Admins can grant or reject requests (default 5 credits, adjustable by admin).
    * Admins can search for credit requests.
* **Packs (Subscriptions):**
    * Admins can manage subscription packs (currently only the starter pack with 20 daily credits).
    * Admins can activate/deactivate packs and AI power for each pack.
    * Admins can create new packs.
    * Admins can search for packs.

## Security and Access Control:

* Users cannot access the admin panel, and admins cannot access the user panel.
* Attempts to access unauthorized panels result in redirection to the appropriate panel.
* Admins logging in through the user login are redirected to the admin dashboard.
* Users logging in through the admin login are redirected to the user home screen.
