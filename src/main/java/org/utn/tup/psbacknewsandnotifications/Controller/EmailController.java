package org.utn.tup.psbacknewsandnotifications.Controller;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.utn.tup.psbacknewsandnotifications.Dto.SendEmailDto;
import org.utn.tup.psbacknewsandnotifications.util.EmailHandler;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/api/comms")
@RequiredArgsConstructor
public class EmailController {
    private final EmailHandler handler;

    @PostMapping("/send-email")
    public ResponseEntity<?> sendEmail(SendEmailDto dto) throws MessagingException, UnsupportedEncodingException {
        handler.sendEmail(dto.getTo(), dto.getSubject(), dto.getBody());
        return new ResponseEntity<>("Email enviado correctamente", HttpStatus.OK);

    }
}
