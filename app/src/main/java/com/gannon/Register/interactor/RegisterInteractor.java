package com.gannon.Register.interactor;

import android.content.Context;
import android.util.Log;

import com.gannon.R;
import com.gannon.Register.interactor.model.RegisterActivityReq;
import com.gannon.Register.interactor.model.RegisterActivityRes;
import com.gannon.Register.presenter.RegisterPresenter;
import com.gannon.utils.RestAPI;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterInteractor implements RegisterInteractorInt {
    private Context context;
    private RestAPI restAPI;
    /* access modifiers changed from: private */
    public RegisterActivityRes userRegisterActivityRes;
    /* access modifiers changed from: private */
    public RegisterPresenter userRegisterPresenter;

    public RegisterInteractor(Context context2, RestAPI restAPI2) {
        this.context = context2;
        this.restAPI = restAPI2;
    }

    public void callRegisterWebService(RegisterPresenter registerPresenter, RegisterActivityReq registerActivityReq) {
        this.userRegisterPresenter = registerPresenter;
        callRegisterService(registerActivityReq);
    }

    private void callRegisterService(RegisterActivityReq registerActivityReq) {
        this.userRegisterPresenter.context.showProgress();
        try {
            this.restAPI.getRegisterActivityResCall(registerActivityReq).enqueue(new Callback<RegisterActivityRes>() {
                public void onResponse(Call<RegisterActivityRes> call, Response<RegisterActivityRes> response) {
                    userRegisterPresenter.context.hideProgress();
                    if (response.isSuccessful()) {
                        userRegisterActivityRes = (RegisterActivityRes) response.body();
                        if (userRegisterActivityRes.getStatusCode() == 200) {
                            userRegisterPresenter.responseSuccess(userRegisterActivityRes);
                        }else {
                            userRegisterPresenter.responseFailed(userRegisterActivityRes.getError());
                        }
                    }
                }

                public void onFailure(Call<RegisterActivityRes> call, Throwable th) {
                    userRegisterPresenter.context.hideProgress();
                    userRegisterPresenter.responseFailed(userRegisterPresenter.context.getResources().getString(R.string.server_not_responding));
                }
            });
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            sb.append(" Exception Msg ==> ");
            sb.append(e.getMessage());
            Log.v("Login Exception", sb.toString());
            this.userRegisterPresenter.context.hideProgress();
            RegisterPresenter registerPresenter = this.userRegisterPresenter;
            registerPresenter.responseFailed(registerPresenter.context.getResources().getString(R.string.server_not_responding));
        }
    }
}
