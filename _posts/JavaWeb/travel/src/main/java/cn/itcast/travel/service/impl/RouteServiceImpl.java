package cn.itcast.travel.service.impl;


import cn.itcast.travel.dao.FavoriteDao;
import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.dao.RouteImgDao;
import cn.itcast.travel.dao.SellerDao;
import cn.itcast.travel.dao.impl.FavoriteDaoImpl;
import cn.itcast.travel.dao.impl.RouteDaoImpl;
import cn.itcast.travel.dao.impl.RouteImgDaoImpl;
import cn.itcast.travel.dao.impl.SellerDaoImpl;
import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;

import cn.itcast.travel.domain.RouteImg;
import cn.itcast.travel.domain.Seller;
import cn.itcast.travel.service.RouteService;

import java.util.List;

public class RouteServiceImpl implements RouteService {
    private RouteDao routeDao = new RouteDaoImpl();
    private RouteImgDao routeImgDao = new RouteImgDaoImpl();
    private SellerDao sellerDao = new SellerDaoImpl();
    private FavoriteDao favoriteDao = new FavoriteDaoImpl();
    /**
     * 根据类别进行分页查询
     * @param cid
     * @param currentPage
     * @param pageSize
     * @param rname
     * @return
     */
    @Override
    public PageBean<Route> pageQuery(int cid, int currentPage, int pageSize, String rname) {
        //封装pageBean
        PageBean<Route> routePageBean = new PageBean<>();
        //设置当前页码
        routePageBean.setCurrentPage(currentPage);
        //设置每页显示条数
        routePageBean.setPageSize(pageSize);

        //设置总记录数
        int totalCount = routeDao.findTotalCount(cid,rname);
        routePageBean.setTotalCount(totalCount);
        //设置当前页面显示的数据集合
        int start = (currentPage - 1) * pageSize;//开始的记录数
        List<Route> byPage = routeDao.findByPage(cid, start, pageSize,rname);
        routePageBean.setList(byPage);

        //设置总页数 = 总记录数/每页显示条数
        int totalPage = totalCount%pageSize == 0? totalCount/pageSize :(totalCount/pageSize) + 1;
        routePageBean.setTotalPage(totalPage);

        return routePageBean;
    }

    /**
     * 根据id查询
     * @param rid
     * @return
     */
    @Override
    public Route findOne(String rid) {
        //1.根据id去route表中查询route对象
        Route route = routeDao.findOne(Integer.parseInt(rid));
        //2.根据route的id查询图片集合信息
        List<RouteImg> routeImgList = routeImgDao.findByRid(route.getRid());
        //2.2将集合设置到route对象
        route.setRouteImgList(routeImgList);
        //3.根据route的sid查询商家对象
        Seller seller = sellerDao.findBySid(route.getSid());
        route.setSeller(seller);
        //4.查询收藏次数
        int count = favoriteDao.countFavoriteByRid(rid);
        route.setCount(count);
        return route;
    }
}
