package com.gannon.Register.presenter;

import com.gannon.Register.activity.RegisterActivity;
import com.gannon.Register.interactor.RegisterInteractorInt;
import com.gannon.Register.interactor.model.RegisterActivityReq;
import com.gannon.Register.interactor.model.RegisterActivityRes;

public class RegisterPresenter {
    public RegisterActivity context;
    private RegisterInteractorInt registerInteractorInt;

    public RegisterPresenter(RegisterActivity registerActivity, RegisterInteractorInt registerInteractorInt2) {
        this.registerInteractorInt = registerInteractorInt2;
        this.context = registerActivity;
    }

    public void signInBtnClicked(RegisterActivityReq registerActivityReq) {
        this.registerInteractorInt.callRegisterWebService(this, registerActivityReq);
    }

    public void responseSuccess(RegisterActivityRes registerActivityRes) {
        if (registerActivityRes.getStatusCode() == 200) {
            context.serviceSuccess(registerActivityRes);
        } else {
            context.serviceFailed(registerActivityRes.getMessage());
        }
    }

    public void responseFailed(String str) {
        this.context.showValidationToast(str);
    }

    public void saveRegisterLogin(RegisterActivityRes registerActivityRes) {
        if (registerActivityRes.getStatusCode() == 200) {
            context.navigateToHomeScreen();
        }
    }
}
