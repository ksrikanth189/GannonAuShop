package com.gannon.forgetPassword.presenter;


import com.gannon.R;
import com.gannon.forgetPassword.activity.ForgetPasswordActivity;
import com.gannon.forgetPassword.interactor.ForgetPasswordInteractorInt;
import com.gannon.forgetPassword.interactor.model.ForgetPasswordReq;
import com.gannon.forgetPassword.interactor.model.ForgetPasswordRes;

public class ForgetPasswordPresenter {

    public ForgetPasswordActivity context;

    private ForgetPasswordInteractorInt adminForgetPasswordInteractorInt;

    public ForgetPasswordPresenter(ForgetPasswordActivity context, ForgetPasswordInteractorInt adminForgetPasswordInteractorInt) {
        this.adminForgetPasswordInteractorInt = adminForgetPasswordInteractorInt;
        this.context = context;
    }

    public void forgetBtnClicked(String mobile) {
        if (mobile.length() == 0) {
            context.showValidationToast(context.getResourceStr(context, R.string.pls_enter_mobile));
        } else {

            ForgetPasswordReq adminForgetPasswordReq = new ForgetPasswordReq();
            adminForgetPasswordReq.setEmail(mobile);
            adminForgetPasswordInteractorInt.callForgerPassWebService(this, adminForgetPasswordReq);

        }
    }

    public void responseSuccess(ForgetPasswordRes adminForgetPasswordRes) {
        if (adminForgetPasswordRes.getStatusCode() != null && adminForgetPasswordRes.getStatusCode() == 200) {
            context.serviceSuccess(adminForgetPasswordRes.getMessage());
        } else {
            context.serviceFailed(adminForgetPasswordRes.getMessage());
        }
    }

    public void responseFailed(String str) {
        context.showValidationToast(str);
    }


    public void saveLogin(ForgetPasswordRes adminForgetPasswordRes) {

        context.navigateToLoginScreen();

    }

}
