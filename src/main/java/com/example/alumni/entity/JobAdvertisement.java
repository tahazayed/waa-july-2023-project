package com.example.alumni.entity;

import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;

@Entity
@Data
@Table(name="jobadvertisement")
@NoArgsConstructor @AllArgsConstructor
@SQLDelete(sql = "UPDATE JobAdvertisement SET deleted = true WHERE id=?")
@FilterDef(name = "deletedJobAdvertisementFilter", parameters = @ParamDef(name = "isDeleted", type = Boolean.class))
@Filter(name = "deletedJobAdvertisementFilter", condition = "deleted = :isDeleted")
public class JobAdvertisement {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String positionTitle;
    @Lob
    private String Description;
    private Double minSalary;
    private Double maxSalary;
    @Lob
    private String requiredSkills;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Tag> tags;

    @ManyToOne
    @JoinColumn
    private User user;

    private boolean deleted = Boolean.FALSE;
}