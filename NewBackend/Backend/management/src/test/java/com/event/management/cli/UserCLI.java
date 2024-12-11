package com.event.management.cli;

import com.event.management.dto.UserDto;
import com.event.management.dto.UserRegistrationDto;
import com.event.management.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class UserCLI {

    private final AuthService authService;

    public UserCLI(AuthService authService) {
        this.authService = authService;
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the User Management CLI!");

        while (true) {
            System.out.println("\nChoose an option:");
            System.out.println("1. Sign Up New User");
            System.out.println("2. List All Users");
            System.out.println("3. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline

            switch (choice) {
                case 1:
                    signUpUser(scanner);
                    break;
                case 2:
                    listUsers();
                    break;
                case 3:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    private void signUpUser(Scanner scanner) {
        System.out.println("Enter user details:");

        System.out.print("User Name: ");
        String userName = scanner.nextLine();

        System.out.print("Mobile Number: ");
        String userMobileNo = scanner.nextLine();

        System.out.print("Name: ");
        String name = scanner.nextLine();

        System.out.print("Address: ");
        String userAddress = scanner.nextLine();

        System.out.print("Specialization: ");
        String userSpecialization = scanner.nextLine();

        System.out.print("Email: ");
        String userEmail = scanner.nextLine();

        System.out.print("Password: ");
        String userPassword = scanner.nextLine();

        System.out.print("Role (e.g., USER or ADMIN): ");
        String userRole = scanner.nextLine();

        UserRegistrationDto userRegistrationDto = UserRegistrationDto.builder()
                .userName(userName)
                .userMobileNo(userMobileNo)
                .name(name)
                .userAddress(userAddress)
                .userSpecialization(userSpecialization)
                .userEmail(userEmail)
                .userPassword(userPassword)
                .userRole(userRole)
                .build();

        // Create a mock HttpServletResponse for the CLI
        HttpServletResponse httpServletResponse = new MockHttpServletResponse();

        try {
            // Passing the mock response to register the user
            authService.registerUser(userRegistrationDto, httpServletResponse); // HttpServletResponse used
            System.out.println("User signed up successfully!");
        } catch (Exception e) {
            System.out.println("Failed to sign up user: " + e.getMessage());
        }
    }

    private void listUsers() {
        System.out.println("Listing all users:");
        // Assume there's a method to list users from the service
        // userService.getAllUsers().forEach(user -> System.out.println(user.toString()));
    }
}
