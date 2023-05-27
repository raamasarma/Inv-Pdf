package com.raithanna.dairy.RaithannaDairy.mobileController;

import com.raithanna.dairy.RaithannaDairy.models.bank;
import com.raithanna.dairy.RaithannaDairy.models.customer;
import com.raithanna.dairy.RaithannaDairy.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;

import static com.sun.org.apache.xml.internal.serializer.utils.Utils.messages;

//import static com.sun.org.apache.xml.internal.serializer.utils.Utils.messages;


@Controller
 class mobCustomerController {
    @Autowired
    private CustomerRepository customerRepository;

    @PostMapping(value = "/createCustomer", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<String> customer(Model model, @RequestBody customer c, HttpServletRequest request, HttpSession session){
        customer custWithMaxCustno = customerRepository.findTopByOrderByCustSeriesDesc();

        //String result="";
        Integer maxCust_no = 80;
        if (custWithMaxCustno != null) {
            maxCust_no = custWithMaxCustno.getCustSeries();
            System.out.println("max"+maxCust_no);
            maxCust_no++;
        }
        // customer  c =new customer();
        c.setCustSeries(maxCust_no);
        c.setCustCode("RDML00" + (maxCust_no));
        System.out.println("customer"+c);
        String mobileNo= c.getMobileNo();
        customer existingCheck = customerRepository.findByMobileNo(mobileNo);

        try {
            customerRepository.save(c);
            return ResponseEntity.status(202).body("Successfully Created Customer(CODE 202)\n");
        } catch (Exception handlerException) {
            model.addAttribute("messages", messages);
            return ResponseEntity.status(203).body("Error Creating your Customer pls retry (CODE 203)\n");
        }
    }

}
