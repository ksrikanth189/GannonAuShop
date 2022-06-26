package


        com.gannon.Login.presenter;


import com.gannon.Login.activity.LoginActivity;
import com.gannon.Login.interactor.LoginInteractorInt;
import com.gannon.Login.interactor.model.LoginActivityReq;
import com.gannon.Login.interactor.model.LoginActivityRes;
import com.gannon.R;
import com.gannon.sharedpref.SharedPrefHelper;

public class LoginPresenter {

    public LoginActivity context;

    private LoginInteractorInt loginInteractorInt;

    public LoginPresenter(LoginActivity context, LoginInteractorInt loginInteractorInt) {
        this.loginInteractorInt = loginInteractorInt;
        this.context = context;
    }

    public void signInBtnClicked(String userId, String password) {
        if (userId.length() == 0) {
            context.showValidationToast(context.getResourceStr(context, R.string.pls_enter_register_mobile));

        } else if (password.length() == 0) {
            context.showValidationToast(context.getResourceStr(context, R.string.pls_enter_password));
        }  else {
            LoginActivityReq loginActivityReq = new LoginActivityReq();
            loginActivityReq.setUserName(userId);
            loginActivityReq.setPassword(password);
            loginInteractorInt.callLoginWebService(this, loginActivityReq);





        }
    }



    public void responseSuccess(LoginActivityRes userLoginActivityRes) {
        if (userLoginActivityRes.getStatusCode() != 200) {
            this.context.serviceFailed(userLoginActivityRes.getMessage().getMessage());
        } else if (userLoginActivityRes.getMessage() != null) {
            this.context.serviceSuccess(userLoginActivityRes);
            SharedPrefHelper.saveLoginDts(context, userLoginActivityRes);

        }
    }

    public void responseFailed(String str) {
        context.showValidationToast(str);
    }

    public void saveLogin(LoginActivityRes userLoginActivityRes) {
        SharedPrefHelper.saveLoginDts(context, userLoginActivityRes);

        if (userLoginActivityRes != null) {
            context.navigateToHomeScreen();

        }
    }

}
