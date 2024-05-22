package ru.mirea.infinitejourneysbackend.unit.service.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import ru.mirea.infinitejourneysbackend.domain.model.*;
import ru.mirea.infinitejourneysbackend.service.email.EmailService;

import java.lang.reflect.Field;
import java.time.OffsetDateTime;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    @Mock
    private Order order;

    @Mock
    private User buyer;

    @Mock
    private Tour tour;

    @Mock
    private Country country;

    @Mock
    private User seller;

    @BeforeEach
    void setUp() throws Exception {
        Field mailAddressField = EmailService.class.getDeclaredField("mailAddress");
        mailAddressField.setAccessible(true);
        mailAddressField.set(emailService, "test@mail.com");
    }

    @Test
    void sendEmail_Success() throws MessagingException {
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        when(order.getBuyer()).thenReturn(buyer);
        when(buyer.getEmail()).thenReturn("buyer@example.com");
        when(buyer.getUsername()).thenReturn("BuyerName");
        when(order.getId()).thenReturn(123L);
        when(order.getTour()).thenReturn(tour);
        when(tour.getTitle()).thenReturn("TourTitle");
        when(tour.getDescription()).thenReturn("TourDescription");
        when(tour.getStartDate()).thenReturn(OffsetDateTime.now());
        when(tour.getEndDate()).thenReturn(OffsetDateTime.now().plusDays(7));
        when(tour.getCountry()).thenReturn(country);
        when(country.getName()).thenReturn("CountryName");
        when(tour.getPrice()).thenReturn(100.0);
        when(tour.getSeller()).thenReturn(seller);
        when(seller.getUsername()).thenReturn("SellerName");
        when(seller.getEmail()).thenReturn("seller@example.com");

        // Act
        emailService.sendEmail(order);

        // Assert
        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }
}
