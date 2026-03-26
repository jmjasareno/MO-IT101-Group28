import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

/**
* MotorPh Basic Payroll System
* This processes payroll data for employees based on attendancce records
* It supports user types: employee and payroll staff
*/
public class MotorPH_BasicPayrollSystem {

    // CSV file paths for data sources
    static final String EMPLOYEE_FILE = "resources/MotorPH_Employee Data.csv";
    static final String ATTENDANCE_FILE = "resources/Attendance Record.csv";

    // Valid Credentials for two user types
    static final String EMPLOYEE_USERNAME = "employee";
    static final String PAYROLL_USERNAME = "payroll_staff";
    static final String COMMON_PASSWORD = "12345";

    // Date and time formatters for CSV data
    static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("H:mm");

    /**
    *
    */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Check if required file exist
        if (!fileReadable(EMPLOYEE_FILE) || !fileReadable(ATTENDANCE_FILE)) {
            System.out.println("One or more required CSV files are missing or unreadable.");
            System.out.println("Please check the resources folder.");
            scanner.close();
            return;
        }

        System.out.println("===== MotorPH Basic Payroll System =====");
        System.out.print("Enter username: ");
        String username = scanner.nextLine().trim();

        System.out.print("Enter password: ");
        String password = scanner.nextLine().trim();

        // Proceeding based on username and password
        if (username.equals(EMPLOYEE_USERNAME) && password.equals(COMMON_PASSWORD)) {
            employeeMenu(scanner);
        } else if (username.equals(PAYROLL_USERNAME) && password.equals(COMMON_PASSWORD)) {
            payrollStaffMenu(scanner);
        } else {
            System.out.println("Incorrect username and/or password.");
        }

        scanner.close();
    }

    /** 
    *Displays the employee menu
    *Employees can view their details or exit the program
    *
    *@param scanner Scanner object for reading user input
    */
    static void employeeMenu(Scanner scanner) {
        while (true) {
            System.out.println("\n===== Employee Menu =====");
            System.out.println("1. Enter your employee number");
            System.out.println("2. Exit the program");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine().trim();

            if (choice.equals("1")) {
                System.out.print("Enter employee number: ");
                String employeeNumber = scanner.nextLine().trim();
                showEmployeeDetails(employeeNumber);
            } else if (choice.equals("2")) {
                System.out.println("Program terminated.");
                break;
            } else {
                System.out.println("Invalid option.");
            }
        }
    }

    /**
    *Display the payroll staff main menu
    *Payroll staff can process payroll or exit the program
    *
    *@param scanner Scanner object for reading user input
    */
    static void payrollStaffMenu(Scanner scanner) {
        while (true) {
            System.out.println("\n===== Payroll Staff Menu =====");
            System.out.println("1. Process Payroll");
            System.out.println("2. Exit the program");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine().trim();

            if (choice.equals("1")) {
                processPayrollMenu(scanner);
            } else if (choice.equals("2")) {
                System.out.println("Program terminated.");
                break;
            } else {
                System.out.println("Invalid option.");
            }
        }
    }

    /** 
    *Displays the payroll processing sub-menu
    *Payroll staff can process one employee, all employee, or exit the program
    *
    *@param scanner Scanner object for reading user input
    */
    static void processPayrollMenu(Scanner scanner) {
        while (true) {
            System.out.println("\n===== Process Payroll =====");
            System.out.println("1. One employee");
            System.out.println("2. All employees");
            System.out.println("3. Exit the program");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine().trim();

            if (choice.equals("1")) {
                System.out.print("Enter employee number: ");
                String employeeNumber = scanner.nextLine().trim();
                processOneEmployee(employeeNumber);
            } else if (choice.equals("2")) {
                processAllEmployees();
            } else if (choice.equals("3")) {
                System.out.println("Program terminated.");
                break;
            } else {
                System.out.println("Invalid option.");
            }
        }
    }

    /** 
    * Displays employee details for the given employee number.
    * Used by employee menu.
    *
    * @param employeeNumber The employee number to look up
    */
    static void showEmployeeDetails(String employeeNumber) {
        Map<String, String[]> employees = readEmployeeData();

        if (!employees.containsKey(employeeNumber)) {
            System.out.println("Employee number does not exist.");
            return;
        }

        String[] row = employees.get(employeeNumber);

        System.out.println("\nEmployee Number: " + clean(row[0]));
        System.out.println("Employee Name: " + clean(row[2]) + " " + clean(row[1]));
        System.out.println("Birthday: " + clean(row[3]));
    }

    /** 
    * Process payroll for one employee
    * 
    * @param employeeNumber The employee number to process
    */
    static void processOneEmployee(String employeeNumber) {
        Map<String, String[]> employees = readEmployeeData();
        Map<String, Map<String, Double>> attendanceHours = readAttendanceHours();

        if (!employees.containsKey(employeeNumber)) {
            System.out.println("Employee number does not exist.");
            return;
        }

        showPayrollForEmployee(employeeNumber, employees.get(employeeNumber), attendanceHours.get(employeeNumber));
    }

    /**
    * Process payroll for all employees
    */
    static void processAllEmployees() {
        Map<String, String[]> employees = readEmployeeData();
        Map<String, Map<String, Double>> attendanceHours = readAttendanceHours();

        for (String employeeNumber : employees.keySet()) {
            showPayrollForEmployee(employeeNumber, employees.get(employeeNumber), attendanceHours.get(employeeNumber));
        }
    }

    /**
    * Displays complete payroll records for an employee from June to December
    * Displays first and second cutoff with all deductions
    * @param employeeNumber The employee's ID number
    * @param employeeRow Array containing employee data from CSV
    * @param employeeHours Map of 
    */ 
    static void showPayrollForEmployee(String employeeNumber, String[] employeeRow, Map<String, Double> employeeHours) {
        String firstName = clean(employeeRow[2]);
        String lastName = clean(employeeRow[1]);
        String birthday = clean(employeeRow[3]);
        double hourlyRate = parseNumber(clean(employeeRow[18]));

        System.out.println("\n==============================================================");
        System.out.println("Employee #: " + employeeNumber);
        System.out.println("Employee Name: " + firstName + " " + lastName);
        System.out.println("Birthday: " + birthday);
        System.out.println("==============================================================");

        // Process each month from June to December
        for (int month = 6; month <= 12; month++) {
            String firstCutoffKey = "2024-" + twoDigits(month) + "-1";
            String secondCutoffKey = "2024-" + twoDigits(month) + "-2";

            // Hours for each cutoff, default to 0 if it is not found
            double firstCutoffHours = 0.0;
            double secondCutoffHours = 0.0;

            if (employeeHours != null) {
                if (employeeHours.containsKey(firstCutoffKey)) {
                    firstCutoffHours = employeeHours.get(firstCutoffKey);
                }
                if (employeeHours.containsKey(secondCutoffKey)) {
                    secondCutoffHours = employeeHours.get(secondCutoffKey);
                }
            }

            // Compute gross salaries
            double firstGross = firstCutoffHours * hourlyRate;
            double secondGross = secondCutoffHours * hourlyRate;
            
            // Compute deductions only after combining both cutoffs to get the total monthly gross.
            double monthlyGross = firstGross + secondGross;
            double sss = computeSSS(monthlyGross);
            double philHealth = computePhilHealth(monthlyGross);
            double pagIbig = computePagIbig(monthlyGross);
            double taxableIncome = monthlyGross - sss - philHealth - pagIbig;
            double tax = computeWithholdingTax(taxableIncome);
            double totalDeductions = sss + philHealth + pagIbig + tax;

            // Deductions apply only to second cutoff
            double firstNetSalary = firstGross;
            double secondNetSalary = secondGross - totalDeductions;

            YearMonth ym = YearMonth.of(2024, month);

            // Display first cutoff (1st to 15th)
            System.out.println("\nCutoff Date: " + monthName(month) + " 1 to " + monthName(month) + " 15");
            System.out.println("Total Hours Worked: " + firstCutoffHours);
            System.out.println("Gross Salary: " + firstGross);
            System.out.println("Net Salary: " + firstNetSalary);

            System.out.println();

            // Display second cutoff (16th to end of the month)
            System.out.println("Cutoff Date: " + monthName(month) + " 16 to " + monthName(month) + " " + ym.atEndOfMonth().getDayOfMonth());
            System.out.println("Total Hours Worked: " + secondCutoffHours);
            System.out.println("Gross Salary: " + secondGross);
            System.out.println("SSS: " + sss);
            System.out.println("PhilHealth: " + philHealth);
            System.out.println("Pag-IBIG: " + pagIbig);
            System.out.println("Tax: " + tax);
            System.out.println("Total Deductions: " + totalDeductions);
            System.out.println("Net Salary: " + secondNetSalary);
        }
    }

    /**
    * Read employee CSV data and stores in map
    * The map uses employee as key and the full row array as value.
    *
    * @return Map of employees numbers to employee data arrays
    */
    static Map<String, String[]> readEmployeeData() {
        Map<String, String[]> employees = new LinkedHashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(EMPLOYEE_FILE))) {
            String line = br.readLine(); // skip header

            while ((line = br.readLine()) != null) {
                String[] row = splitCsv(line);

                if (row.length >= 19) {
                    employees.put(clean(row[0]), row);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading employee data: " + e.getMessage());
        }

        return employees;
    }

    /** 
    * Read attendance and group total hours worked per cutoff.
    * Group hours by employees number and cutoff period.
    *
    * @return Map of employee numbers to map of cutoff periods to total hours
    */
    static Map<String, Map<String, Double>> readAttendanceHours() {
        Map<String, Map<String, Double>> allAttendance = new LinkedHashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(ATTENDANCE_FILE))) {
            String line = br.readLine(); // skip header

            while ((line = br.readLine()) != null) {
                String[] row = splitCsv(line);

                if (row.length < 6) {
                    continue;
                }

                String employeeNumber = clean(row[0]);
                String dateText = clean(row[3]);
                String logInText = clean(row[4]);
                String logOutText = clean(row[5]);

                // skip empty values
                if (employeeNumber.isEmpty() || dateText.isEmpty() || logInText.isEmpty() || logOutText.isEmpty()) {
                    continue;
                }

                try {
                    LocalDate date = LocalDate.parse(dateText, DATE_FORMAT);
                    LocalTime logIn = LocalTime.parse(logInText, TIME_FORMAT);
                    LocalTime logOut = LocalTime.parse(logOutText, TIME_FORMAT);

                    // Only process records from June to December
                    if (date.getMonthValue() < 6 || date.getMonthValue() > 12) {
                        continue;
                    }

                    double workedHours = computeHoursWorked(logIn, logOut);
                    String cutoffKey = getCutoffKey(date);

                    
                    allAttendance.putIfAbsent(employeeNumber, new LinkedHashMap<String, Double>());
                    Map<String, Double> employeeCutoffHours = allAttendance.get(employeeNumber);

                    // 
                    if (!employeeCutoffHours.containsKey(cutoffKey)) {
                        employeeCutoffHours.put(cutoffKey, 0.0);
                    }
                    
                    employeeCutoffHours.put(cutoffKey, employeeCutoffHours.get(cutoffKey) + workedHours);

                } catch (Exception e) {
                    // Skip malformed rows
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading attendance data: " + e.getMessage());
        }

        return allAttendance;
    }

    /** 
    * Compute hours worked based on company policies:
    * Work hours are from 8:00AM - 5:00PM only
    * Grace period up to 8:10 AM counts as 8:00AM
    * one hour lunch break
    *
    *@param actualLogIn The actual login time
    *@param actualLogOut The actual logout time
    *@return Total hours worked
    */
    static double computeHoursWorked(LocalTime actualLogIn, LocalTime actualLogOut) {
        LocalTime officialStart = LocalTime.of(8, 0);
        LocalTime officialEnd = LocalTime.of(17, 0);
        LocalTime graceTime = LocalTime.of(8, 10);

        // Cap logout at 5:00 PM
        LocalTime adjustedOut = actualLogOut.isAfter(officialEnd) ? officialEnd : actualLogOut;

        // Grace period: up to 8:10 AM counts as 8:00 AM
        LocalTime adjustedIn;
        if (actualLogIn.isBefore(officialStart) || !actualLogIn.isAfter(graceTime)) {
            adjustedIn = officialStart;
        } else {
            adjustedIn = actualLogIn;
        }

        // Check if logout is after login
        if (!adjustedOut.isAfter(adjustedIn)) {
            return 0.0;
        }

        // Calculates total minutes worked
        double workedMinutes = Duration.between(adjustedIn, adjustedOut).toMinutes();
        double workedHours = workedMinutes / 60.0;

        // Deduct 1 hour lunch break to match the given examples
        workedHours = workedHours - 1.0;

        if (workedHours < 0.0) {
            return 0.0;
        }

        return workedHours;
    }

    /**
    * Determines the cutoff key
    * First cutoff: 1st to 15th of the month
    * Second cutoff: 16th to end of the month
    *
    * @param date The date to clasify
    * @return String date format 
    */
    static String getCutoffKey(LocalDate date) {
        String yearMonth = date.getYear() + "-" + twoDigits(date.getMonthValue());

        if (date.getDayOfMonth() <= 15) {
            return yearMonth + "-1";
        } else {
            return yearMonth + "-2";
        }
    }

    /** 
    * Compute SSS contribution based on monthly gross salary
    *
    * @param monthlyGross Monthly gross salary
    * @return SSS contribution amount
    */
    static double computeSSS(double monthlyGross) {
        if (monthlyGross < 3250) return 135.0;
        else if (monthlyGross < 3750) return 157.5;
        else if (monthlyGross < 4250) return 180.0;
        else if (monthlyGross < 4750) return 202.5;
        else if (monthlyGross < 5250) return 225.0;
        else if (monthlyGross < 5750) return 247.5;
        else if (monthlyGross < 6250) return 270.0;
        else if (monthlyGross < 6750) return 292.5;
        else if (monthlyGross < 7250) return 315.0;
        else if (monthlyGross < 7750) return 337.5;
        else if (monthlyGross < 8250) return 360.0;
        else if (monthlyGross < 8750) return 382.5;
        else if (monthlyGross < 9250) return 405.0;
        else if (monthlyGross < 9750) return 427.5;
        else if (monthlyGross < 10250) return 450.0;
        else if (monthlyGross < 10750) return 472.5;
        else if (monthlyGross < 11250) return 495.0;
        else if (monthlyGross < 11750) return 517.5;
        else if (monthlyGross < 12250) return 540.0;
        else if (monthlyGross < 12750) return 562.5;
        else if (monthlyGross < 13250) return 585.0;
        else if (monthlyGross < 13750) return 607.5;
        else if (monthlyGross < 14250) return 630.0;
        else if (monthlyGross < 14750) return 652.5;
        else if (monthlyGross < 15250) return 675.0;
        else if (monthlyGross < 15750) return 697.5;
        else if (monthlyGross < 16250) return 720.0;
        else if (monthlyGross < 16750) return 742.5;
        else if (monthlyGross < 17250) return 765.0;
        else if (monthlyGross < 17750) return 787.5;
        else if (monthlyGross < 18250) return 810.0;
        else if (monthlyGross < 18750) return 832.5;
        else if (monthlyGross < 19250) return 855.0;
        else if (monthlyGross < 19750) return 877.5;
        else if (monthlyGross < 20250) return 900.0;
        else if (monthlyGross < 20750) return 922.5;
        else if (monthlyGross < 21250) return 945.0;
        else if (monthlyGross < 21750) return 967.5;
        else if (monthlyGross < 22250) return 990.0;
        else if (monthlyGross < 22750) return 1012.5;
        else if (monthlyGross < 23250) return 1035.0;
        else if (monthlyGross < 23750) return 1057.5;
        else if (monthlyGross < 24250) return 1080.0;
        else if (monthlyGross < 24750) return 1102.5;
        else return 1125.0;
    }

    /**
    * Computes PhilHealth deduction
    * Premium rate is 3% of monthly gross
    * Monthly gross is capped at 10,000 and 60,000
    *
    * @param monthlyGross Monthly gross salary
    * @return Employee's Philhealth 
    */
    static double computePhilHealth(double monthlyGross) {
        double premiumRate = 0.03;
        double salaryFloor = 10000.0;
        double salaryCeiling = 60000.0;

        double basis = monthlyGross;

        if (basis < salaryFloor) {
            basis = salaryFloor;
        } else if (basis > salaryCeiling) {
            basis = salaryCeiling;
        }

        double monthlyPremium = basis * premiumRate;
        return monthlyPremium / 2.0;
    }

    /**
    * Computes Pag-IBIG deduction
    * 1% for salary up to 1,500, 2% for salary above 1,500
    * Maximum contribution is 100
    *
    * @param monthlyGross Monthly gross salaray
    * @return Pag-IBIG deduction amount
    */
    static double computePagIbig(double monthlyGross) {
        double contribution;

        if (monthlyGross >= 1000 && monthlyGross <= 1500) {
            contribution = monthlyGross * 0.01;
        } else {
            contribution = monthlyGross * 0.02;
        }

        if (contribution > 100.0) {
            contribution = 100.0;
        }

        return contribution;
    }

    /**
     * Withholding tax deduction
     * 
     * @param taxableIncome Income after SSS, PhilHealth, and Pag-IBIG deductions
     * @return Witholding tax amount
     */
    static double computeWithholdingTax(double taxableIncome) {
        if (taxableIncome <= 20832) {
            return 0.0;
        } else if (taxableIncome < 33333) {
            return (taxableIncome - 20833) * 0.20;
        } else if (taxableIncome < 66667) {
            return 2500 + ((taxableIncome - 33333) * 0.25);
        } else if (taxableIncome < 166667) {
            return 10833 + ((taxableIncome - 66667) * 0.30);
        } else if (taxableIncome < 666667) {
            return 40833.33 + ((taxableIncome - 166667) * 0.32);
        } else {
            return 200833.33 + ((taxableIncome - 666667) * 0.35);
        }
    }

    /**
    * Check if file is readable
    *
    * @param filePath Path to the file
    * @return true if file can be read, false otherwise
    */
    static boolean fileReadable(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
    * Split CSV while respecting quoted commas.
    *
    * @param line The CSV line to split
    * @return Array of field values 
    */
    static String[] splitCsv(String line) {
        return line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
    }

    /**
    * Remove quotes and trims whitespace from a string
    *
    * @param text The text to clean
    * @return Cleaned string
    */
    static String clean(String text) {
        return text.trim().replace("\"", "");
    }

    /**
    * Convert a string to double, handling commas and empty strings.
    *
    * @param text The text to test
    * @return Double value, or 0.0 if test fails
    */
    static double parseNumber(String text) {
        String cleaned = clean(text).replace(",", "");

        if (cleaned.isEmpty()) {
            return 0.0;
        }

        return Double.parseDouble(cleaned);
    }

    /** 
    * Format a number as a two-digit string.
    *
    * @param number The number to format
    * @return Two-digit string
    */
    static String twoDigits(int number) {
        return number < 10 ? "0" + number : String.valueOf(number);
    }

    /*
    * Convert month number to month name
    *
    * @param month Month number (1-12)
    * @return Month name
    */
    static String monthName(int month) {
        switch (month) {
            case 1: return "January";
            case 2: return "February";
            case 3: return "March";
            case 4: return "April";
            case 5: return "May";
            case 6: return "June";
            case 7: return "July";
            case 8: return "August";
            case 9: return "September";
            case 10: return "October";
            case 11: return "November";
            case 12: return "December";
            default: return "";
        }
    }
}
