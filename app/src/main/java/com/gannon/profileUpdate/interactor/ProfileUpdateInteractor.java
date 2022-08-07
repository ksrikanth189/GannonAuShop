package com.gannon.profileUpdate.interactor;

import android.content.Context;
import android.util.Log;

import com.gannon.R;
import com.gannon.profileUpdate.interactor.model.ProfileUpdateReq;
import com.gannon.profileUpdate.interactor.model.ProfileUpdateRes;
import com.gannon.profileUpdate.presenter.ProfileUpdatePresenter;
import com.gannon.utils.RestAPI;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileUpdateInteractor implements ProfileUpdateInteractorInt {

    private Context context;
    private RestAPI restAPI;
    private ProfileUpdatePresenter profileUpdatePresenter;
    private ProfileUpdateRes profileUpdateRes;

    public ProfileUpdateInteractor(Context context, RestAPI restAPI) {
        this.context = context;
        this.restAPI = restAPI;
    }

    @Override
    public void callForgerPassWebService(ProfileUpdatePresenter profileUpdatePresenter, ProfileUpdateReq profileUpdateReq) {

        this.profileUpdatePresenter = profileUpdatePresenter;
        callLoginService(profileUpdateReq);
    }

    private void callLoginService(final ProfileUpdateReq profileUpdateReq) {

        profileUpdatePresenter.context.showProgress();
        try {
            final Call<ProfileUpdateRes> loginActivityResCall = restAPI.getProfileUpdateResCall(profileUpdateReq);
            loginActivityResCall.enqueue(new Callback<ProfileUpdateRes>() {
                @Override
                public void onResponse(Call<ProfileUpdateRes> call, Response<ProfileUpdateRes> response) {
                    profileUpdatePresenter.context.hideProgress();
                    if (response.isSuccessful()) {
                        profileUpdateRes = response.body();
                        if (profileUpdateRes.getStatusCode() == 200) {
                            profileUpdatePresenter.responseSuccess(profileUpdateRes);
                        }else {
                            profileUpdatePresenter.responseFailed(profileUpdateRes.getError());

                        }
                    }
                }

                @Override
                public void onFailure(Call<ProfileUpdateRes> call, Throwable t) {
                    profileUpdatePresenter.context.hideProgress();
                    profileUpdatePresenter.responseFailed(profileUpdatePresenter.context.getResources().getString(R.string.server_not_responding));
                }
            });
        } catch (Exception e) {
            Log.v("Login Exception", " Exception Msg ==> " + e.getMessage());
            profileUpdatePresenter.context.hideProgress();
            profileUpdatePresenter.responseFailed(profileUpdatePresenter.context.getResources().getString(R.string.server_not_responding));
        }
    }

}
