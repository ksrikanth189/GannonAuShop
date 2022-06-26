package com.gannon.forgetPassword.view;


public interface ForgetPasswordView {

    void serviceSuccess(String success);

    void serviceFailed(String str);

    void showProgress();

    void hideProgress();

    void showSuccessToast(String str);

    void showValidationToast(String str);

    void navigateToLoginScreen();

    void closeApp(String str);
}
