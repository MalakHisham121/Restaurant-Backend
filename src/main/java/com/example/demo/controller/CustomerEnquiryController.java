package com.example.demo.controller;

import com.example.demo.dto.CustomerEnquriyDTO;
import com.example.demo.dto.EnquiryDTO;
import com.example.demo.service.EnquiryService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer/enquiries")
public class CustomerEnquiryController {

    private final EnquiryService enquiryService;

    public CustomerEnquiryController(EnquiryService enquiryService) {
        this.enquiryService = enquiryService;
    }

    // POST: /api/customer/enquiries/create
    @PostMapping("/create")
    public EnquiryDTO createEnquiry(@RequestBody CustomerEnquriyDTO request) {
        return enquiryService.createEnquiry(request);
    }
}
