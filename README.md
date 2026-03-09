<p align="center">
  <img src="https://storage.googleapis.com/bukas-website-v3-prd/website_v3/images/MMDC_Logo_-_VERTICAL_-_Colored.original.png" width="200">
</p>

# MotorPH Basic Payroll System

Repository: **MO-IT101-Group28**

This project is a **Java-based payroll processing system** developed for the course **MO-IT101 – Computer Programming 1** at **Mapúa Malayan Digital College (MMDC)**.

The system reads employee and attendance data from CSV files and computes payroll information including total hours worked, gross salary, government deductions, and net salary.

---

# Course Information

**Course:** MO-IT101 – Computer Programming 1  
**Section:** A1101  

**Instructor:**  
Aldrin John Tamayo  
ajtamayo@mmdc.mcl.edu.ph  

**Team Name:** Group 28

---

# Team Details

### John Wilberth Botin  
Email: lr.jwbotin@mmdc.mcl.edu.ph  

The main development of the payroll system was implemented in this project. This includes the login feature, employee menu, and payroll staff menu. The program reads employee and attendance data from CSV files and processes the records to compute payroll information. Payroll computations such as total hours worked, cutoff salary calculations, and government deductions including SSS, PhilHealth, Pag-IBIG, and withholding tax were also implemented. All parts of the system were integrated into a single Java file following the project requirements.

---

### Josh Mheir Jasareno  
Email: lr.jmjasareno@mmdc.mcl.edu.ph  

Assigned for preparing the documentation of the project milestone. This includes organizing the description of how the payroll system works and preparing the project report. Assisted in reviewing the system workflow and explaining the payroll staff menu logic and overall program structure in the documentation.

---

### Julie Ann Abella  
Email: lr.jaabella@mmdc.mcl.edu.ph  

Assisted in implementing the login portion of the system. This helped establish the process where the program asks for user credentials and redirects users to the correct menu depending on the login role.

---

### Jorge Castro  
Email: lr.jcastro@mmdc.mcl.edu.ph  

Assisted in preparing the employee menu that appears after logging in as an employee. This work helped structure the options that allow employees to view their employee number, employee name, and birthday.

---

### JT Englis  
Email: lr.jenglis@mmdc.mcl.edu.ph  

Participated as a team member but did not help.

---

# Program Details

The MotorPH Basic Payroll System is a console-based Java program that simulates a simplified payroll processing system for MotorPH employees. The program reads employee information and attendance records from CSV files and uses these records to compute payroll data automatically. Employee data such as employee number, employee name, birthday, and hourly rate are retrieved from the employee CSV file, while attendance logs are read from the attendance CSV file.

The system starts by asking the user to log in using either an employee account or a payroll staff account. If the login is successful, the program displays the appropriate menu based on the username entered. The employee side allows the user to enter an employee number and view the employee’s basic details such as employee number, employee name, and birthday. The payroll staff side allows the user to process payroll either for one employee or for all employees.

For attendance processing, the system only considers records from June to December. Attendance is grouped into two cutoff periods: the first cutoff from the 1st to the 15th of the month, and the second cutoff from the 16th to the end of the month. For attendance processing, the program calculates the total hours worked based on the employee's login and logout times recorded in the attendance file. The system applies several rules when computing working hours. Login times up to 8:10 AM are treated as 8:00 AM as part of the grace period policy. If an employee logs out after 5:00 PM, the time is capped at 5:00 PM since overtime hours are not included in the computation. After determining the working duration, the program subtracts the standard one-hour break to obtain the final total hours worked for the day.

For payroll computation, the system first calculates the gross salary for each cutoff by multiplying the total hours worked by the employee’s hourly rate. The gross salary from both cutoffs is then added to determine the monthly gross salary. Government deductions such as SSS, PhilHealth, Pag-IBIG, and withholding tax are computed based on this monthly gross salary. These deductions are applied only during the second cutoff payout. The system then displays the payroll summary for each employee, including cutoff dates, total hours worked, gross salary, deductions, total deductions, and net salary, without rounding off any values.


---

# File Structure

```
MO-IT101-GROUP28
│
├── resources
│   ├── Attendance Record.csv
│   └── MotorPH_Employee Data.csv
│
├── MotorPH_BasicPayrollSystem.java
└── README.md
```

---

# Project Plan Link

The project planning document can be accessed through the Google Sheets link below.

**Project Plan Spreadsheet**

https://docs.google.com/spreadsheets/d/1gID59rZNcey6Tk4yp7aozkZJkDPLcaOgTB6Y4tuO3nc/edit?usp=sharing

Note: The **Project Plan is located in the 4th sheet** of the spreadsheet.