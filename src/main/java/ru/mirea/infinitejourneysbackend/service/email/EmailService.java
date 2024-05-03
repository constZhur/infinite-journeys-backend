package ru.mirea.infinitejourneysbackend.service.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.mirea.infinitejourneysbackend.domain.model.Order;
import ru.mirea.infinitejourneysbackend.exception.mail.SendMessagingProblem;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String mailAddress;

    private final String subject =
            "Успешная покупка тура в сервисе Infinite Journeys";

    @Async
    public void sendEmail(Order order) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(message, true, "UTF-8");

            String buyerEmail = order.getBuyer().getEmail();
            String text = "Уважаемый " + order.getBuyer().getUsername() + ",\n\n" +
                    "Поздравляем с успешной покупкой тура!\n\n" +
                    "Информация о заказе:\n" +
                    "Идентификатор заказа: " + order.getId() + "\n" +
                    "Название тура: " + order.getTour().getTitle() + "\n" +
                    "Описание тура: " + order.getTour().getDescription() + "\n" +
                    "Начало тура: " + order.getTour().getStartDate().toLocalDate() + "\n" +
                    "Конец тура: " + order.getTour().getEndDate().toLocalDate() + "\n" +
                    "Страна: " + order.getTour().getCountry().getName() + "\n" +
                    "Цена: " + order.getTour().getPrice() + "\n" +
                    "Продавец: " + order.getTour().getSeller().getUsername() + ".\n\n" +
                    "Если возникнут вопросы, пишите на почту продавца: " + order.getTour().getSeller().getEmail() + ".\n\n" +
                    "C уважением, Infinite Journeys!";

            helper.setFrom(mailAddress);
            helper.setTo(buyerEmail);
            helper.setSubject(subject);
            helper.setText(text);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new SendMessagingProblem(order.getBuyer().getEmail());
        }
    }
}
