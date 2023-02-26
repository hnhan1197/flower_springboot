package com.FlowerShop.FlowerShop.controllers;

import com.FlowerShop.FlowerShop.repositories.FlowerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {
    @Autowired
    private FlowerRepository flowerRepository;
    @GetMapping("")
    public String getAdminPage(Model model){
        model.addAttribute("activePage", "home");
        return "admin/index";
    }
}