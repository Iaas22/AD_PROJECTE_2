package com.example.tienda.mapper;

import com.example.tienda.dto.RoleDTO;
import com.example.tienda.model.Role;

public class RoleMapper {

    private RoleMapper() {
    }

    public static RoleDTO toDTO(Role role) {
        RoleDTO dto = new RoleDTO();
        dto.setId(role.getId());
        dto.setName(role.getName().name());
        dto.setDescription(role.getDescr());
        return dto;
    }
}
