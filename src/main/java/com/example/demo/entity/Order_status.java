package com.example.demo.entity;

import jakarta.persistence.Column;

public enum Order_status {
    PENDING,
    READY,
    DELIVERED,

    CANCELLED,

    COMPLETED
}
