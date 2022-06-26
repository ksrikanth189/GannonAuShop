package


        com.gannon.profileUpdate.presenter;


import com.gannon.profileUpdate.activity.ProfileUpdateActivity;
import com.gannon.profileUpdate.interactor.ProfileUpdateInteractorInt;
import com.gannon.profileUpdate.interactor.model.ProfileUpdateReq;
import com.gannon.profileUpdate.interactor.model.ProfileUpdateRes;

public class ProfileUpdatePresenter {

    public ProfileUpdateActivity context;

    private ProfileUpdateInteractorInt profileUpdateInteractorInt;

    public ProfileUpdatePresenter(ProfileUpdateActivity context, ProfileUpdateInteractorInt ProfileUpdateInteractorInt) {
        this.profileUpdateInteractorInt = ProfileUpdateInteractorInt;
        this.context = context;
    }

    public void changePasssBtnClicked(ProfileUpdateReq profileUpdateReq) {

            profileUpdateInteractorInt.callForgerPassWebService(this, profileUpdateReq);


    }

    public void responseSuccess(ProfileUpdateRes ProfileUpdateRes) {
        if (ProfileUpdateRes.getStatusCode() != null && ProfileUpdateRes.getStatusCode() == 200
                && ProfileUpdateRes.getStatus().equalsIgnoreCase("Success")) {
                context.serviceSuccess(ProfileUpdateRes.getMessage());
        } else {
            context.serviceFailed(ProfileUpdateRes.getMessage());
        }
    }

    public void responseFailed(String str) {
        context.showValidationToast(str);
    }



}
