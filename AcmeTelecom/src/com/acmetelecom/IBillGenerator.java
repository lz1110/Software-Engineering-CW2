package com.acmetelecom;

import com.acmetelecom.customer.Customer;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ik610
 * Date: 11/11/13
 * Time: 11:24 AM
 * To change this template use File | Settings | File Templates.
 */
public interface IBillGenerator {
    public void send(Customer customer, List<LineItem> calls, String totalBill);
}
