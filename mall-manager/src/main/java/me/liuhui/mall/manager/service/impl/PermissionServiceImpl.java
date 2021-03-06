package me.liuhui.mall.manager.service.impl;

import me.liuhui.mall.common.base.vo.ResultVO;
import me.liuhui.mall.manager.runtime.AdminSessionHolder;
import me.liuhui.mall.manager.service.PermissionService;
import me.liuhui.mall.manager.service.dto.permission.CreatePermissionDTO;
import me.liuhui.mall.manager.service.dto.permission.ModifyPermissionDTO;
import me.liuhui.mall.manager.service.mapstruct.PermissionConverter;
import me.liuhui.mall.manager.service.vo.admin.PermissionVO;
import me.liuhui.mall.manager.service.vo.auth.AuthVO;
import me.liuhui.mall.repository.dao.PermissionDao;
import me.liuhui.mall.repository.model.Permission;
import me.liuhui.mall.repository.model.enums.PermissionType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created on 2020/10/14 20:12
 * <p>
 * Description: [TODO]
 * <p>
 * Company: []
 *
 * @author [清远]
 */
@Service
public class PermissionServiceImpl implements PermissionService {
    @Resource
    private PermissionConverter permissionConverter;
    @Resource
    private PermissionDao permissionDao;
    @Override
    public List<Permission> currentPermissions() {
        AuthVO currentAdmin = AdminSessionHolder.getCurrentAdmin();
        return permissionDao.selectByAdminId(currentAdmin.getAdminId());
    }

    @Override
    public ResultVO<List<PermissionVO>> menuTree() {
        List<Permission> permissions = currentPermissions().stream().filter(permission -> Objects.equals(PermissionType.MENU.getCode(), permission.getType())).collect(Collectors.toList());
        return ResultVO.buildSuccessResult(buildPermissionTree(permissions));
    }

    @Override
    public ResultVO<List<PermissionVO>> permissionTree() {
        List<Permission> permissions = permissionDao.selectList(null);
        return ResultVO.buildSuccessResult(buildPermissionTree(permissions));
    }

    @Override
    public ResultVO<List<PermissionVO>> list() {
        Map<String, Object> cond = new HashMap<>(2);
        cond.put("orderBy", "weight desc");
        List<Permission> permissions = permissionDao.selectList(cond);
        return ResultVO.buildSuccessResult(permissionConverter.toVo(permissions));
    }

    @Override
    public ResultVO<Boolean> create(CreatePermissionDTO dto) {
        Map<String,Object> cond = new HashMap<>(2);
        cond.put("code" , dto.getCode());
        long count = permissionDao.count(cond);
        if(count > 0 ){
            return ResultVO.buildFailResult("权限码已存在！");
        }
        Permission entity = permissionConverter.createDtoToEntity(dto);
        entity.setCreateTime(new Date());
        permissionDao.insert(entity);
        return ResultVO.buildSuccessResult();
    }

    @Override
    public ResultVO<Boolean> modify(ModifyPermissionDTO dto) {
        Permission permission = permissionDao.selectByPk(dto.getId());
        if(permission == null){
            return ResultVO.buildFailResult("权限不存在");
        }
        if(!StringUtils.equals(permission.getCode(),dto.getCode())){
            Map<String,Object> cond = new HashMap<>(2);
            cond.put("code" , dto.getCode());
            cond.put("notId" , dto.getId());
            long count = permissionDao.count(cond);
            if(count > 0 ){
                return ResultVO.buildFailResult("权限码已存在！");
            }
        }

        Permission entity = permissionConverter.modifyDtoToEntity(dto);
        permissionDao.update(entity);
        return ResultVO.buildSuccessResult();
    }

    @Override
    public ResultVO<Boolean> delete(Set<Long> ids) {
        for (Long id : ids) {
            permissionDao.delete(id);
        }
        return ResultVO.buildSuccessResult();
    }

    private List<PermissionVO> buildPermissionTree(List<Permission> permissions) {
        List<Permission> tops = permissions.stream().filter(p -> Objects.isNull(p.getPid()) || p.getPid() == -1).collect(Collectors.toList());
        List<PermissionVO> permissionVos = permissionConverter.toVo(tops);
        Collections.sort(permissionVos);
        fillChild(permissionVos, permissions);
        return permissionVos;
    }

    private void fillChild(List<PermissionVO> permissionVos, List<Permission> permissions) {
        permissionVos.forEach(permissionVO -> {
            List<Permission> child = permissions.stream().filter(permission -> Objects.equals(permission.getPid(), permissionVO.getId())).collect(Collectors.toList());
            if (child.size() > 0) {
                List<PermissionVO> childen = permissionConverter.toVo(child);
                Collections.sort(childen);
                permissionVO.setChildren(childen);
                fillChild(permissionVO.getChildren(), permissions);
            }
        });
    }
}
