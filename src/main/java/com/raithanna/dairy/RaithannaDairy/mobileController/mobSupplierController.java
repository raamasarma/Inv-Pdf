package com.raithanna.dairy.RaithannaDairy.mobileController;

import com.raithanna.dairy.RaithannaDairy.models.supplier;
import com.raithanna.dairy.RaithannaDairy.models.userModel;
import com.raithanna.dairy.RaithannaDairy.repositories.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
 class mobSupplierContrlooer {
    @Autowired
    private SupplierRepository supplierRepository;
    @PostMapping(value = "/supplier", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<String> supplier(Model model, @RequestBody supplier sr, HttpServletRequest request, HttpSession session){
        List<String> messages = new ArrayList<>();
        System.out.println(sr);
        String supName = sr.getSupplierName();
        String initials = Arrays.stream(supName.split(" "))
                .map(s -> s.substring(0, 1))
                .collect(Collectors.joining());
        System.out.println("----"+initials);
        Long sup = supplierRepository.count();
        Integer count = (int) (sup+1);
        String format = String.format("%03d", count);
        sr.setSupplierCode(initials.toUpperCase()+"_"+format);


        try {
            supplierRepository.save(sr);
            return ResponseEntity.status(202).body("Successfully Created Supplier(CODE 202)\n");
        } catch (Exception handlerException) {
            model.addAttribute("messages", messages);
            return ResponseEntity.status(203).body("Error Creating your supplier pls retry (CODE 203)\n");
        }
    }

}
