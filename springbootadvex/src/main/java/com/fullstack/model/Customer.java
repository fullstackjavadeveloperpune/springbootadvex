package com.fullstack.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.tomcat.util.descriptor.web.SecurityRoleRef;
import org.hibernate.validator.constraints.Range;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "CUSTOMER")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int custId;

    @Size(min = 2, message = "Customer Name should be at least 2 characters")
    private String custName;

    @NotNull(message = "Customer Address should not be null")
    private String custAddress;

    private double custAccBalance;

    @Range(min = 1000000000, max = 9999999999L, message = "Customer Contact Number must be 10 digit")
    @Column(unique = true)
    private long custContactNumber;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date custDOB;

    @Email(message = "Email ID must be valid")
    @Column(unique = true)
    private String custEmailId;

    @Size(min = 4, message = "Password should be at least 4 characters")
    private String custPassword;
}
