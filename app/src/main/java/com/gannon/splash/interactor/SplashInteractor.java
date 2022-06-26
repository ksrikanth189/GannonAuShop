package


        com.gannon.splash.interactor;

import android.content.Context;
import android.util.Log;

import com.gannon.R;
import com.gannon.splash.interactor.model.CheckForUpdateRes;
import com.gannon.splash.presenter.SplashPresenter;
import com.gannon.utils.RestAPI;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashInteractor implements SplashInteractorInt {

    private Context context;
    private RestAPI restAPI;
    private SplashPresenter splashPresenter;
    private CheckForUpdateRes checkForUpdateRes;


    public SplashInteractor(Context context, RestAPI restAPI) {
        this.context = context;
        this.restAPI = restAPI;
    }


    @Override
    public void checkAppUpdate(SplashPresenter splashPresenter) {
        this.splashPresenter = splashPresenter;
        checkApplicationUpdate();
    }


    private void checkApplicationUpdate() {

        splashPresenter.context.showProgress();
        try {
            final Call<CheckForUpdateRes> checkForUpdateResCall = restAPI.getCheckForUpdateResCall();
            checkForUpdateResCall.enqueue(new Callback<CheckForUpdateRes>() {
                @Override
                public void onResponse(Call<CheckForUpdateRes> call, Response<CheckForUpdateRes> response) {
                    splashPresenter.context.hideProgress();
                    if (response.isSuccessful()) {
                        checkForUpdateRes = response.body();
                        splashPresenter.responseSuccess(checkForUpdateRes);
                    } else {
//                        splashPresenter.responseFailed(checkForUpdateRes.getMessage().toString() != null ? checkForUpdateRes.getMessage().toString() : context.getResources().getString(R.string.server_not_responding));
                        splashPresenter.responseFailed(splashPresenter.context.getResources().getString(R.string.server_not_responding));

                    }
                }

                @Override
                public void onFailure(Call<CheckForUpdateRes> call, Throwable t) {
                    splashPresenter.context.hideProgress();
                    splashPresenter.responseFailed(splashPresenter.context.getResources().getString(R.string.server_not_responding));
                }
            });
        } catch (Exception e) {
            Log.v("Login Exception", " Exception Msg ==> " + e.getMessage());
            splashPresenter.context.hideProgress();
            splashPresenter.responseFailed(splashPresenter.context.getResources().getString(R.string.server_not_responding));
        }
    }


}
