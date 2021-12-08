package by.bsuir.common.app.entity;

import lombok.*;

@EqualsAndHashCode(callSuper = false)
@Data
@Builder
public class StudentFile {

    private Integer id;
    private String name;
    private Integer age;
    private String summary;
}
