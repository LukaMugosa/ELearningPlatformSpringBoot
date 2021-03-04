package me.amplitudo.elearning.web.rest.errors;

public enum ExceptionErrors {

    FACULTY_EXISTS("faculty-already-exists","Faculty with given name already exists.");

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