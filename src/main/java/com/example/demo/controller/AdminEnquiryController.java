package com.example.demo.controller;

import com.example.demo.dto.EnquiryDTO;
import com.example.demo.service.EnquiryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/enquiries")
public class AdminEnquiryController {

    private final EnquiryService enquiryService;

    public AdminEnquiryController(EnquiryService enquiryService) {
        this.enquiryService = enquiryService;
    }

    @GetMapping("/list")
    public List<EnquiryDTO> listEnquiries() {
        return enquiryService.listAllEnquiries();
    }

    @PutMapping("/update/{id}")
    public EnquiryDTO replyToEnquiry(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String replyContent = body.get("replyContent");
        return enquiryService.replyToEnquiry(id, replyContent);
    }
}
