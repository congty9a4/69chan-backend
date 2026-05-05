package com.congty9a4.backend.service;

import com.congty9a4.backend.config.ResendConfig;
import com.congty9a4.backend.dto.req.ResendEmailRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jakarta.annotation.PostConstruct;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final RestTemplate restTemplate;
    private final ResendConfig resendConfig;

    @Qualifier("webApplicationContext")
    private final ResourceLoader resourceLoader;

    @Value("${resend.from.email}")
    private String domainEmail;

    @Value("${resend.from.name}")
    private String appName;

    private String otpTemplateHtml;

    @PostConstruct
    void loadOtpTemplate() {
        Resource template = resourceLoader.getResource("classpath:static/mail-template.html");
        try (InputStream input = template.getInputStream()) {
            this.otpTemplateHtml = new String(input.readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.warn("Failed to load mail template from classpath, falling back to inline HTML: {}", e.getMessage());
            this.otpTemplateHtml = null;
        }
    }

    @Async
    public void sendOtpEmail(String toEmail, String otp) {
        String htmlBody = renderOtpTemplate(toEmail, otp);

        // 2. Set up HTTP Headers (Attach the API Key for authentication)
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(resendConfig.getKey());

        // 3. Build the Request Payload (DTO)
        ResendEmailRequest payload = ResendEmailRequest.builder()
                .from(appName + " " + domainEmail)
                .to(List.of(toEmail))
                .subject("[69chan] Your Account Verification Code")
                .html(htmlBody)
                .build();

        // 4. Send the HTTP POST request to the Resend API
        HttpEntity<ResendEmailRequest> request = new HttpEntity<>(payload, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    resendConfig.getUrl(),
                    request,
                    Map.class);

            // 5. Handle the response and log the result
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                log.info("OTP email successfully sent to {} | Resend ID: {}", toEmail, response.getBody().get("id"));
            } else {
                log.error("Failed to send email. Resend API responded with error: {}", response.getBody());
            }
        } catch (Exception e) {
            log.error("Failed to connect to Resend API for {}: {}", toEmail, e.getMessage());
        }
    }

    private String renderOtpTemplate(String toEmail, String otp) {
        if (otpTemplateHtml == null || otpTemplateHtml.isBlank()) {
            return """
                    <div style=\"font-family: Arial, sans-serif; padding: 20px;\">
                        <div style=\"max-width: 600px; margin: 0 auto; border: 1px solid #ddd; padding: 20px; border-radius: 8px;\">
                            <h2 style=\"color: #00466a;\">69chan Social Network</h2>
                            <p>Hi there,</p>
                            <p>Thank you for choosing 69chan. Please use the following OTP to verify your account:</p>
                            <h2 style=\"background: #00466a; width: max-content; padding: 10px; color: #fff; border-radius: 4px;\">
                                %s
                            </h2>
                            <p>This OTP is valid for 5 minutes.</p>
                            <p>Regards,<br />69chan Team</p>
                        </div>
                    </div>
                    """
                    .formatted(otp);
        }

        String safeOtp = otp == null ? "" : otp.trim();
        String username = toEmail == null ? "" : toEmail.split("@", 2)[0];

        return otpTemplateHtml
                .replace("{{username}}", username)
                .replace("{{email}}", toEmail == null ? "" : toEmail)
                .replace("{{otp}}", safeOtp)
                .replace("{{d1}}", getDigit(safeOtp, 0))
                .replace("{{d2}}", getDigit(safeOtp, 1))
                .replace("{{d3}}", getDigit(safeOtp, 2))
                .replace("{{d4}}", getDigit(safeOtp, 3))
                .replace("{{d5}}", getDigit(safeOtp, 4))
                .replace("{{d6}}", getDigit(safeOtp, 5));
    }

    private String getDigit(String otp, int index) {
        if (otp == null || otp.length() <= index) {
            return "";
        }
        return String.valueOf(otp.charAt(index));
    }

}

