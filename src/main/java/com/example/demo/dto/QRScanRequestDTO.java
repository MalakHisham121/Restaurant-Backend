package com.example.demo.dto;

public class QRScanRequestDTO {
    private String qrCode;

    public QRScanRequestDTO() {}

    public QRScanRequestDTO(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }
}
