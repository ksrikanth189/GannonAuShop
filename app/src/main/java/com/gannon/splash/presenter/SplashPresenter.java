package


        com.gannon.splash.presenter;


import com.gannon.splash.activity.SplashActivity;
import com.gannon.splash.interactor.SplashInteractorInt;
import com.gannon.splash.interactor.model.CheckForUpdateRes;

public class SplashPresenter {

    public SplashActivity context;
    private SplashInteractorInt splashInteractorInt;

    public SplashPresenter(SplashActivity context, SplashInteractorInt splashInteractorInt) {
        this.splashInteractorInt = splashInteractorInt;
        this.context = context;
    }


    public void responseFailed(String str) {
        context.showValidationToast(str);
    }


    public void checkForAppUpdate() {
        splashInteractorInt.checkAppUpdate(this);
    }


    public void responseSuccess(CheckForUpdateRes checkForUpdateRes) {
        if (checkForUpdateRes.getStatus() != 1) {
            this.context.serviceFailed(checkForUpdateRes.getMessage());
        } else {
            this.context.serviceUpdateSuccess(checkForUpdateRes);
        }
    }


}
