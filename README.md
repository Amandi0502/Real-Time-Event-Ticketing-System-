
# Overview.
The University Multiple Real Time Event Management System is a web application designed to streamline the management of various events such as workshops, seminars, cultural programs, and academic sessions.The application allows users to create and manage events,register and partcipate in events and monitor event schedules.The objectives are 
Enhance event management efficiency,provide a user friendly platform for event discovery and participation and demonstarate the integration of modern technologies.



## Features.

- User Management
- Event Management
- Registration System


## Tech Stack.

**Frontend:** React, HTML5,CSS & Bootstrap

**Backend:** SpringBoot(Java) & RESTful APIs

**Database:** MySQL

**Tools and Libraries:** Node.js (Frontend),Maven(Backend) & Postman(API testing)

**Concurrency:** Java Multithreading

## Setup Instructions.
**Prerequisites:**

Ensure you have the following installed:

    Node.js (v16 or later)
    Java JDK (v11 or later)
    MySQL/PostgreSQL
**Clone the Repository:**
```bash
git clone https://github.com/your-username/Real-Time-Event-Ticketing-System.git  
cd Real-Time-Event-Ticketing-System  
```

**frontend:**
```bash
cd client 
npm install  
npm run dev
```

**Backend:**
Open the backend project in your Intellij Idea and 
Update the application.properties file with your database credentials:
```bash
spring.datasource.url=jdbc:mysql://localhost:3306/event_management  
spring.datasource.username=root  
spring.datasource.password=your_password  
```
Run the Spring Boot application.

**database Setup:**
Create a database named eventManagement.
Import the provided SQL script to create tables and insert initial data.
## Usage

Open the application in your browser at http://localhost:3000.
Log in or register as a new user.
Perform actions based on your role:
   
   **Admin:** Approve events, manage users, and oversee activities
   
   **Organizer:** Create and manage events.


**Participant:** Browse and register for events.



## Contributing

Contributions are always welcome!

A.M.A.L Alahakoon(Developer)

Poravi Guganathan(Project guide)


## License

[MIT](https://choosealicense.com/licenses/mit/)

