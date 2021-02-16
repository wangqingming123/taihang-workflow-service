package com.allintechinc.taihang.workflow.entity;

import com.allintechinc.taihang.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author sundp
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "process_definition")
@DynamicInsert
public class ProcessDefinition implements Serializable {

    private static final long serialVersionUID = 8568342313989621572L;
    /**
     * 主键ID，根据数据库策越选择生成ID方式
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long tenantId;
    private String processType;
    private String processKey;
    private String name;
    private String reviewerIds;
    private String reviewerNames;
    private String contents;
    /**
     * 表示该字段为创建人，在这个实体被insert的时候，会自动为其赋值
     */
    @CreatedBy
    private Long createdBy;

    /**
     * 表示该字段为创建时间字段，在这个实体被insert的时候，会自动为其赋值
     */
    @CreatedDate
    private LocalDateTime createdDate;

    /**
     * 表示该字段为修改人，在这个实体被update的时候，会自动为其赋值
     */
    @LastModifiedBy
    private Long lastModifiedBy;

    /**
     * 表示该字段为修改时间字段，在这个实体被update的时候，会自动为其赋值
     */
    @LastModifiedDate
    private LocalDateTime lastModifiedDate;
}
