package me.amplitudo.elearning.web.rest.errors;

public enum ExceptionErrors {

    FACULTY_EXISTS("faculty-already-exists","Faculty with given name already exists."),
    COURSE_EXISTS("course-already-exists","Course with given name already exists."),
    COURSE_ORIENTATION_EXISTS("course-orientation-already-exists","Course relationship with given orientation already exists."),
    BUILDING_EXISTS("building-already-exists","Building with given name already exists.");

    private final String errorCode;
    private final String errorDescription;

    ExceptionErrors(String errorCode, String errorDescription) {
        this.errorCode = errorCode;
        this.errorDescription = errorDescription;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorDescription() {
        return errorDescription;
    }


}
