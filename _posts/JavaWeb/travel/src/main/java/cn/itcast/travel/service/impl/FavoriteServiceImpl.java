package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.FavoriteDao;
import cn.itcast.travel.dao.impl.FavoriteDaoImpl;
import cn.itcast.travel.domain.Favorite;
import cn.itcast.travel.service.FavoriteService;

public class FavoriteServiceImpl implements FavoriteService {
    /**
     * 判断是否收藏
     * @param rid
     * @param uid
     * @return
     */
    @Override
    public boolean isFavorite(String rid, int uid) {
        FavoriteDao favoriteDao = new FavoriteDaoImpl();
        Favorite favorite = favoriteDao.findByRidAndUid(Integer.parseInt(rid), uid);
        boolean flag = false;
        if (favorite != null && !favorite.equals("null")) {
            flag = true;
        }
        return flag;
    }

    @Override
    public void add(String rid, int uid) {
        FavoriteDao favoriteDao = new FavoriteDaoImpl();
        favoriteDao.add(Integer.parseInt(rid), uid);
    }
}
