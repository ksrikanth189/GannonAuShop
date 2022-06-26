package com.gannon.Login.interactor;


import com.gannon.Login.interactor.model.LoginActivityReq;
import com.gannon.Login.presenter.LoginPresenter;

public interface LoginInteractorInt {

    public void callLoginWebService(LoginPresenter userLoginPresenter, LoginActivityReq userLoginActivityReq);
}
