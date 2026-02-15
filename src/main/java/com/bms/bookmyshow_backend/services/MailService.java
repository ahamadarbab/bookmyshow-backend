package com.bms.bookmyshow_backend.services;

import com.bms.bookmyshow_backend.models.Bill;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Slf4j
@Service
public class MailService {

    // With the help of java mail sender object
    // we will be able to send email to the customers.
    // To send the email we require emailId, password and the host

    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost("smtp.gmail.com");  // For now, email which I am using belongs to gmail so, the host will be smtp.gmail.com
        javaMailSender.setPort(587);  // generally to send mail from our computer we require some port number so, the port number which we will use is 587
        javaMailSender.setUsername("youremail@gmail.com");  // we will be sending email so, by which email our spring application will send mail to the users
        javaMailSender.setPassword("yourpassword");  // password of the email, it is the app password, not the actual email password
        Properties props = javaMailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", "true");  // Our springboot api will connect gmail to send email via password so, mail.smtp.auth is true
        props.put("mail.smtp.starttls.enable", "true");  // this property we are setting for secure connection
        return javaMailSender;
    }

    public void sendBillDetailsToCustomer(Bill bill) {
        // I want to generate a string that string will represent mail body
        // And that mail body is going to contain all the bill details
        StringBuilder ticketDetails = new StringBuilder();

        for(int i = 0; i < bill.getSeatBookings().size(); i++) {
            var booking = bill.getSeatBookings().get(i);
            ticketDetails.append(String.format(
                "Ticket %d:\n Seat: %s | Ticket ID: %s\n Movie: %s | Show: %s\n Time: %s\n\n",
                i + 1,
                booking.getSeatId(),
                booking.getTicketId(),
                booking.getShow().getMovie().getMovieName(),
                booking.getShow().getShowName(),
                booking.getShow().getDisplayStartTime()
            ));
        }

        String finalMailText = String.format(
            "========================================\n" +
            "    BOOKING CONFIRMATION RECEIPT\n" +
            "========================================\n\n" +
            "Dear %s,\n\n" +
            "Thank you for your booking!\n\n" +
            "BOOKING DETAILS\n" +
            "Bill ID: %s\n" +
            "Payment ID: %s\n" +
            "Payment Source: %s\n\n" +
            "THEATER INFORMATION\n" +
            "Theater: %s\n" +
            "Address: %s, %s, %s - %d\n\n" +
            "TICKET DETAILS\n" +
            "Number of Seats: %d\n\n" +
            "%s" +
            "TOTAL AMOUNT: ₹%.2f\n\n" +
            "========================================\n" +
            "Please arrive 15 minutes early.\n" +
            "Show this email at the counter.\n" +
            "Enjoy your movie!\n" +
            "========================================\n",
            bill.getCustomer().getUserName(),
            bill.getId(),
            bill.getPaymentId(),
            bill.getPaymentSource(),
            bill.getTheater().getTheaterName(),
            bill.getTheater().getAddress(),
            bill.getTheater().getCity(),
            bill.getTheater().getState(),
            bill.getTheater().getPincode(),
            bill.getSeatBookings().size(),
            ticketDetails.toString(),
            bill.getTotalPrice()
        );

        // How we will send the mail ->
        // So, to send the mail our api requires its own email id and password

        JavaMailSender javaMailSender = this.getJavaMailSender();
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        try {
            mimeMessageHelper.setSubject("Congratulations!! Booking confirmed");
            mimeMessageHelper.setText(finalMailText);
            mimeMessageHelper.setTo(bill.getCustomer().getEmail());
            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }
}
