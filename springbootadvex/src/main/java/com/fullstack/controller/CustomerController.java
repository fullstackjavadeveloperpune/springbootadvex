package com.fullstack.controller;

import com.fullstack.exception.RecordNotFoundException;
import com.fullstack.model.Customer;
import com.fullstack.service.ICustomerService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/customers")
@Slf4j
public class CustomerController {

    @Autowired
    private ICustomerService customerService;

    @PostMapping("/signup")
    public ResponseEntity<Customer> signUp(@Valid @RequestBody Customer customer) {
        log.info("#Trying to Signup for Customer: " + customer.getCustName());
        return new ResponseEntity<>(customerService.signUp(customer), HttpStatus.CREATED);
    }

    @PostMapping("/saveall")
    public ResponseEntity<List<Customer>> saveAll(@Valid @RequestBody List<Customer> customerList) {
        return new ResponseEntity<>(customerService.saveAll(customerList), HttpStatus.OK);
    }


    @GetMapping("/signin/{custEmailId}/{custPassword}")
    public ResponseEntity<Boolean> signIn(@PathVariable String custEmailId, @PathVariable String custPassword) {
        return new ResponseEntity<>(customerService.signIn(custEmailId, custPassword), HttpStatus.OK);
    }

    @GetMapping("/findbyid/{custId}")
    public ResponseEntity<Optional<Customer>> findById(@PathVariable int custId) {
        return new ResponseEntity<>(customerService.findById(custId), HttpStatus.OK);
    }


    @GetMapping("/findall")
    public ResponseEntity<List<Customer>> findAll() {
        return new ResponseEntity<>(customerService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/findbyname/{custName}")
    public ResponseEntity<List<Customer>> findByName(@PathVariable String custName) {
        return new ResponseEntity<>(customerService.findAll().stream().filter(cust -> cust.getCustName().equals(custName)).toList(), HttpStatus.OK);
    }

    @GetMapping("/findbyemail")
    public ResponseEntity<Customer> findByEmail(@RequestParam String custEmailId) {
        return new ResponseEntity<>(customerService.findAll().stream().filter(cust -> cust.getCustEmailId().equals(custEmailId)).toList().get(0), HttpStatus.OK);
    }

    @GetMapping("/findbycontactnumber")
    public ResponseEntity<Customer> findByContactNumber(@RequestParam long custContactNumber) {
        return new ResponseEntity<>(customerService.findAll().stream().filter(cust -> cust.getCustContactNumber() == custContactNumber).toList().get(0), HttpStatus.OK);
    }

    @GetMapping("/findbydob/{custDOB}")
    public ResponseEntity<List<Customer>> findByDOB(@PathVariable String custDOB) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

        return new ResponseEntity<>(customerService.findAll().stream().filter(cust -> simpleDateFormat.format(cust.getCustDOB()).equals(custDOB)).toList(), HttpStatus.OK);
    }

    @GetMapping("/sortbyiddesc")
    public ResponseEntity<List<Customer>> sortById() {
        return new ResponseEntity<>(customerService.findAll().stream().sorted(Comparator.comparing(Customer::getCustId).reversed()).toList(), HttpStatus.OK);
    }

    @GetMapping("/sortbynamedesc")
    public ResponseEntity<List<Customer>> sortByName() {
        return new ResponseEntity<>(customerService.findAll().stream().sorted(Comparator.comparing(Customer::getCustName).reversed()).toList(), HttpStatus.OK);
    }

    @GetMapping("/sortbyaccbal")
    public ResponseEntity<List<Customer>> sortByAccBalance() {
        return new ResponseEntity<>(customerService.findAll().stream().sorted(Comparator.comparing(Customer::getCustAccBalance)).toList(), HttpStatus.OK);
    }

    @GetMapping("/sortbydobdesc")
    public ResponseEntity<List<Customer>> sortByDOB() {
        return new ResponseEntity<>(customerService.findAll().stream().sorted(Comparator.comparing(Customer::getCustDOB).reversed()).toList(), HttpStatus.OK);
    }

    @GetMapping("/findbyanyinput")
    public ResponseEntity<List<Customer>> findByAnyInput(@RequestParam String input) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

        return new ResponseEntity<>(customerService.findAll().stream().filter(cust -> cust.getCustName().equals(input)
                || simpleDateFormat.format(cust.getCustDOB()).equals(input)
                || String.valueOf(cust.getCustId()).equals(input)
                || cust.getCustEmailId().equals(input)
                || String.valueOf(cust.getCustContactNumber()).equals(input)).toList(), HttpStatus.OK);

    }


    @GetMapping("/checkloaneligibility/{custId}")
    public ResponseEntity<String> checkLoanEligibility(@PathVariable int custId) {

        //  String msg = "";

        Customer customer = customerService.findById(custId).orElseThrow(() -> new RecordNotFoundException("Customer #ID Does Not Exist"));

      /*  if (customer.getCustAccBalance() >= 50000.00) {
            msg = "Eligible for Loan";
        } else {
            msg = "Not Eligible for Loan";
        }*/

        return new ResponseEntity<>(customer.getCustAccBalance() >= 50000.00 ? "Eligible for Loan" : "Not Eligible for Loan", HttpStatus.OK);
    }

    @PutMapping("/update/{custId}")
    public ResponseEntity<Customer> update(@PathVariable int custId, @Valid @RequestBody Customer customer) {
        Customer customer1 = customerService.findById(custId).orElseThrow(() -> new RecordNotFoundException("Customer #ID Does Not Exist"));

        customer1.setCustName(customer.getCustName());
        customer1.setCustAddress(customer.getCustAddress());
        customer1.setCustPassword(customer.getCustPassword());
        customer1.setCustEmailId(customer.getCustEmailId());
        customer1.setCustContactNumber(customer.getCustContactNumber());
        customer1.setCustAccBalance(customer.getCustAccBalance());
        customer1.setCustDOB(customer.getCustDOB());

        return new ResponseEntity<>(customerService.update(customer1), HttpStatus.CREATED);

    }

    @PatchMapping("/changeaddress/{custId}/{custAddress}")
    public ResponseEntity<Customer> changeAddress(@PathVariable int custId, @PathVariable String custAddress) {
        Customer customer1 = customerService.findById(custId).orElseThrow(() -> new RecordNotFoundException("Customer #ID Does Not Exist"));
        customer1.setCustAddress(custAddress);

        return new ResponseEntity<>(customerService.update(customer1), HttpStatus.CREATED);
    }


    @DeleteMapping("/deletebyid/{custId}")
    public ResponseEntity<String> deleteById(@PathVariable int custId) {

        customerService.deleteById(custId);

        return new ResponseEntity<>("Data Deleted Successfully", HttpStatus.OK);
    }

    @DeleteMapping("/deleteall")
    public ResponseEntity<String> deleteAll() {
        customerService.deleteAll();
        return new ResponseEntity<>("All Data Deleted Successfully", HttpStatus.OK);
    }

}
