package me.liuhui.mall.manager.service.dto.user;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;


@EqualsAndHashCode(callSuper = true)
@Data
public class ModifyUserDTO extends CreateUserDTO {


    @NotNull
    private Long id;


}
