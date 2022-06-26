package


        com.gannon.forgetPassword.interactor;


import com.gannon.forgetPassword.interactor.model.ForgetPasswordReq;
import com.gannon.forgetPassword.presenter.ForgetPasswordPresenter;

public interface ForgetPasswordInteractorInt {

    public void callForgerPassWebService(ForgetPasswordPresenter
                                                 forgetPasswordPresenter, ForgetPasswordReq
                                                 forgetPasswordReq);
}
