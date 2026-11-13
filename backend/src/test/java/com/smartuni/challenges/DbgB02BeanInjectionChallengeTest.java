package com.smartuni.challenges;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * DBG-B02: Registration emails must go through the SANDBOX sender in this configuration.
 * The context cannot even start. Fix the configuration so it starts AND the mailer uses the sandbox sender,
 * without deleting the SMTP bean (other code needs it).
 */
@Tag("challenge")
@Tag("DBG-B02")
class DbgB02BeanInjectionChallengeTest {

    interface EmailSender {
        String name();
    }

    static class SmtpEmailSender implements EmailSender {
        public String name() {
            return "smtp";
        }
    }

    static class SandboxEmailSender implements EmailSender {
        public String name() {
            return "sandbox";
        }
    }

    static class RegistrationMailer {

        private final EmailSender emailSender;

        RegistrationMailer(EmailSender emailSender) {
            this.emailSender = emailSender;
        }

        String senderName() {
            return emailSender.name();
        }
    }

    @TestConfiguration
    static class MailConfig {

        @Bean
        EmailSender smtpEmailSender() {
            return new SmtpEmailSender();
        }

        @Bean
        EmailSender sandboxEmailSender() {
            return new SandboxEmailSender();
        }

        @Bean
        RegistrationMailer registrationMailer(EmailSender emailSender) {
            return new RegistrationMailer(emailSender);
        }
    }

    @Test
    void mailerUsesSandboxSender() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MailConfig.class)) {
            RegistrationMailer mailer = context.getBean(RegistrationMailer.class);
            assertThat(mailer.senderName()).isEqualTo("sandbox");
            assertThat(context.getBeansOfType(EmailSender.class)).hasSize(2);
        }
    }
}
