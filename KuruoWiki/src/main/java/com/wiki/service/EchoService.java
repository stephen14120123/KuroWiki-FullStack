package com.wiki.service;

import com.wiki.dao.EchoMapper;
import com.wiki.entity.EchoInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EchoService {

    @Autowired
    private EchoMapper echoMapper;

    public List<EchoInfo> getAllEchoes() {
        return echoMapper.getAllEchoes();
    }

    public EchoInfo getEchoById(Integer id) {
        return echoMapper.getEchoById(id);
    }

    public void saveEcho(EchoInfo echo) {
        int rows = echoMapper.insertEcho(echo);
        if (rows == 0) {
            throw new IllegalArgumentException("声骸添加失败");
        }
    }

    public void updateEcho(EchoInfo echo) {
        int rows = echoMapper.updateEcho(echo);
        if (rows == 0) {
            throw new IllegalArgumentException("声骸修改失败，声骸可能不存在");
        }
    }

    public void deleteEcho(Integer id) {
        int rows = echoMapper.deleteEcho(id);
        if (rows == 0) {
            throw new IllegalArgumentException("声骸删除失败，声骸可能不存在");
        }
    }
}
