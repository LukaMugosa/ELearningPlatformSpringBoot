package me.amplitudo.elearning.service.dto;

import java.io.Serializable;

public class CourseOrientationDTO implements Serializable {

    private Long courseId;
    private Long orientationId;

    public CourseOrientationDTO(Long courseId, Long orientationId) {
        this.courseId = courseId;
        this.orientationId = orientationId;
    }

    public CourseOrientationDTO() {
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Long getOrientationId() {
        return orientationId;
    }

    public void setOrientationId(Long orientationId) {
        this.orientationId = orientationId;
    }


}
