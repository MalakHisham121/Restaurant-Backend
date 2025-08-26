package com.example.demo.controller;

import com.example.demo.dto.AboutResponseDTO;
import com.example.demo.dto.QRScanRequestDTO;
import com.example.demo.dto.QRScanResponseDTO;
import com.example.demo.service.MiscService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/misc")
public class MiscController {

    @Autowired
    private MiscService miscService;

    @GetMapping("/view/about")
    public ResponseEntity<?> getAboutInfo() {
        try {
            AboutResponseDTO aboutInfo = miscService.getAboutInfo();
            return ResponseEntity.ok(aboutInfo);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while retrieving about information: " + e.getMessage());
        }
    }

    @PostMapping("/scan/qr")
    public ResponseEntity<?> scanQRCode(@RequestBody QRScanRequestDTO request) {
        try {
            // Validate request
            if (request.getQrCode() == null || request.getQrCode().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("QR code is required");
            }

            QRScanResponseDTO response = miscService.scanQRCode(request);
            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while scanning QR code: " + e.getMessage());
        }
    }
}
