package com.gannon.Login.interactor;

import android.content.Context;
import android.util.Log;

import com.gannon.Login.interactor.model.LoginActivityReq;
import com.gannon.Login.interactor.model.LoginActivityRes;
import com.gannon.Login.presenter.LoginPresenter;
import com.gannon.R;
import com.gannon.utils.RestAPI;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginInteractor implements LoginInteractorInt {

    private Context context;
    private RestAPI restAPI;
    private LoginPresenter userLoginPresenter;
    private LoginActivityRes userLoginActivityRes;

    public LoginInteractor(Context context, RestAPI restAPI) {
        this.context = context;
        this.restAPI = restAPI;
    }

    @Override
    public void callLoginWebService(LoginPresenter userLoginPresenter, LoginActivityReq userLoginActivityReq) {

        this.userLoginPresenter = userLoginPresenter;
        callLoginService(userLoginActivityReq);
    }

    private void callLoginService(final LoginActivityReq userLoginActivityReq) {

        userLoginPresenter.context.showProgress();
        try {
            final Call<LoginActivityRes> loginActivityResCall = restAPI.getLoginResCall(userLoginActivityReq);
            loginActivityResCall.enqueue(new Callback<LoginActivityRes>() {
                @Override
                public void onResponse(Call<LoginActivityRes> call, Response<LoginActivityRes> response) {
                    userLoginPresenter.context.hideProgress();
                    if (response.isSuccessful()) {
                        userLoginActivityRes = response.body();

                        if (userLoginActivityRes.getStatusCode() == 200) {
                            userLoginPresenter.responseSuccess(userLoginActivityRes);
                        } else {
                            userLoginPresenter.responseFailed(userLoginActivityRes.getStatus());
                        }
                    }
                }

                @Override
                public void onFailure(Call<LoginActivityRes> call, Throwable t) {
                    userLoginPresenter.context.hideProgress();
                    userLoginPresenter.responseFailed(userLoginPresenter.context.getResources().getString(R.string.server_not_responding));
                }
            });
        } catch (Exception e) {
            Log.v("Login Exception", " Exception Msg ==> " + e.getMessage());
            userLoginPresenter.context.hideProgress();
            userLoginPresenter.responseFailed(userLoginPresenter.context.getResources().getString(R.string.server_not_responding));
        }
    }


}
