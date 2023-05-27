package com.raithanna.dairy.RaithannaDairy.mobileController;

import com.raithanna.dairy.RaithannaDairy.models.*;
import com.raithanna.dairy.RaithannaDairy.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class mobSaleController {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private DailySalesRepository dailySlaesRepository;
    @Autowired
    private SaleOrderRepository saleOrderRepository;
    @Autowired
    private ProductMasterRepository productMasterRepository;
    private DateTimeFormatter dateFormatter1 = DateTimeFormatter.ofPattern("ddMMyy");

    @PostMapping(value = "/getOrderDetails", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Map> getOrderDetails(@RequestBody Map<String, String> body, Model model, HttpServletRequest request, HttpSession session) {
        System.out.println(body);
        Map body2 = new HashMap();
        List Customers = new ArrayList<>();
        for (customer n :customerRepository.findAll()){
            List Data =  new ArrayList<>();
            Data.add(n.getCustName());
            Data.add(n.getCustCode());
            Customers.add(Data);
        }
        List Products=new ArrayList<>();
        for(productMaster p:productMasterRepository.findAll()){
            List Product=new ArrayList<>();
            Product.add(p.getUnitRate().toString());
            Product.add(p.getPCode());
            Products.add(Product);
        }
        saleOrder saleOrder = saleOrderRepository.findTopByOrderByOrderNoDesc();
        Integer orderNo;
        if (saleOrder == null) {
            orderNo = Integer.valueOf(String.valueOf(1));
            System.out.println(orderNo);
        } else {
            orderNo = Integer.valueOf(saleOrder.getOrderNo()) + 1;
        }
//      saleOrder sale=  saleOrderRepository.findByCustCode(body.get("custCode"));
//        System.out.println("sale:"+sale);
        LocalDate date = LocalDate.parse(body.get("date").toString(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        body2.putIfAbsent("customers", Customers);
        body2.putIfAbsent("Products", Products);
        body2.putIfAbsent("orderNo", orderNo);
        return ResponseEntity.status(202).body(body2);
    }
    @PostMapping(value = "/saleOrderMob", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Map> saveSales(@RequestBody Map<String, String> body, Model model, HttpServletRequest request, HttpSession session) {
        System.out.println("1111111111111111111111");
        System.out.println(body);
        dailySales ds=new dailySales();
        ds.setDate(String.valueOf(((body.get("date")))));
        ds.setOrderNo(String.valueOf((body.get("orderNo"))));
        ds.setProdCode(body.get("prodCode"));
        ds.setCustCode(body.get("custCode"));
        ds.setDisc(Double.valueOf(body.get("disc")));
        ds.setNetAmount(Double.valueOf(body.get("netAmount")));
        ds.setAmount(Double.valueOf(body.get("amount")));
        ds.setQuantity(Double.valueOf(body.get("quantity")));
        ds.setUnitRate(Double.valueOf(body.get("unitRate")));




       // ds.setCreationDate(LocalDate.parse(body.get("creationDate")));
       // ds.setName(body.get("name"));
       // ds.setProdCode(body.get("prodCode"));
        dailySlaesRepository.save(ds);

        saleOrder so=new saleOrder();
        so.setOrderNo(body.get("orderNo"));
        so.setName(body.get("name"));
        so.setUnitRate(Double.parseDouble(body.get("unitRate")));
        so.setCustCode(body.get("custCode"));
        saleOrderRepository.save(so);


        Map<String,String> respBody = new HashMap<>();
        List<String> messages = new ArrayList<>();
        messages.add("Successfully Created");
        model.addAttribute("messages", messages );
        return ResponseEntity.status(202).body(new HashMap());


    }
    @PostMapping(value = "/saleMobile", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<String> registerMob(Model model, @RequestBody dailySales sales, HttpServletRequest request, HttpSession session) {
        List<String> messages = new ArrayList<>();
        System.out.println(sales);
        try {
          dailySlaesRepository.save(sales);
            return ResponseEntity.status(202).body("Successfully Created(CODE 202)\n");
        } catch (Exception handlerException) {
            model.addAttribute("messages", messages);
            return ResponseEntity.status(203).body("Error Creating your Account pls retry (CODE 203)\n");
        }

    }
}
// try figuring out how to solve the date error i am done with frontend work and disconnecting
//ok