package


        com.gannon.forgetPassword.interactor;

import android.content.Context;
import android.util.Log;

import com.gannon.R;
import com.gannon.forgetPassword.interactor.model.ForgetPasswordReq;
import com.gannon.forgetPassword.interactor.model.ForgetPasswordRes;
import com.gannon.forgetPassword.presenter.ForgetPasswordPresenter;
import com.gannon.utils.RestAPI;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgetPasswordInteractor implements ForgetPasswordInteractorInt {

    private Context context;
    private RestAPI restAPI;
    private ForgetPasswordPresenter adminForgetPasswordPresenter;
    private ForgetPasswordRes forgetPasswordRes;

    public ForgetPasswordInteractor(Context context, RestAPI restAPI) {
        this.context = context;
        this.restAPI = restAPI;
    }

    @Override
    public void callForgerPassWebService(ForgetPasswordPresenter adminForgetPasswordPresenter, ForgetPasswordReq adminForgetPasswordReq) {

        this.adminForgetPasswordPresenter = adminForgetPasswordPresenter;
        callLoginService(adminForgetPasswordReq);
    }

    private void callLoginService(final ForgetPasswordReq adminForgetPasswordReq) {

        adminForgetPasswordPresenter.context.showProgress();
        try {
            final Call<ForgetPasswordRes> loginActivityResCall = restAPI.getForgetPasswordResCall(adminForgetPasswordReq);
            loginActivityResCall.enqueue(new Callback<ForgetPasswordRes>() {
                @Override
                public void onResponse(Call<ForgetPasswordRes> call, Response<ForgetPasswordRes> response) {
                    adminForgetPasswordPresenter.context.hideProgress();
                    if (response.isSuccessful()) {
                        forgetPasswordRes = response.body();
                        if (forgetPasswordRes.getStatusCode() == 200 && forgetPasswordRes.getStatus().equalsIgnoreCase("success")) {
                            adminForgetPasswordPresenter.responseSuccess(forgetPasswordRes);
                        }else {
                            adminForgetPasswordPresenter.responseFailed(forgetPasswordRes.getMessage());

                        }
                    }
                }

                @Override
                public void onFailure(Call<ForgetPasswordRes> call, Throwable t) {
                    adminForgetPasswordPresenter.context.hideProgress();
                    adminForgetPasswordPresenter.responseFailed(adminForgetPasswordPresenter.context.getResources().getString(R.string.server_not_responding));
                }
            });
        } catch (Exception e) {
            Log.v("Login Exception", " Exception Msg ==> " + e.getMessage());
            adminForgetPasswordPresenter.context.hideProgress();
            adminForgetPasswordPresenter.responseFailed(adminForgetPasswordPresenter.context.getResources().getString(R.string.server_not_responding));
        }
    }

}
