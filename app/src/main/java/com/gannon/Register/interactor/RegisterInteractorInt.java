package com.gannon.Register.interactor;

import com.gannon.Register.interactor.model.RegisterActivityReq;
import com.gannon.Register.presenter.RegisterPresenter;

public interface RegisterInteractorInt {
    void callRegisterWebService(RegisterPresenter registerPresenter, RegisterActivityReq registerActivityReq);
}
