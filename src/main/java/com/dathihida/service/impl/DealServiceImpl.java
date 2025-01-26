package com.dathihida.service.impl;

import com.dathihida.model.Deal;
import com.dathihida.model.HomeCategory;
import com.dathihida.repository.DealRepository;
import com.dathihida.repository.HomeCategoryRepository;
import com.dathihida.service.DealService;
import com.dathihida.service.HomeCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DealServiceImpl implements DealService {
    private final DealRepository dealRepository;
    private final HomeCategoryRepository homeCategoryRepository;
    @Override
    public List<Deal> getDeals() {
        return dealRepository.findAll();
    }

    @Override
    public Deal createDeal(Deal deal) throws Exception {
        HomeCategory category = homeCategoryRepository.findById(deal.getCategory().getId())
                .orElseThrow(()->new Exception("you can't find id"));
        Deal newDeal = new Deal();
        newDeal.setCategory(category);
        newDeal.setDiscount(deal.getDiscount());

        return dealRepository.save(newDeal);
    }

    @Override
    public Deal updateDeal(Deal deal, Long id) throws Exception {
        Deal existingDeal = dealRepository.findById(id)
                .orElseThrow(()-> new Exception("you can't id with found"));
        HomeCategory category = homeCategoryRepository.findById(deal.getCategory().getId())
                .orElseThrow(()-> new Exception("you can't id with found"));

        if (existingDeal != null){
            if(deal.getDiscount() != null){
                existingDeal.setDiscount(deal.getDiscount());
            }
            if(category != null){
                existingDeal.setCategory(category);
            }
            return dealRepository.save(existingDeal);
        }throw new Exception("Deal not found");
    }

    @Override
    public void deleteDeal(Long id) throws Exception {
        Deal deal = dealRepository.findById(id).orElseThrow(()-> new Exception("you can't id with found"));
        dealRepository.delete(deal);
    }
}
