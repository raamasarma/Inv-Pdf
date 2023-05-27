package com.raithanna.dairy.RaithannaDairy.mobileController;

import com.raithanna.dairy.RaithannaDairy.models.bank;
import com.raithanna.dairy.RaithannaDairy.models.supplier;
import com.raithanna.dairy.RaithannaDairy.repositories.BankRepository;
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
import java.util.List;

@Controller
 class mobBankController {
    @Autowired
    private BankRepository bankRepository;
    @PostMapping(value = "/bank", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<String> bank(Model model, @RequestBody bank b, HttpServletRequest request, HttpSession session){
        List<String> messages = new ArrayList<>();
        System.out.println(b);
        try {
            bankRepository.save(b);
            return ResponseEntity.status(202).body("Successfully Created Bank(CODE 202)\n");
        } catch (Exception handlerException) {
            model.addAttribute("messages", messages);
            return ResponseEntity.status(203).body("Error Creating your Bank pls retry (CODE 203)\n");
        }
    }
}
