package cn.lena.microblog.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author lena
 * @since 2020-07-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Users extends Model<Users> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "userId", type = IdType.AUTO)
    private Integer userId;

    private String username;

    private String password;

    private String sex;

    private String email;

    @TableField("joinDate")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date joinDate;

    @TableField("lastTime")
    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date lastTime;

    private Integer status;

    private String avater;

    private Integer catagory;

    private String collect;

    private Integer fans;

    private Integer attention;

    //strategy= FieldStrategy.IGNORED 设置该属性太危险了，不采用
    @TableField(value = "fansId")
    private String fansId;

    @TableField(value = "attentionId")
    private String attentionId;

    @TableLogic
    private Integer deleted;


    @Override
    protected Serializable pkVal() {
        return this.userId;
    }

}
