package com.raithanna.dairy.RaithannaDairy.controller;

import com.raithanna.dairy.RaithannaDairy.models.company;
import com.raithanna.dairy.RaithannaDairy.repositories.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Controller
public class companyController {


    @Autowired
    private CompanyRepository companyRepository;

    @GetMapping("/company")
    private String createCompanyForm(Model model, HttpSession session){
        company cmp =new company();
        model.addAttribute("company",cmp);
        return "company";
    }
    @PostMapping("/company")
    public String savecreateCompany(Model model,company cmp){
        companyRepository.save(cmp);
        company Company=new company();
        model.addAttribute("company",cmp);
        return "redirect:/company";
    }

}

