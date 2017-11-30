package com.randioo.majiang_collections_server.dao;

import org.apache.ibatis.annotations.Param;

import com.randioo.majiang_collections_server.entity.bo.OnlineTimeBO;
import com.randioo.randioo_server_base.annotation.MyBatisGameDaoAnnotation;
import com.randioo.randioo_server_base.db.BaseDAO;

@MyBatisGameDaoAnnotation
public interface OnlineTimeDao extends BaseDAO<OnlineTimeBO> {
    OnlineTimeBO getByRoleId(@Param("roleId") int roleId);
}
