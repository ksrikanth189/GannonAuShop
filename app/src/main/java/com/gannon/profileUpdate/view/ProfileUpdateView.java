package


        com.gannon.profileUpdate.view;


public interface ProfileUpdateView {

    void serviceSuccess(String success);

    void serviceFailed(String str);

    void showProgress();

    void hideProgress();

    void showSuccessToast(String str);

    void showValidationToast(String str);

    void navigateToHomeScreen();

    void closeApp(String str);
}
