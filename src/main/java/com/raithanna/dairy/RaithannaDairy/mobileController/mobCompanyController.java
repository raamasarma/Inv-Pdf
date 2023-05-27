package com.raithanna.dairy.RaithannaDairy.mobileController;

import com.raithanna.dairy.RaithannaDairy.models.company;
import com.raithanna.dairy.RaithannaDairy.models.supplier;
import com.raithanna.dairy.RaithannaDairy.repositories.CompanyRepository;
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
public class mobCompanyController {
    @Autowired
    private CompanyRepository companyRepository;
    @PostMapping(value = "/company", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<String> company(Model model, @RequestBody company cmp, HttpServletRequest request, HttpSession session){
        List<String> messages = new ArrayList<>();
        System.out.println(cmp);
        try {
            companyRepository.save(cmp);
            return ResponseEntity.status(202).body("Successfully Created Company(CODE 202)\n");
        } catch (Exception handlerException) {
            model.addAttribute("messages", messages);
            return ResponseEntity.status(203).body("Error Creating your Companypls retry (CODE 203)\n");
        }
    }

}
