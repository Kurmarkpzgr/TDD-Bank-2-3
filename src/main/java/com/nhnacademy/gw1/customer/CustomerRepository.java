package com.nhnacademy.gw1.customer;

public interface CustomerRepository {

  Customer findById(Long customerId);
}

