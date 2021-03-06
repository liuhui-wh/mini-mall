package me.liuhui.mall.manager.service.dto.user;

import me.liuhui.mall.common.base.dto.PageDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Data
public class ListUserDTO extends PageDTO {



    private String username;

    private String email;

    private Integer type;

    private Integer status;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date minLastLoginTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date maxLastLoginTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date minRegisterTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date maxRegisterTime;


}
