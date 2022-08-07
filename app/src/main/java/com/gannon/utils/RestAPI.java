package com.gannon.utils;

import com.gannon.Login.interactor.model.LoginActivityReq;
import com.gannon.Login.interactor.model.LoginActivityRes;
import com.gannon.Register.interactor.model.RegisterActivityReq;
import com.gannon.Register.interactor.model.RegisterActivityRes;
import com.gannon.Register.interactor.model.VerifyOtpReq;
import com.gannon.Register.interactor.model.VerifyOtpRes;
import com.gannon.forgetPassword.interactor.model.ForgetPasswordReq;
import com.gannon.forgetPassword.interactor.model.ForgetPasswordRes;
import com.gannon.home.model.AmountSaveReq;
import com.gannon.home.model.ApprovedDenySaveRes;
import com.gannon.home.model.ApprovedSaveReq;
import com.gannon.home.model.DenySaveReq;
import com.gannon.home.model.HomeAllListReq;
import com.gannon.home.model.HomeAllListRes;
import com.gannon.home.model.HomeApproveListRes;
import com.gannon.home.model.HomeDenyListRes;
import com.gannon.home.model.HomeListEditReqPayLoad;
import com.gannon.home.model.HomeListEditResponsePayLoad;
import com.gannon.home.model.NotificationsCountReq;
import com.gannon.home.model.NotificationsCountRes;
import com.gannon.home.model.ProductNamesDropDownServiceReq;
import com.gannon.home.model.ProductNamesDropDownServiceRes;
import com.gannon.home.model.SearchProductReq;
import com.gannon.home.model.SearchProductRes;
import com.gannon.home.model.StatusSaveReq;
import com.gannon.myfavourite.model.MyFavouriteReqPayLoad;
import com.gannon.myfavourite.model.MyFavouriteResponsePayLoad;
import com.gannon.myfavourite.model.MyFavouriteUpdateReqPayLoad;
import com.gannon.mysales.model.ImageDeleteReq;
import com.gannon.mysales.model.ImageDeleteRes;
import com.gannon.mysales.model.MySalesEditReqPayLoad;
import com.gannon.mysales.model.MySalesEditResponsePayLoad;
import com.gannon.mysales.model.MySalesHistoryReqPayLoad;
import com.gannon.mysales.model.MySalesHistoryResponsePayLoad;
import com.gannon.mysales.model.MySalesUpdateReq;
import com.gannon.mywins.model.MyWinsReqPayLoad;
import com.gannon.mywins.model.MyWinsResponsePayLoad;
import com.gannon.notifications.model.NotificationsReq;
import com.gannon.notifications.model.NotificationsRes;
import com.gannon.notifications.model.NotificationsUpdateReq;
import com.gannon.notifications.model.NotificationsUpdateRes;
import com.gannon.notifications.model.UserNotificationsRes;
import com.gannon.notifications.model.UserNotificationsUpdateReq;
import com.gannon.profileUpdate.interactor.model.ProfileGetReq;
import com.gannon.profileUpdate.interactor.model.ProfileGetRes;
import com.gannon.profileUpdate.interactor.model.ProfileUpdateReq;
import com.gannon.profileUpdate.interactor.model.ProfileUpdateRes;
import com.gannon.splash.interactor.model.CheckForUpdateRes;
import com.gannon.uploadAuctionDonation.FileUploadRespPojo;
import com.gannon.mysales.model.MySalesReqPayLoad;
import com.gannon.mysales.model.MySalesResponsePayLoad;
import com.gannon.uploadAuctionDonation.interactor.model.SaveResponsePayLoad;
import com.gannon.uploadAuctionDonation.interactor.model.NewAuctionDonationSaveReq;
import com.gannon.usermanagement.UserMangAuctionSearchReq;
import com.gannon.usermanagement.UserMangSearchReq;
import com.gannon.usermanagement.UserMangSearchRes;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface RestAPI {

    @GET(ApplicationContext.RELATIVE_PATH + ApplicationContext.CheckForAppUpdate)
    Call<CheckForUpdateRes> getCheckForUpdateResCall();

    @POST(ApplicationContext.RELATIVE_PATH + ApplicationContext.login_details)
    Call<LoginActivityRes> getLoginResCall(@Body LoginActivityReq adminLoginReq);


    @POST(ApplicationContext.RELATIVE_PATH + ApplicationContext.register_details)
    Call<RegisterActivityRes> getRegisterActivityResCall(@Body RegisterActivityReq registerActivityReq);

    @POST(ApplicationContext.RELATIVE_PATH + ApplicationContext.user_verify)
    Call<VerifyOtpRes> getVerifyOtpResCall(@Body VerifyOtpReq verifyOtpReq);


    @POST(ApplicationContext.RELATIVE_PATH + ApplicationContext.forget_password)
    Call<ForgetPasswordRes> getForgetPasswordResCall(@Body ForgetPasswordReq adminForgetPasswordReq);


    @POST(ApplicationContext.RELATIVE_PATH + ApplicationContext.profile_update)
    Call<ProfileUpdateRes> getProfileUpdateResCall(@Body ProfileUpdateReq profileUpdateReq);

    @POST(ApplicationContext.RELATIVE_PATH + ApplicationContext.profile_Get)
    Call<ProfileGetRes> getProfileGetResCall(@Body ProfileGetReq profileUpdateReq);

    @POST(ApplicationContext.RELATIVE_PATH + ApplicationContext.approvedServiceList)
    Call<HomeApproveListRes> getHomeCategorysListResCall();

    @POST(ApplicationContext.RELATIVE_PATH + ApplicationContext.denyUsersListServiceList)
    Call<HomeDenyListRes> getHomeDenyListResCall();



    @POST(ApplicationContext.RELATIVE_PATH + ApplicationContext.approvedServiceList)
    Call<HomeApproveListRes> getUserMangApproveListResCall(@Body UserMangSearchReq userMangSearchReq);

    @POST(ApplicationContext.RELATIVE_PATH + ApplicationContext.denyUsersListServiceList)
    Call<HomeDenyListRes> getUserMangDenyListResCall(@Body UserMangSearchReq userMangSearchReq);




    @POST(ApplicationContext.RELATIVE_PATH + ApplicationContext.approvedServicesave)
    Call<ApprovedDenySaveRes> getApproveSaveResCall(@Body ApprovedSaveReq approvedSaveReq);

    @POST(ApplicationContext.RELATIVE_PATH + ApplicationContext.denyUsersListServicesave)
    Call<ApprovedDenySaveRes> getDenySaveResCall(@Body DenySaveReq denySaveReq);

    @POST(ApplicationContext.RELATIVE_PATH + ApplicationContext.auctionOrDonationServiceSave)
    Call<SaveResponsePayLoad> getNewAuctionDonationResponsePayLoadCall(@Body NewAuctionDonationSaveReq denySaveReq);

    @Multipart
    @POST(ApplicationContext.RELATIVE_PATH + "auctionOrDonationService/upload")
    Call<FileUploadRespPojo> fileUploadService(@Part List<MultipartBody.Part> file);

    @POST(ApplicationContext.RELATIVE_PATH + ApplicationContext.auctionOrDonationServiceList)
    Call<MySalesResponsePayLoad> getMySalesListResPayLoadCall(@Body MySalesReqPayLoad denySaveReq);

    @POST(ApplicationContext.RELATIVE_PATH + ApplicationContext.auctionOrDonationServiceDetails)
    Call<MySalesEditResponsePayLoad> getMySalesEditResponsePayLoadCall(@Body MySalesEditReqPayLoad denySaveReq);

    @POST(ApplicationContext.RELATIVE_PATH + ApplicationContext.auctionOrDonationServiceUpdate)
    Call<SaveResponsePayLoad> getMySalesUpdateResponsePayLoadCall(@Body MySalesUpdateReq mySalesUpdateReq);

    @POST(ApplicationContext.RELATIVE_PATH + ApplicationContext.allAuctionDonationsServiceList)
    Call<HomeAllListRes> getHomeAllListResCall(@Body HomeAllListReq homeAllListReq);

    @POST(ApplicationContext.RELATIVE_PATH + ApplicationContext.allAuctionDonationsServiceDetails)
    Call<HomeListEditResponsePayLoad> getHomeListEditResponsePayLoadCall(@Body HomeListEditReqPayLoad homeListEditReqPayLoad);

    @POST(ApplicationContext.RELATIVE_PATH + ApplicationContext.auctionOrDonationServiceHistory)
    Call<MySalesHistoryResponsePayLoad> getMySalesHistoryResponsePayLoadCall(@Body MySalesHistoryReqPayLoad denySaveReq);


    @POST(ApplicationContext.RELATIVE_PATH + ApplicationContext.userWinsList)
    Call<MyWinsResponsePayLoad> getMyWinsResponsePayLoadCall(@Body MyWinsReqPayLoad denySaveReq);

    @POST(ApplicationContext.RELATIVE_PATH + ApplicationContext.myFavouriteServiceList)
    Call<MyFavouriteResponsePayLoad> getMyFavouriteResponsePayLoadCall(@Body MyFavouriteReqPayLoad denySaveReq);

    @POST(ApplicationContext.RELATIVE_PATH + ApplicationContext.myFavouriteServiceUpdate)
    Call<SaveResponsePayLoad> getMyFavouriteUpdatecall(@Body MyFavouriteUpdateReqPayLoad denySaveReq);

    @POST(ApplicationContext.RELATIVE_PATH + ApplicationContext.searchallAuctionDonationsServiceList)
    Call<SearchProductRes> getSearchProductResCall(@Body SearchProductReq searchProductReq);

    @POST(ApplicationContext.RELATIVE_PATH + ApplicationContext.allAuctionDonationsServiceAmountupdate)
    Call<SaveResponsePayLoad> getAmountSaveResCall(@Body AmountSaveReq amountSaveReq);

    @POST(ApplicationContext.RELATIVE_PATH + ApplicationContext.usersListDropDownService)
    Call<UserMangSearchRes> getUserMangSearchResCall(@Body UserMangAuctionSearchReq searchProductReq);

    @POST(ApplicationContext.RELATIVE_PATH + ApplicationContext.usersListDropDownService)
    Call<ProductNamesDropDownServiceRes> getProductNamesDropDownServiceResCall(@Body ProductNamesDropDownServiceReq searchProductReq);

    @POST(ApplicationContext.RELATIVE_PATH + ApplicationContext.allAuctionDonationsService_adminUpdate)
    Call<SaveResponsePayLoad> getStatusSaveResponsePayLoadCall(@Body StatusSaveReq statusSaveReq);

    @POST(ApplicationContext.RELATIVE_PATH + ApplicationContext.notificationsServiceList)
    Call<NotificationsRes> getNotificationsResCall(@Body NotificationsReq denySaveReq);

    @POST(ApplicationContext.RELATIVE_PATH + ApplicationContext.notificationsServiceUpdate)
    Call<NotificationsUpdateRes> getNotificationsUpdateResCall(@Body NotificationsUpdateReq denySaveReq);

    @POST(ApplicationContext.RELATIVE_PATH + ApplicationContext.notificationsServiceCount)
    Call<NotificationsCountRes> getNotificationsCountResCall(@Body NotificationsCountReq countReq);

    @POST(ApplicationContext.RELATIVE_PATH + ApplicationContext.usernotificationsServiceCount)
    Call<NotificationsCountRes> getUserNotificationsCountResCall(@Body NotificationsCountReq countReq);


    @POST(ApplicationContext.RELATIVE_PATH + ApplicationContext.usernotificationsServiceUpdate)
    Call<NotificationsUpdateRes> getUserNotificationsUpdateResCall(@Body UserNotificationsUpdateReq updateReq);

    @POST(ApplicationContext.RELATIVE_PATH + ApplicationContext.usernotificationsServiceList)
    Call<UserNotificationsRes> getUserNotificationsResCall(@Body NotificationsReq denySaveReq);

    @POST(ApplicationContext.RELATIVE_PATH + ApplicationContext.auctionOrDonationService_delete)
    Call<ImageDeleteRes> getImageDeleteResCall(@Body ImageDeleteReq imageDeleteReq);

}
