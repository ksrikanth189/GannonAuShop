package com.gannon.Register.view;


import com.gannon.Register.interactor.model.RegisterActivityRes;

public interface RegisterView {
    void closeApp(String str);

    void hideProgress();

    void navigateToHomeScreen();

    void serviceFailed(String str);

    void serviceSuccess(RegisterActivityRes registerActivityRes);

    void showProgress();

    void showSuccessToast(String str);

    void showValidationToast(String str);
}
