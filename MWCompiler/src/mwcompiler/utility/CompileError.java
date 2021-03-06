package mwcompiler.utility;

public class CompileError extends RuntimeException {
    private String message;


    public CompileError(String stage, String msg, Location location) {
        assert location != null;
        this.message = StringProcess.getErrWarning("Error", stage, msg, location);
    }
    public CompileError(String stage, String msg, Location location, String bug) {
        assert location != null;
        this.message = StringProcess.getErrWarning(bug, stage, msg, location);
    }

    public CompileError(String stage, String msg, int line, int charPosInline) {
        this.message = StringProcess.getErrWarning("Error", stage, msg, line, charPosInline);
    }

    @Override
    public String getMessage() {
        return message;
    }
}
