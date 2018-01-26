package com.key.dwsurvey.dao;

import com.key.common.dao.BaseDao;
import com.key.dwsurvey.entity.SurveyDirectory;

import java.util.List;

public interface SurveyDirectoryDao extends BaseDao<SurveyDirectory, String>{

    Long userSurveyCount(String id, String beginDate, String endDate);

    public List<Object[]> groupUser(String beginDate,String endDate) ;
}
