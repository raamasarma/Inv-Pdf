package com.raithanna.dairy.RaithannaDairy.mobileController;

import com.raithanna.dairy.RaithannaDairy.models.*;
import com.raithanna.dairy.RaithannaDairy.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
        System.out.println("body1:" + body);
        Map body2 = new HashMap();
        List Customers = new ArrayList<>();
        for (customer n : customerRepository.findAll()) {
            List Data = new ArrayList<>();
            Data.add(n.getCustName());
            Data.add(n.getCustCode());
            Customers.add(Data);
        }
        List Products = new ArrayList<>();
        for (productMaster p : productMasterRepository.findAll()) {
            List Product = new ArrayList<>();
            Product.add(p.getUnitRate().toString());
            Product.add(p.getPCode());
            Products.add(Product);
        }
       saleOrder order=saleOrderRepository.findTopByOrderByOrderNoDesc();
        int orderNo;
        if(order==null){
             orderNo = 1;
        }else{
            System.out.println("getOrdeNo:" +order.getOrderNo());
            orderNo= order.getOrderNo()+1;
            System.out.println("orderNo:" +  orderNo);
        }
        LocalDate date = LocalDate.parse(body.get("date").toString(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        body2.putIfAbsent("customers", Customers);
        body2.putIfAbsent("Products", Products);
        body2.putIfAbsent("orderNo", orderNo);

        return ResponseEntity.status(202).body(body2);
    }

    @PostMapping(value = "/saleOrderMob", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Map> saleOrderMob(@RequestBody Map<String, String> body, Model model, HttpServletRequest request, HttpSession session) {
        System.out.println("1111111111111111111111");
        System.out.println("body2:"+body);
        dailySales ds = new dailySales();
        LocalDate date1 = LocalDate.parse(body.get("date").toString(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));

        System.out.println("date:"+date1);
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

        saleOrder so = new saleOrder();
        so.setOrderNo(Integer.parseInt(body.get("orderNo")));
        so.setName(body.get("name"));
        so.setUnitRate(Double.parseDouble(body.get("unitRate")));
        so.setCustCode(body.get("custCode"));
        System.out.println("2222222222222222222222222");
        System.out.println("date:"+body.get("date").toString());
        so.setDate(body.get("date"));
        saleOrderRepository.save(so);


        Map<String, String> respBody = new HashMap<>();
        List<String> messages = new ArrayList<>();
        messages.add("Successfully Created");
        model.addAttribute("messages", messages);
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

    @PostMapping(value = "/getSales", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Map> getSales(@RequestBody Map<String, String> body, Model model, HttpServletRequest request, HttpSession session) {
        System.out.println("333333333333333333333333");
        System.out.println("body3:"+body);
        Map body2 = new HashMap();
        List orderNumbers = new ArrayList();
        String date = body.get("date").toString();
        System.out.println("4444444444444444444444444444444");
        System.out.println("date:"+date);

       for(saleOrder order :saleOrderRepository.findByDate(body.get("date").toString())) {


           System.out.println("orderNo:" + order.getOrderNo());

           orderNumbers.add(order.getOrderNo());

       }

        body2.putIfAbsent("orderNumbers", orderNumbers);
        return ResponseEntity.status(202).body(body2);
    }

        @PostMapping(value = "/getSO", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
        public ResponseEntity<Map> getSO(@RequestBody Map<String, String> body, Model model, HttpServletRequest request, HttpSession session ){
          System.out.println("body4:"+body);
        String orderNo = body.get("orderNo");
        if (orderNo==null) {
            return ResponseEntity.status(202).body(new HashMap<>());
        }
        Map body2 = new HashMap();
        saleOrder so = saleOrderRepository.findByOrderNo(orderNo);

        body2.putIfAbsent("custCode", so.getOrderNo());
        body2.putIfAbsent("date", so.getDate());
        body2.putIfAbsent("custCode", so.getCustCode());
        body2.putIfAbsent("amount", so.getAmount());
        body2.putIfAbsent("disc", so.getDisc());
        body2.putIfAbsent("netAmount", so.getNetAmount());
        body2.putIfAbsent("unitRate", so.getUnitRate());
        return ResponseEntity.status(202).body(body2);
    }
    @PostMapping(value = "/updatesaleOrderMob", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Map> updateSales(@RequestBody Map<String, String> body, Model model, HttpServletRequest request, HttpSession session) {

        System.out.println("update");
        System.out.println("body5:"+body);
        dailySales ds= dailySlaesRepository.findByOrderNo(body.get("orderNo"));
        ds.setDate((body.get("date")));
        ds.setCustCode(body.get("custCode"));
        ds.setProdCode(body.get("prodCode"));
        ds.setDisc(Double.parseDouble(body.get("disc")));
        ds.setAmount(Double.parseDouble(body.get("amount")));
        ds.setNetAmount(Double.parseDouble(body.get("netAmount")));
        ds.setQuantity(Double.parseDouble(body.get("quantity")));
        ds.setUnitRate(Double.parseDouble(body.get("date")));
        Map<String,String> respBody = new HashMap<>();
        List<String> messages = new ArrayList<>();
        messages.add("Successfully Updated");
        model.addAttribute("messages", messages );
        return ResponseEntity.status(202).body(new HashMap());

    }
}

