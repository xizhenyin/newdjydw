package com.key.dwsurvey.dao.impl;

import com.key.dwsurvey.dao.SurveyDirectoryDao;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import com.key.common.dao.BaseDaoImpl;
import com.key.dwsurvey.entity.SurveyDirectory;

import java.util.List;

/**
 * 问卷目录 dao
 * @author keyuan(keyuan258@gmail.com)
 *
 * https://github.com/wkeyuan/DWSurvey
 * http://dwsurvey.net
 */

//@Repository("surveyDirectoryDao")
@Repository
public class SurveyDirectoryDaoImpl extends BaseDaoImpl<SurveyDirectory, String> implements SurveyDirectoryDao {

    @Override
    public Long userSurveyCount(String userId, String beginDate, String endDate) {
        String sqlExt = "";
        if(beginDate!=null){
            sqlExt+= " and create_date >= :beginDate ";
        }
        if(endDate!=null){
            sqlExt+= " and create_date <= :endDate ";
        }
        String sql = "select count(*) from t_survey_directory where user_id = :userId "+sqlExt;
        SQLQuery sqlQuery = this.getSession().createSQLQuery(sql);
        sqlQuery.setParameter("userId",userId);
        if(beginDate!=null){
            sqlQuery.setParameter("beginDate",beginDate);
        }
        if(endDate!=null){
            sqlQuery.setParameter("endDate",endDate);
        }
        Object obj = sqlQuery.uniqueResult();
        return obj!=null?Long.parseLong(obj.toString()):0L;
    }

    @Override
    public List<Object[]> groupUser(String beginDate,String endDate) {
        String sql = "select t1.login_name,t1.name,count(*) from t_user t1,t_survey_directory t2 where t1.id = t2.user_id and t2.create_date >= :beginDate and t2.create_date < :endDate group by t2.user_id";
        SQLQuery sqlQuery = this.getSession().createSQLQuery(sql);
        sqlQuery.setParameter("beginDate",beginDate);
        sqlQuery.setParameter("endDate",endDate);
        return sqlQuery.list();
    }
}
