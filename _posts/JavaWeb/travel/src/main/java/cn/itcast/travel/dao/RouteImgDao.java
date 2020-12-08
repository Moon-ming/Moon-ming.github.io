package cn.itcast.travel.dao;

import cn.itcast.travel.domain.RouteImg;

import java.util.List;

/**
 * 根据route的id查询图片
 */
public interface RouteImgDao {
    public List<RouteImg> findByRid(int rid);
}
