package com.FlowerShop.FlowerShop.controllers;

import com.FlowerShop.FlowerShop.FileUploadUtil;
import com.FlowerShop.FlowerShop.models.Flower;
import com.FlowerShop.FlowerShop.repositories.CategoryRepository;
import com.FlowerShop.FlowerShop.repositories.FlowerRepository;
import com.FlowerShop.FlowerShop.repositories.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping(value = "/flower")
public class FlowerController {
    @Autowired
    private FlowerRepository flowerRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @GetMapping(value = "")
    public String index(Model model) {
        List<Flower> flowerList = flowerRepository.findAll();
        model.addAttribute("activePage", "flower");
        model.addAttribute("flowerList", flowerList);
        return "admin/Flower/index";
    }

    @GetMapping(value = "/create")
    public String create(Model model) {
        model.addAttribute("activePage", "flower");
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("suppliers", supplierRepository.findAll());
        return "admin/Flower/create";
    }

    @PostMapping(value = "/create")
    public String create(@ModelAttribute Flower flower, @RequestParam("image") MultipartFile multipartFile) throws IOException {
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

        fileName = fileName.replaceAll(" ","");
        flower.setImg(fileName);
        Flower savedFlower = flowerRepository.save(flower);
        String uploadDir = "flowers/" + savedFlower.getId();
        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

        return "redirect:/flower";
    }

    @GetMapping("/edit")
    public String edit(Model model, @RequestParam("id") int id) {
        Flower flower = flowerRepository.getById(id);
        model.addAttribute("activePage", "flower");
        model.addAttribute("flower", flower);
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("suppliers", supplierRepository.findAll());
        return "admin/Flower/edit";
    }

    @PostMapping("/edit")
    public String edit(@ModelAttribute("flower") Flower flower, @RequestParam(value = "image", required = false) MultipartFile multipartFile) throws IOException {
        if (multipartFile == null){
            flower.setImg(flowerRepository.getById(flower.getId()).getImg());
            flowerRepository.save(flower);
        }
        else {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            fileName = fileName.replaceAll(" ","");
            flower.setImg(fileName);
            Flower savedFlower = flowerRepository.save(flower);
            String uploadDir = "flowers/" + savedFlower.getId();
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        }

        return "redirect:/flower";
    }

    @GetMapping("/delete")
    public String delete(Model model, @RequestParam("id") int id) {
        Flower flower = flowerRepository.getById(id);
        model.addAttribute("activePage", "flower");
        model.addAttribute("flower", flower);
        return "admin/Flower/delete";
    }

    @PostMapping("/delete")
    public String delete(@ModelAttribute("flower") Flower flower) {
        flowerRepository.deleteById(flower.getId());
        return "redirect:/flower";
    }
}
