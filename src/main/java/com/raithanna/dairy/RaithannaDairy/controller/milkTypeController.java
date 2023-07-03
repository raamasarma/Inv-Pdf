package com.raithanna.dairy.RaithannaDairy.controller;

import com.raithanna.dairy.RaithannaDairy.models.milktype;
import com.raithanna.dairy.RaithannaDairy.repositories.MilktypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Controller
public class milkTypeController {
    @Autowired
    private MilktypeRepository milktypeRepository;
    @GetMapping("/milktype")
    private String milktypeForm(Model model, HttpSession session){
        milktype mk=new milktype();
        model.addAttribute("milktype",mk);
        return "milktype";
    }
    @PostMapping("/milktype")
    public String saveMilkType(Model model,milktype mk){
        milktypeRepository.save(mk);
        milktype mt=new milktype();
        model.addAttribute("milktype",mk);
        return "redirect:/milktype";
    }
}
