package com.example.tienda.dto.request;

import java.util.ArrayList;
import java.util.List;

public class AddUserRolesRequest {

    private List<Long> roleIds = new ArrayList<>();

    public List<Long> getRoleIds() { return roleIds; }
    public void setRoleIds(List<Long> roleIds) { this.roleIds = roleIds; }
}