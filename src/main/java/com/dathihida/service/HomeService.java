package com.dathihida.service;

import com.dathihida.model.Home;
import com.dathihida.model.HomeCategory;

import java.util.List;

public interface HomeService {
    public Home createHomePageData(List<HomeCategory> allCategories);
}
