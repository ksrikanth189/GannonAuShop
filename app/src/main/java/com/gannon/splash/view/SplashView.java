package


        com.gannon.splash.view;


import com.gannon.splash.interactor.model.CheckForUpdateRes;

public interface SplashView {

    void serviceFailed(String str);

    void showProgress();

    void hideProgress();

    void showSuccessToast(String str);

    void showValidationToast(String str);

    void navigateToHomeScreen();

    void closeApp(String str);

    void serviceSuccess(String str);
    void serviceUpdateSuccess(CheckForUpdateRes  checkForUpdateRes);

}
