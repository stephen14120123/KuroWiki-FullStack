package com.wiki.dao;

import com.wiki.entity.EchoInfo;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface EchoMapper {
    // 获取所有声骸
    List<EchoInfo> getAllEchoes();

    // 根据 ID 获取单个声骸
    EchoInfo getEchoById(Integer id);

    // 新增声骸
    int insertEcho(EchoInfo echo);

    // 更新声骸
    int updateEcho(EchoInfo echo);

    // 删除声骸
    int deleteEcho(Integer id);
}