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
public class Messages extends Model<Messages> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "message", type = IdType.AUTO)
    private Integer message;

    @TableField("sentUserId")
    private Integer sentUserId;

    @TableField("receiveUserId")
    private Integer receiveUserId;

    //1点赞 2评论 3关注
    private Integer operation;

    @TableLogic
    private Integer deleted;

    @TableField("passageId")
    private Integer passageId;

    @TableField("discussContent")
    private String discussContent;

    @TableField("operationTime")
    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date operationTime;


    @Override
    protected Serializable pkVal() {
        return this.message;
    }

}
