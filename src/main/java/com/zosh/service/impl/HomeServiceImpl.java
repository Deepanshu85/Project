package com.zosh.service.impl;

import com.zosh.domain.HomeCategorySection;
import com.zosh.modal.Deal;
import com.zosh.modal.Home;
import com.zosh.modal.HomeCategory;
import com.zosh.repository.DealRepository;
import com.zosh.service.HomeService;
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
        List<HomeCategory> gridCategories = allCategories.stream()
                .filter(cateory->cateory.getSection()
                == HomeCategorySection.GRID).toList();
        List<HomeCategory> shopByCategories = allCategories.stream()
                .filter(category->category.getSection()
                ==HomeCategorySection.SHOP_BY_CATEGORIES).toList();
        List<HomeCategory> electricCategories = allCategories.stream()
                .filter(category->category.getSection()==
                        HomeCategorySection.ELECTRIC_CATEGORIES).toList();
        List<HomeCategory> dealCategories = allCategories.stream()
                .filter(category->category.getSection()==
                        HomeCategorySection.DEALS).toList();
        List<Deal> createdDeals = new ArrayList<>();
        if(dealRepository.findAll().isEmpty()){
            List<Deal> deals = allCategories.stream()
                    .filter(category->category.getSection()==
                            HomeCategorySection.DEALS).map(category->
                            new Deal(null,10,category)).collect(Collectors.toList());
            createdDeals= dealRepository.saveAll(deals);
        }else createdDeals = dealRepository.findAll();

        Home home = new Home();
        home.setGrid(gridCategories);
        home.setShopByCategories(shopByCategories);
        home.setShopByCategories(electricCategories);
        home.setDeals(createdDeals);
        home.setDealCategories(dealCategories);
        return home;
    }
}
