
package me.liuhui.mall.manager.service.impl;

import me.liuhui.mall.common.base.vo.ResultVO;
import me.liuhui.mall.manager.runtime.AdminSessionHolder;
import me.liuhui.mall.manager.service.CategoryService;
import me.liuhui.mall.manager.service.dto.category.CreateCategoryDTO;
import me.liuhui.mall.manager.service.dto.category.ModifyCategoryDTO;
import me.liuhui.mall.manager.service.mapstruct.CategoryConverter;
import me.liuhui.mall.manager.service.vo.category.CategoryVO;
import me.liuhui.mall.repository.dao.CategoryDao;
import me.liuhui.mall.repository.model.Category;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

import static me.liuhui.mall.common.utils.Constants.CATEGORY_TOP_PID;

/**
 * Created on 2020/10/14 20:12
 * <p>
 * Description: [TODO]
 * <p>
 *
 * @author [清远]
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Resource
    private CategoryConverter categoryConverter;
    @Resource
    private CategoryDao categoryDao;


    @Override
    public ResultVO<List<CategoryVO>> subCategory(Long pid) {
        Map<String, Object> cond = new HashMap<>(2);
        cond.put("pid", pid == null ? CATEGORY_TOP_PID : pid);
        cond.put("orderBy", "weight desc");
        List<Category> categories = categoryDao.selectList(cond);
        return ResultVO.buildSuccessResult(categoryConverter.toVo(categories));
    }

    @Override
    public ResultVO<Boolean> create(CreateCategoryDTO dto) {
        Category entity = categoryConverter.createDtoToEntity(dto);
        entity.setLeaf(true);
        entity.setCreateTime(new Date());
        entity.setCreateAdmin(AdminSessionHolder.getCurrentAdmin().getUsername());
        categoryDao.insert(entity);
        if (!CATEGORY_TOP_PID.equals(entity.getPid())) {
            Category parent = categoryDao.selectByPk(entity.getPid());
            parent.setLeaf(false);
            categoryDao.update(parent, "leaf");
        }
        return ResultVO.buildSuccessResult();
    }

    @Override
    public ResultVO<Boolean> modify(ModifyCategoryDTO dto) {
        Category category = categoryDao.selectByPk(dto.getId());
        if (category == null) {
            return ResultVO.buildFailResult("分类不存在");
        }
        Long oldPid = category.getPid();
        Long newPid = dto.getPid();
        Category entity = categoryConverter.modifyDtoToEntity(dto);
        categoryDao.update(entity);
        if (!Objects.equals(oldPid, newPid)) {
            if (!CATEGORY_TOP_PID.equals(newPid)) {
                Category newParent = categoryDao.selectByPk(newPid);
                if (newParent.getLeaf()) {
                    newParent.setLeaf(false);
                    categoryDao.update(newParent, "leaf");
                }
            }
            if (!CATEGORY_TOP_PID.equals(oldPid)) {
                Category oldParent = categoryDao.selectByPk(oldPid);
                Map<String, Object> cond = new HashMap<>(2);
                cond.put("pid", oldPid);
                long childNum = categoryDao.count(cond);
                if (childNum == 0) {
                    oldParent.setLeaf(true);
                    categoryDao.update(oldParent, "leaf");
                }
            }
        }
        return ResultVO.buildSuccessResult();
    }

    @Override
    public ResultVO<Boolean> delete(Set<Long> ids) {
        for (Long id : ids) {
            Category category = categoryDao.selectByPk(id);
            if(category == null){
                return ResultVO.buildFailResult("分类不存在");
            }
            categoryDao.delete(id);
            if(!CATEGORY_TOP_PID.equals(category.getPid())) {
                Map<String, Object> cond = new HashMap<>(2);
                cond.put("pid", category.getPid());
                long childNum = categoryDao.count(cond);
                if (childNum == 0) {
                    Category parent = new Category();
                    parent.setId(category.getPid());
                    parent.setLeaf(true);
                    categoryDao.update(parent, "leaf");
                }
            }
        }
        return ResultVO.buildSuccessResult();
    }

}
