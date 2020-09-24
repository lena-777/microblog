package cn.lena.microblog.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
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
public class Passage extends Model<Passage>{

    private static final long serialVersionUID = 1L;

    @TableId(value = "passageId", type = IdType.AUTO)
    private Integer passageId;

    @TableField("userId")
    private Integer userId;

    @TableField("createDate")
    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date createDate;

    private String title;

    private String described;

    private String content;

    private Integer liked;

 //   private String discuss;

    private Integer visited;

    @TableField("updateTime")
    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date updateTime;

    @TableField("isPrivate")
    private Integer isPrivate;

    @TableField("isPublish")
    private Integer isPublish;

    @TableLogic
    private Integer deleted;

    @TableField("isOriginal")
    private Integer isOriginal;

    @TableField("originalPassageId")
    private Integer originalPassageId;

   // private String keywords;

  //  private String catagory;

    @TableField(exist = false)  //表明不是数据库表中的属性
    private Integer[] keywords;

    @TableField(exist = false)  //表明不是数据库表中的属性
    private Integer[] catagory;

    @Override
    protected Serializable pkVal() {
        return this.passageId;
    }

}
