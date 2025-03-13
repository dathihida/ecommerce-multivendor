package com.dathihida.controller;

import com.dathihida.model.Home;
import com.dathihida.model.HomeCategory;
import com.dathihida.service.HomeCategoryService;
import com.dathihida.service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CustomerController {
    private final HomeCategoryService homeCategoryService;
    private final HomeService homeService;

//    @PostMapping("/home/categories")
//    public ResponseEntity<Home> createHomeCategories(@RequestBody List<HomeCategory> homeCategories) {
//        List<HomeCategory> categories = homeCategoryService.createCategories(homeCategories);
//        Home home = homeService.createHomePageData(categories);
//        return new ResponseEntity<>(home, HttpStatus.ACCEPTED);
//    }
}
