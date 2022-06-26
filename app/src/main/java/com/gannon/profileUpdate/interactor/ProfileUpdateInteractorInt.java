package


        com.gannon.profileUpdate.interactor;


import com.gannon.profileUpdate.interactor.model.ProfileUpdateReq;
import com.gannon.profileUpdate.presenter.ProfileUpdatePresenter;

public interface ProfileUpdateInteractorInt {

    public void callForgerPassWebService(ProfileUpdatePresenter profileUpdatePresenter, ProfileUpdateReq profileUpdateReq);
}
