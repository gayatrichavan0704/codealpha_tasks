import java.util.*;
import java.io.*;

class Student {

    String name;
    int marks;

    Student(String name, int marks) {
        this.name = name;
        this.marks = marks;
    }

    String getGrade() {

        if (marks >= 90)
            return "A+";
        else if (marks >= 80)
            return "A";
        else if (marks >= 70)
            return "B";
        else if (marks >= 60)
            return "C";
        else if (marks >= 50)
            return "D";
        else
            return "F";
    }
}

public class StudentGradeTracker {

    static ArrayList<Student> students = new ArrayList<>();
    static final String FILE_NAME = "students.txt";

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        loadStudents();

        while (true) {

            System.out.println("\n===== STUDENT GRADE TRACKER =====");
            System.out.println("1. Add Student");
            System.out.println("2. View Student Report");
            System.out.println("3. Search Student");
            System.out.println("4. Exit");

            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            if (choice == 1) {
                addStudent(sc);
            }
            else if (choice == 2) {
                viewReport();
            }
            else if (choice == 3) {
                searchStudent(sc);
            }
            else if (choice == 4) {

                saveStudents();

                System.out.println(
                    "Data saved successfully!"
                );

                System.out.println(
                    "Thank you for using Student Grade Tracker!"
                );

                break;
            }
            else {
                System.out.println(
                    "Invalid choice! Please try again."
                );
            }
        }

        sc.close();
    }

    static void addStudent(Scanner sc) {

        System.out.print("\nEnter student name: ");
        String name = sc.nextLine();

        int marks;

        while (true) {

            System.out.print("Enter marks (0-100): ");
            marks = sc.nextInt();
            sc.nextLine();

            if (marks >= 0 && marks <= 100) {
                break;
            }

            System.out.println(
                "Invalid marks! Please enter between 0 and 100."
            );
        }

        Student student = new Student(name, marks);
        students.add(student);

        saveStudents();

        System.out.println(
            "Student added and saved successfully!"
        );
    }

    static void viewReport() {

        if (students.isEmpty()) {
            System.out.println("\nNo students available.");
            return;
        }

        int total = 0;
        int highest = students.get(0).marks;
        int lowest = students.get(0).marks;

        System.out.println("\n===== STUDENT REPORT =====");

        for (Student student : students) {

            System.out.println(
                student.name + " - " +
                student.marks + " - Grade " +
                student.getGrade()
            );

            total += student.marks;

            if (student.marks > highest)
                highest = student.marks;

            if (student.marks < lowest)
                lowest = student.marks;
        }

        double average = (double) total / students.size();

        System.out.println("\n===== SUMMARY =====");
        System.out.println("Average Marks : " + average);
        System.out.println("Highest Marks : " + highest);
        System.out.println("Lowest Marks  : " + lowest);
    }

    static void searchStudent(Scanner sc) {

        System.out.print("\nEnter student name to search: ");
        String searchName = sc.nextLine();

        boolean found = false;

        for (Student student : students) {

            if (student.name.equalsIgnoreCase(searchName)) {

                System.out.println("\nStudent Found!");
                System.out.println("Name  : " + student.name);
                System.out.println("Marks : " + student.marks);
                System.out.println("Grade : " + student.getGrade());

                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("Student not found.");
        }
    }

    static void saveStudents() {

        try {

            FileWriter writer = new FileWriter(FILE_NAME);

            for (Student student : students) {

                writer.write(
                    student.name + "," + student.marks + "\n"
                );
            }

            writer.close();

        } catch (IOException e) {

            System.out.println(
                "Error while saving student data."
            );
        }
    }

    static void loadStudents() {

        File file = new File(FILE_NAME);

        if (!file.exists()) {
            return;
        }

        try {

            Scanner fileScanner = new Scanner(file);

            while (fileScanner.hasNextLine()) {

                String line = fileScanner.nextLine();
                String[] data = line.split(",");

                if (data.length == 2) {

                    String name = data[0];
                    int marks = Integer.parseInt(data[1]);

                    students.add(
                        new Student(name, marks)
                    );
                }
            }

            fileScanner.close();

        } catch (Exception e) {

            System.out.println(
                "Error while loading student data."
            );
        }
    }
}