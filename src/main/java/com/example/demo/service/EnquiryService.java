package com.example.demo.service;

import com.example.demo.dto.CustomerEnquriyDTO;
import com.example.demo.dto.EnquiryDTO;
import com.example.demo.entity.Enquiry;
import com.example.demo.entity.User;
import com.example.demo.repository.EnquiryRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnquiryService {

    private final EnquiryRepository enquiryRepository;
    private final UserRepository userRepository; // عشان نجيب الـ User بالـ id

    public EnquiryService(EnquiryRepository enquiryRepository, UserRepository userRepository) {
        this.enquiryRepository = enquiryRepository;
        this.userRepository = userRepository;
    }

    public EnquiryDTO createEnquiry(CustomerEnquriyDTO request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // أو email حسب الـ UserDetails

        User customer = userRepository.findByUsername(username)  // لازم يكون عندك method في UserRepository
                .orElseThrow(() -> new RuntimeException("Customer not found: " + username));

        Enquiry enquiry = new Enquiry();
        enquiry.setContent(request.getContent());
        enquiry.setCustomer(customer);
        enquiry.setCreatedAt(OffsetDateTime.now());

        Enquiry saved = enquiryRepository.save(enquiry);
        return mapToDTO(saved);
    }

    // List all enquiries
    public List<EnquiryDTO> listAllEnquiries() {
        return enquiryRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Update enquiry
    public EnquiryDTO replyToEnquiry(Long id, String replyContent) {
        Enquiry enquiry = enquiryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Enquiry not found with id " + id));

        enquiry.setReplyContent(replyContent);

        Enquiry saved = enquiryRepository.save(enquiry);
        return mapToDTO(saved);
    }

    private EnquiryDTO mapToDTO(Enquiry enquiry) {
        EnquiryDTO dto = new EnquiryDTO();
        dto.setId(enquiry.getId());
        dto.setContent(enquiry.getContent());
        dto.setReplyContent(enquiry.getReplyContent());
        dto.setCustomerId(enquiry.getCustomer() != null ? enquiry.getCustomer().getId() : null);
        dto.setCreatedAt(enquiry.getCreatedAt() != null
                ? enquiry.getCreatedAt().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
                : null);
        return dto;
    }
}
