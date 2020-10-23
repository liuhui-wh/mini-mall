package me.liuhui.mall.manager.service;

import me.liuhui.mall.common.base.vo.ResultVO;
import me.liuhui.mall.manager.service.dto.product.CreateProductDTO;
import me.liuhui.mall.manager.service.dto.product.ListProductDTO;
import me.liuhui.mall.manager.service.dto.product.ModifyProductDTO;
import me.liuhui.mall.manager.service.vo.product.ListProductVO;

import java.util.Set;


public interface ProductService {


	ResultVO<Boolean> create(CreateProductDTO dto);

	ResultVO<Boolean> modify(ModifyProductDTO dto);

	ResultVO<Boolean> delete(Set<Long> ids);

	ResultVO<ListProductVO> list(ListProductDTO dto);


}