package com.acmetelecom.Implementations;

import com.acmetelecom.Interfaces.IBillGenerator;
import com.acmetelecom.Interfaces.Printer;
import com.acmetelecom.Utils.LineItem;
import com.acmetelecom.Utils.MoneyFormatter;
import com.acmetelecom.customer.Customer;

import java.util.List;

public class BillGenerator implements IBillGenerator {
    @Override
	public void send(Customer customer, List<LineItem> calls, String totalBill) {

        Printer printer = HtmlPrinter.getInstance();
        printer.printHeading(customer.getFullName(), customer.getPhoneNumber(), customer.getPricePlan());
        for (LineItem call : calls) {
            printer.printItem(call.date(), call.callee(), call.durationMinutes(), MoneyFormatter.penceToPounds(call.cost()));
        }
        printer.printTotal(totalBill);
    }

}
