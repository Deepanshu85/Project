package com.zosh.service;

import com.zosh.modal.Home;
import com.zosh.modal.HomeCategory;

import java.util.List;

public interface HomeService {
    public Home createHomePageData(List<HomeCategory> allCategories);
}
