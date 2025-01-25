package com.dathihida.controller;

import com.dathihida.model.Home;
import com.dathihida.model.HomeCategory;
import com.dathihida.service.HomeCategoryService;
import com.dathihida.service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class HomeCategoryController {
    private final HomeCategoryService homeCategoryService;
    private final HomeService homeService;

    @PostMapping("/home/categories")
    public ResponseEntity<Home> addCategory(@RequestBody List<HomeCategory> homeCategory) {
        List<HomeCategory> categories = homeCategoryService.createCategories(homeCategory);
        Home home = homeService.createHomePageData(categories);
        return new ResponseEntity<>(home, HttpStatus.ACCEPTED);
    }

    @GetMapping("/admin/home-category")
    public ResponseEntity<List<HomeCategory>> getHomeCategory() {
        List<HomeCategory> categories = homeCategoryService.getAllHomeCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @PatchMapping("/admin/home-category/{id}")
    public ResponseEntity<HomeCategory> updateCategory(@PathVariable Long id, @RequestBody HomeCategory homeCategory) throws Exception {
        HomeCategory updateHomeCategory = homeCategoryService.updateHomeCategory(homeCategory, id);
        return new ResponseEntity<>(updateHomeCategory, HttpStatus.OK);
    }
}
