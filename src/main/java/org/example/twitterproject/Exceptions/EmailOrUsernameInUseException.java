package org.example.twitterproject.Exceptions;

public class EmailOrUsernameInUseException extends RuntimeException{
    private boolean emailError = false;
    private boolean usernameError = false;
    public EmailOrUsernameInUseException(){

    }
    public EmailOrUsernameInUseException(boolean emailError, boolean usernameError){
        this.emailError = emailError;
        this.usernameError = usernameError;
    }

    public boolean isEmailError() {
        return emailError;
    }

    public void setEmailError(boolean emailError) {
        this.emailError = emailError;
    }

    public boolean isUsernameError() {
        return usernameError;
    }

    public void setUsernameError(boolean usernameError) {
        this.usernameError = usernameError;
    }

    public boolean isReadyToThrow(){
        System.out.println("is throwing");
        return usernameError || emailError;
    }
}
