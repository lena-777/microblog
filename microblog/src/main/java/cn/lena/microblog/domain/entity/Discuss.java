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
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author lena
 * @since 2020-07-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Discuss extends Model<Discuss> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "discussId", type = IdType.AUTO)
    private Integer discussId;

    @TableField("userId")
    private Integer userId;

    @TableField("passageId")
    private Integer passageId;

    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date time;

    private String context;

    private Integer liked;

    @TableField("parentId")
    private Integer parentId;

    @TableField("childrenId")
    private Integer childrenId;

    @TableField(exist = false)  //表明数据库不存在该字段
    private List<Discuss> discuss;    //存放子评论

    @TableLogic
    private Integer deleted;


    @Override
    protected Serializable pkVal() {
        return this.discussId;
    }

}
