package com.example.demo.controllers;

import com.example.demo.entity.ProductType;
import com.example.demo.repo.ProductTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Optional;

@Controller
public class ControllerArticle {

    @Autowired
    private ProductTypeRepository productTypeRepository;

    @GetMapping("blog/{id}/edit")
    public String blogEdit(@PathVariable(value = "id") long id, Model model) {
        if (!productTypeRepository.existsById(id)) {
            return "redirect:/";
        }
        Optional<ProductType> optionalProductType = productTypeRepository.findById(id);
        ArrayList<ProductType> productTypeArrayList = new ArrayList<>();
        optionalProductType.ifPresent(productTypeArrayList::add);
        model.addAttribute("productTypeArrayList", productTypeArrayList);
        return "blog-edit";
    }

    @PostMapping("blog/{id}/edit")
    public String blogPostUpdate(@PathVariable(value = "id") long id, @RequestParam String text,
                                 @RequestParam String published_at, @RequestParam int priority, Model model) throws ParseException {
        ProductType productType = productTypeRepository.findById(id).orElseThrow();
        productType.setText(text);
        productType.setPriority(priority);
        try {
            String date = new String(published_at).replace("T", " ");
            DateFormat date2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            if (date.length() != 0) {
                java.util.Date time = date2.parse(date);
                productType.setPublished_at(time);
                productTypeRepository.save(productType);
            } else {
                productType.setPublished_at(productType.getPublished_at());
                productTypeRepository.save(productType);
            }
        } catch (ParseException e) {
            return e.getMessage();
        }
        return "redirect:/{id}";
    }

    @PostMapping("/{id}/return")
    public String blogDelete(@PathVariable(value = "id") long id, Model model) {
        ProductType productType = productTypeRepository.findById(id).orElseThrow();
        productTypeRepository.delete(productType);
        return "redirect:/";
    }
}
