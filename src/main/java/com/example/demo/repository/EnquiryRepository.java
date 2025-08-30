package com.example.demo.repository;

import com.example.demo.entity.Enquiry;

import java.util.List;

public interface EnquiryRepository extends BaseRepository<Enquiry,Long>{
    List<Enquiry> findByCustomerId(long CustomerId );
}