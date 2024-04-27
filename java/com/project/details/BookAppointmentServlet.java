package com.project.details;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/BookAppointmentServlet")
public class BookAppointmentServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError("Error loading JDBC driver: " + e.getMessage());
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String url = "jdbc:mysql://localhost:3306/pre_visit_info_db";
        String user = "root";
        String password = "";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connected successfully.");

            String sql = "INSERT INTO form_data (name, phone, age, userType, fieldOfStudy, accommodation, periodOfStay, interestCategory, businessInterests, outdoorMeetings, numPeople, placeOfInterest, budget, dateOfVisit) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                String name = request.getParameter("name");
                String phone = request.getParameter("phone");
                int age = Integer.parseInt(request.getParameter("age"));
                String userType = request.getParameter("userType");
                String fieldOfStudy = request.getParameter("fieldOfStudy");
                String accommodation = request.getParameter("accommodation");
                String periodOfStay = request.getParameter("periodOfStay");
                String interestCategory = request.getParameter("interestCategory");
                String businessInterests = request.getParameter("businessInterests");
                String outdoorMeetings = request.getParameter("outdoorMeetings");

                int numPeople = 0; // Default value or handle as appropriate for your application
                if ("tourist".equals(userType)) {
                    String numPeopleParam = request.getParameter("numPeople");
                    if (numPeopleParam != null && !numPeopleParam.isEmpty()) {
                        try {
                            numPeople = Integer.parseInt(numPeopleParam);
                            // Use 'numPeople' for further processing or set in PreparedStatement
                        } catch (NumberFormatException e) {
                            // Handle the case where the parameter is not a valid integer
                            e.printStackTrace(); // Or perform appropriate error handling
                        }
                    }
                }

                String placeOfInterest = request.getParameter("placeOfInterest");
                String budget = request.getParameter("budget");
                String dateOfVisit = request.getParameter("dateOfVisit");

                // Set parameters
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, phone);
                preparedStatement.setInt(3, age);
                preparedStatement.setString(4, userType);
                preparedStatement.setString(5, fieldOfStudy);
                preparedStatement.setString(6, accommodation);
                preparedStatement.setString(7, periodOfStay);
                preparedStatement.setString(8, interestCategory);
                preparedStatement.setString(9, businessInterests);
                preparedStatement.setString(10, outdoorMeetings);
                preparedStatement.setInt(11, numPeople);
                preparedStatement.setString(12, placeOfInterest);
                preparedStatement.setString(13, budget);
                preparedStatement.setString(14, dateOfVisit);

                int rowsInserted = preparedStatement.executeUpdate();
                System.out.println(rowsInserted + " row(s) inserted successfully.");

                // Display success popup and redirect to success page
                response.setContentType("text/html");
                PrintWriter out = response.getWriter();
                out.println("<script type=\"text/javascript\">");
                out.println("alert('Booking successful!');");
                out.println("window.location='ques1.html';");
                out.println("</script>");
            } catch (SQLException e) {
                System.out.println("Error occurred while inserting data: " + e.getMessage());

                // Display error popup and redirect to error page
                response.setContentType("text/html");
                PrintWriter out = response.getWriter();
                out.println("<script type=\"text/javascript\">");
                out.println("alert('Error occurred while booking!');");
                out.println("window.location='tourist_booking_error.jsp';");
                out.println("</script>");
            } catch (NumberFormatException e) {
                System.out.println("Error parsing number of people: " + e.getMessage());

                // Display error popup and redirect to error page
                response.setContentType("text/html");
                PrintWriter out = response.getWriter();
                out.println("<script type=\"text/javascript\">");
                out.println("alert('Error parsing number of people!');");
                out.println("window.location='error.jsp';"); // Specify an appropriate error page
                out.println("</script>");
            }
        } catch (SQLException e) {
            System.out.println("Error occurred while connecting to the database: " + e.getMessage());

            // Display database connection error popup and redirect to error page
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<script type=\"text/javascript\">");
            out.println("alert('Database connection error!');");
            out.println("window.location='db_connection_error.jsp';");
            out.println("</script>");
        }
    }
}
