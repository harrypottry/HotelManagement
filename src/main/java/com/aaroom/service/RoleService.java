package com.aaroom.service;

import com.aaroom.beans.Role;
import com.aaroom.persistence.RoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by sosoda on 2018/11/1.
 */
@Service
public class RoleService {

    @Autowired
    private RoleMapper roleMapper;

    public Role getRole(Integer id) {
        return roleMapper.selectByPrimaryKey(id);
    }
}
