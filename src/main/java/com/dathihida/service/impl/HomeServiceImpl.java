package com.dathihida.service.impl;

import com.dathihida.domain.HomeCategorySection;
import com.dathihida.model.Deal;
import com.dathihida.model.Home;
import com.dathihida.model.HomeCategory;
import com.dathihida.repository.DealRepository;
import com.dathihida.service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HomeServiceImpl implements HomeService {

    private final DealRepository dealRepository;

    @Override
    public Home createHomePageData(List<HomeCategory> allCategories) {
        List<HomeCategory> gridCategories = allCategories.stream().filter(category->
                category.getSection() == HomeCategorySection.GRID).toList();

        List<HomeCategory> shopByCategories = allCategories.stream().filter(category->
                category.getSection() == HomeCategorySection.SHOP_BY_CATEGORIES).toList();

        List<HomeCategory> electricCategories = allCategories.stream().filter(category->
                category.getSection() == HomeCategorySection.ELECTRIC_CATEGORIES).toList();

        List<HomeCategory> dealCategories = allCategories.stream().filter(category->
                category.getSection() == HomeCategorySection.DEALS).toList();

        List<Deal> createDealsData = new ArrayList<>();
        if(dealRepository.findAll().isEmpty()){
            List<Deal> deals = allCategories.stream()
                    .filter(category-> category.getSection() == HomeCategorySection.DEALS)
                    .map(category -> new Deal(null, 10, category)).toList();
            createDealsData = dealRepository.saveAll(deals);
        }else {
            createDealsData = dealRepository.findAll();
        }

        Home home = new Home();
        home.setGrid(gridCategories);
        home.setShopByCategories(shopByCategories);
        home.setDealCategories(dealCategories);
        home.setElectricalCategories(electricCategories);
        home.setDeals(createDealsData);

        return home;
    }
}
