package com.raithanna.dairy.RaithannaDairy.controller;
import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import com.raithanna.dairy.RaithannaDairy.ServiceImpl.Utility.DownloadCsvReport;
import com.raithanna.dairy.RaithannaDairy.models.*;
import com.raithanna.dairy.RaithannaDairy.repositories.*;
import com.raithanna.dairy.RaithannaDairy.utilities.NumtoWords;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

import javax.naming.Context;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.*;
@Controller
public class purchaseOrderController {
    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private BankRepository bankRepository;
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    NumtoWords numtoWords;

    @GetMapping("/purchase")
    public String purchaseOrderForm(Model model, HttpSession session) {
        if (session.getAttribute("loggedIn").equals("yes")) {
            List<supplier> Suppliers = supplierRepository.findByOrderByIdDesc();
            //List<purchaseOrder> Amts = purchaseOrderRepository.findByOrderByAmtDesc();
            List<bank> bank = bankRepository.findByOrderByIdDesc();
            List<vehicle> vehicle =vehicleRepository.findByOrderByIdDesc();
            System.out.println(Suppliers.size());
            purchaseOrder po = new purchaseOrder();
            model.addAttribute("purchase", po);
            model.addAttribute("supplier", Suppliers);
            model.addAttribute("bank", bank);
            model.addAttribute("vehicle", vehicle);
            return "purchase";
        }
        List messages = new ArrayList<>();
        messages.add("Login First");
        model.addAttribute("messages", messages);
        return "redirect:/loginPage";
    }
    @Autowired
    SpringTemplateEngine springTemplateEngine;
   @GetMapping("/viewInvoice")
   public ResponseEntity viewInvoice(@RequestParam(name = "invoiceNo", defaultValue = "1") String invoiceNo,@RequestParam(name = "orgCode", defaultValue = "1") String orgCode,HttpServletResponse response,HttpServletRequest request) {
       //List<purchaseOrder> po = purchaseOrderRepository.findByInvNo(invoiceNo);
       purchaseOrder po = purchaseOrderRepository.findByInvNo(invoiceNo);
       String t=po.getInvNo().split("/")[0];
       System.out.println("t:"+t);
       System.out.println("Hello World");
      List< purchaseOrder> po1 = purchaseOrderRepository.findByInvNoContaining(t);
              System.out.println("Po:"+po1.size());
     // purchaseOrder p1= purchaseOrderRepository.findByOrderNo(orderNo);
       System.out.println("Po:"+po);
       // purchaseOrder oc = purchaseOrderRepository.findByOrgCode(orgCode);
       String date=po.getInvDate();
       String OrgCode= po.getOrgCode();
       String suplCode =po.getSuplCode();
       System.out.println(suplCode);
       System.out.println(OrgCode);
       Double ttlamt= po.getTotAmtRound();
       String words = numtoWords.convert(ttlamt.toString().split("\\.")[0]);
       supplier sm= supplierRepository.findBySupplierCode(suplCode);
       System.out.println("sm:"+sm);
       company cp=companyRepository.findByOrgCode(orgCode);
       System.out.println("cp:"+cp);
       WebContext context = new WebContext(request, response, request.getServletContext());
       System.out.println(cp.getOrgLogo());
       context.setVariable("logo", cp.getOrgLogo());
       context.setVariable("banner", cp.getOrgBanner());
       context.setVariable("invoiceNo",invoiceNo);
       context.setVariable("po",po);
       context.setVariable("po1",po1);
        context.setVariable("date",date);
       context.setVariable("sm",sm);
       context.setVariable("cp",cp);
       context.setVariable("words",words);
       String finalhtml = springTemplateEngine.process("purchasepdf",context);
       ByteArrayOutputStream ops = new ByteArrayOutputStream();
       ITextRenderer renderer = new ITextRenderer();
       renderer.setDocumentFromString(finalhtml);
       renderer.layout();
       renderer.createPDF(ops, false);
       renderer.finishPDF();
       return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=supplier.pdf").contentType(MediaType.APPLICATION_OCTET_STREAM).body(ops.toByteArray());
   }
    @GetMapping("/viewInvoice1")
    public ResponseEntity viewInvoice1(@RequestParam(name = "invoiceNo", defaultValue = "1") String invoiceNo,@RequestParam(name = "orgCode", defaultValue = "1") String orgCode,HttpServletResponse response,HttpServletRequest request) {
        //List<purchaseOrder> po = purchaseOrderRepository.findByInvNo(invoiceNo);
        purchaseOrder po = purchaseOrderRepository.findByInvNo(invoiceNo);
        // purchaseOrder p1= purchaseOrderRepository.findByOrderNo(orderNo);
        System.out.println("Po:"+po);
        // purchaseOrder oc = purchaseOrderRepository.findByOrgCode(orgCode);
        String date=po.getInvDate();
        String OrgCode= po.getOrgCode();
        String suplCode =po.getSuplCode();
        System.out.println(suplCode);
        System.out.println(OrgCode);
        Double ttlamt= po.getTotAmtRound();
        String words = numtoWords.convert(ttlamt.toString().split("\\.")[0]);
        supplier sm= supplierRepository.findBySupplierCode(suplCode);
        System.out.println("sm:"+sm);
        company cp=companyRepository.findByOrgCode(orgCode);
        System.out.println("cp:"+cp);
        WebContext context = new WebContext(request, response, request.getServletContext());
        System.out.println(cp.getOrgLogo());
        context.setVariable("logo", cp.getOrgLogo());
        context.setVariable("banner", cp.getOrgBanner());
        context.setVariable("invoiceNo",invoiceNo);
        context.setVariable("po",po);
        context.setVariable("date",date);
        context.setVariable("sm",sm);
        context.setVariable("cp",cp);
        context.setVariable("words",words);
        String finalhtml = springTemplateEngine.process("purchasepdf",context);
        ByteArrayOutputStream ops = new ByteArrayOutputStream();
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(finalhtml);
        renderer.layout();
        renderer.createPDF(ops, false);
        renderer.finishPDF();
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=supplier.pdf").contentType(MediaType.APPLICATION_OCTET_STREAM).body(ops.toByteArray());
    }


    @GetMapping("/purchaseExcelData")
    public String purchaseExcelOrderForm(Model model, HttpSession session) {
        if (session.getAttribute("loggedIn").equals("yes")) {
            List<supplier> Suppliers = supplierRepository.findByOrderByIdDesc();
            purchaseOrder po = new purchaseOrder();
            model.addAttribute("purchase", po);
            model.addAttribute("supplier", Suppliers);
            return "purchaseExcel";
        }
        List messages = new ArrayList<>();
        messages.add("Login First");
        model.addAttribute("messages", messages);
        return "redirect:/loginPage";
    }
    @PostMapping("/purchaseOrderNew")
    public ModelAndView purchaseOrderForm(Model model,@RequestBody List<purchaseOrder> purchaseList, HttpSession session) {
        String invNo ="";
        if (session.getAttribute("loggedIn").equals("yes")) {
            // List<purchaseOrder> list1 = purchaseOrderRepository.findBySupplierAndInvDate(purchaseList.get(0).getSupplier(),purchaseList.get(0).getInvDate());

            System.out.println(purchaseList.get(0).getInvDate());
            List<purchaseOrder> list1 = purchaseOrderRepository.findByInvDate(purchaseList.get(0).getInvDate());
            Set<String> supplierName = new HashSet<>();
            for (purchaseOrder str : list1) {
                System.out.println("set --"+str.getSuplCode()+"-"+str.getInvDate());
                supplierName.add(str.getInvNo());
            }
            System.out.println(list1.size());
            System.out.println(supplierName.size());
            String format = String.format("%03d", supplierName.size() + 1);
            invNo =  purchaseList.get(0).getInvDate() + "-" + purchaseList.get(0).getSuplCode() + "-" + format + "/";
           // String orderNo=purchaseList.get(0).getInvDate() + "-" + purchaseList.get(0).getSuplCode() + "-" + format;
            //System.out.println(orderNo);
            int subOrder = 0;
            for (purchaseOrder list : purchaseList) {
                subOrder = subOrder+1;
                purchaseOrder po = new purchaseOrder();
                // sno from ui
               // po.setSlNo(list.getSlNo());
                po.setInvDate(list.getInvDate());
                po.setSupplier(list.getSupplier());
                po.setVehNumber(list.getVehNumber());
                po.setMilkType(list.getMilkType());
                po.setQuantity(list.getQuantity());
                po.setFatP(list.getFatP());
                po.setSnfP(list.getSnfP());
                po.setTsRate(list.getTsRate());
                po.setLtrRate(list.getLtrRate());
                po.setAmt(list.getAmt());
                //po.setOrderNo(list.getOrderNo());
                po.setPaymentStatus(list.getPaymentStatus());
               // po.setBankrefno(list.getBankrefno());
                po.setBankIfsc(list.getBankIfsc());
                po.setRecDate(list.getRecDate());
                po.setSuplCode(list.getSuplCode());
                po.setInvNo(invNo+subOrder);
               // po.setCode(list.getCode());
               // po.setOrderNo(orderNo);
                po.setInvType(list.getInvType());
                po.setTmode(list.getTmode());
                po.setUnit(list.getUnit());
                po.setPoRefNo(list.getPoRefNo());
                po.setPoRefDate(list.getPoRefDate());
                po.setOrgCode(list.getOrgCode());
                po.setTotAmtRound(list.getTotAmtRound());
                po.setBankrefno(list.getBankrefno());
                purchaseOrderRepository.save(po);
            }
            return new ModelAndView("/loginPage");
        }
        List messages = new ArrayList<>();
        messages.add("Login First");
        model.addAttribute("messages", messages);
        return new ModelAndView("/loginPage");
    }
    @PostMapping("/purchaseExcelData")
    public String purchaseExcelData(@RequestParam Map<String, String> body, Model model, HttpServletResponse response, HttpServletRequest request) {
        System.out.println(body);
        purchaseOrder po = new purchaseOrder();
        System.out.println("SupplierCode:" + body.get("code"));
        po.setSupplier(body.get("supplierName"));
        po.setInvDate(body.get("invDate"));
        po.setRecDate(body.get("recDate"));
        // excel data list
        System.out.println("Supplier:" + body.get("supplier"));
        System.out.println("Supplier:" +po.getSupplier());
        List<purchaseOrder> list = purchaseOrderRepository.findBySuplCodeAndInvDateBetween(po.getSupplier(),po.getInvDate(),po.getRecDate());
        String header[] = {"invDate","supplier","vehNumber","quantity","fatP","snfP","tsRate","milkType","bankIfsc","paymentStatus"};
        DownloadCsvReport.getCsvReportDownload(response, header, list, "invoice_data.csv");
        System.out.println("Excel Size -- "+list.size());
        return "redirect:/purchaseExcelData";
    }
    @GetMapping("/purchaseViewData")
    public String purchaseViewOrderForm(Model model, HttpSession session) {
        if (session.getAttribute("loggedIn").equals("yes")) {
            List<supplier> Suppliers = supplierRepository.findByOrderByIdDesc();
            purchaseOrder po = new purchaseOrder();
            model.addAttribute("purchase", po);
            model.addAttribute("supplier", Suppliers);
            return "purchaseView";
        }
        List messages = new ArrayList<>();
        messages.add("Login First");
        model.addAttribute("messages", messages);
        return "redirect:/loginPage";
    }
    @PostMapping("/purchaseViewData")
    public String purchaseOrder(@RequestParam Map<String, String> body, Model model, HttpServletResponse response, HttpServletRequest request) {
        System.out.println(body);
        purchaseOrder po = new purchaseOrder();
        System.out.println("SupplierName:" + body.get("supplierName"));
        po.setSupplier(body.get("supplierName"));
        var supplier=body.get("SupplierName");
        po.setInvDate(body.get("invDate"));
        System.out.println(body.get("invDate"));
        po.setRecDate(body.get("recDate"));
        System.out.println("supplier:"+po.getSuplCode());
        System.out.println("supplier:"+po.getSupplier());
        // excel data list
        List<purchaseOrder> list = purchaseOrderRepository.findBySuplCodeAndInvDateBetween(po.getSupplier(), po.getInvDate(),po.getRecDate());
       // List<purchaseOrder> list = purchaseOrderRepository.findBySuplCode(po.getSupplier());
        List<supplier> Suppliers = supplierRepository.findByOrderByIdDesc();
        model.addAttribute("supplier", Suppliers);
        model.addAttribute("list", list);
        System.out.println("Excel Size -- " + list.size());
        return "/purchaseView";
    }


}
