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

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;


@Controller
public class MainController {

    @Autowired
    private ProductTypeRepository productTypeRepository;

    @GetMapping("/")
    public String home(Model model) {
        LocalDate localDate = LocalDate.now();
        Date date = Date.valueOf(localDate);

        Iterable<ProductType> product = productTypeRepository.findAll();
        ArrayList<ProductType> arrayList = new ArrayList<>();


        product.forEach(arrayList::add);
        arrayList.sort(new Comparator<ProductType>() {
            @Override
            public int compare(ProductType o1, ProductType o2) {
                if (o1.getPriority() > o2.getPriority()) {
                    return -1;
                } else if (o1.getPriority() < o2.getPriority()) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });

        ArrayList<ProductType> types = new ArrayList<>();
        for (ProductType productType : arrayList) {
            if (!productType.getPublished_at().after(date)) {
                types.add(productType);
            }
        }
        model.addAttribute("product", types);
        return "home";
    }

    @PostMapping("/")
    public String homeAdd(@RequestParam String text,
                          @RequestParam String published_at, @RequestParam int priority, Model model) throws ParseException {
        try {
            String date = new String(published_at).replace("T", " ");
            DateFormat date2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            if (date.length() != 0) {
                java.util.Date time = date2.parse(date);
                ProductType productType = new ProductType(text, priority, time);
                productTypeRepository.save(productType);
            }
        } catch (ParseException e) {
            return e.getMessage();
        }
        return "redirect:/";
    }

    @GetMapping("/{id}")
    public String newBlog(@PathVariable(value = "id") long id, Model model) {
        if (!productTypeRepository.existsById(id)) {
            return "redirect:/";
        }
        Optional<ProductType> optionalProductType = productTypeRepository.findById(id);
        ArrayList<ProductType> productTypeArrayList = new ArrayList<>();
        optionalProductType.ifPresent(productTypeArrayList::add);
        model.addAttribute("productTypeArrayList", productTypeArrayList);
        return "content";
    }

    @PostMapping("/unpublished")
    public String homeUnpublished(Model model) {
        LocalDate localDate = LocalDate.now();
        Date date = Date.valueOf(localDate);

        Iterable<ProductType> product = productTypeRepository.findAll();
        ArrayList<ProductType> arrayList = new ArrayList<>();
        product.forEach(arrayList::add);

        arrayList.sort(new Comparator<ProductType>() {
            @Override
            public int compare(ProductType o1, ProductType o2) {
                if (o1.getPriority() > o2.getPriority()) {
                    return -1;
                } else if (o1.getPriority() < o2.getPriority()) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });

        ArrayList<ProductType> types = new ArrayList<>();
        for (ProductType productType : arrayList) {
            if (productType.getPublished_at().after(date)) {
                types.add(productType);
            }
        }

        model.addAttribute("unpublished", types);
        return "home";
    }
}
