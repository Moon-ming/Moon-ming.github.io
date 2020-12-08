package io.moomin.service;

import io.moomin.domain.Province;

import java.util.List;

public interface ProvinceService {
    public List<Province> findAll();

    public String findAllJson();
}
