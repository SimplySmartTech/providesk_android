package com.simplysmart.lib.request;

import android.content.Context;

import com.simplysmart.lib.callback.ApiCallback;
import com.simplysmart.lib.common.CommonMethod;
import com.simplysmart.lib.config.ErrorUtils;
import com.simplysmart.lib.config.ServiceGenerator;
import com.simplysmart.lib.config.ServiceGeneratorV2;
import com.simplysmart.lib.endpint.ApiInterface;
import com.simplysmart.lib.endpint.WalletApiInterface;
import com.simplysmart.lib.global.AppSessionData;
import com.simplysmart.lib.model.bill.BillDetailResponse;
import com.simplysmart.lib.model.bots.BotResponse;
import com.simplysmart.lib.model.categories.CategoriesResponse;
import com.simplysmart.lib.model.categories.v2.CategoryResponse;
import com.simplysmart.lib.model.common.APIError;
import com.simplysmart.lib.model.common.CloudinaryCredential;
import com.simplysmart.lib.model.common.CommonResponse;
import com.simplysmart.lib.model.helpdesk.ComplaintChatRequest;
import com.simplysmart.lib.model.helpdesk.ComplaintChatResponse;
import com.simplysmart.lib.model.helpdesk.ComplaintDetailResponse;
import com.simplysmart.lib.model.helpdesk.ComplaintFeedbackRequest;
import com.simplysmart.lib.model.helpdesk.ComplaintFeedbackResponse;
import com.simplysmart.lib.model.helpdesk.ComplaintRequest;
import com.simplysmart.lib.model.helpdesk.ComplaintUpdateRequest;
import com.simplysmart.lib.model.helpdesk.HelpDeskResponse;
import com.simplysmart.lib.model.helpdesk.MessageResponseClass;
import com.simplysmart.lib.model.helpdesk.NewComplaint;
import com.simplysmart.lib.model.login.ChangePasswordRequest;
import com.simplysmart.lib.model.login.LoginRequest;
import com.simplysmart.lib.model.login.LoginResponse;
import com.simplysmart.lib.model.login.OtpRequest;
import com.simplysmart.lib.model.notification.NotificationRequest;
import com.simplysmart.lib.model.notification.NotificationResponse;
import com.simplysmart.lib.model.planner.Planner;
import com.simplysmart.lib.model.planner.PlannerRequest;
import com.simplysmart.lib.model.planner.PlannerResponse;
import com.simplysmart.lib.model.sensor.SensorList;
import com.simplysmart.lib.model.sensor.SensorReadingGraphResponse;
import com.simplysmart.lib.model.walllet.TransactionResponse;
import com.simplysmart.lib.model.walllet.WalletCredentialRequest;
import com.simplysmart.lib.model.walllet.WalletCredentialResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by shekhar on 13/05/16.
 * CreateRequest class offer all the methods to prepare request for network call & sync with server
 */
public class CreateRequest {

    private static CreateRequest createRequest = new CreateRequest();

    protected CreateRequest() {
    }

    public static CreateRequest getInstance() {
        return createRequest;
    }

    public void loadSessionData(String apiKey, String authToken, String subDomain, String siteId) {
        AppSessionData.getInstance().setApiKey(apiKey);
        AppSessionData.getInstance().setAuthToken(authToken);
        AppSessionData.getInstance().setSubdomain(subDomain);
        AppSessionData.getInstance().setSite_id(siteId);
    }

    public void loginRequest(Context context, String packageName, String mobileNumber, String pinNumber, String gcmToken, final ApiCallback<LoginResponse> callback) {

        LoginRequest request = new LoginRequest();
        LoginRequest.Session session = new LoginRequest.Session();
        session.setLogin(mobileNumber);
        session.setPassword(pinNumber);
        session.setDevice_id(CommonMethod.getDeviceId(context) + packageName);
        session.setNotification_token(gcmToken);
        request.setSession(session);
        request.setUser_login(true);

        ApiInterface apiInterface = ServiceGeneratorV2.createService(ApiInterface.class);

        Call<LoginResponse> loginResponseCall = apiInterface.residentLogin(request);

        loginResponseCall.enqueue(new Callback<LoginResponse>() {

            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    callback.onFailure(error.message());
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

            }
        });
    }

    public void loginRequestWithSubDomain(Context context, String packageName, String subDomain, String mobileNumber, String pinNumber, String gcmToken, final ApiCallback<LoginResponse> callback) {

        LoginRequest request = new LoginRequest();
        LoginRequest.Session session = new LoginRequest.Session();
        session.setLogin(mobileNumber);
        session.setPassword(pinNumber);
        session.setDevice_id(CommonMethod.getDeviceId(context) + packageName);
        session.setNotification_token(gcmToken);
        request.setSession(session);

        ApiInterface apiInterface = ServiceGeneratorV2.createService(ApiInterface.class);

        Call<LoginResponse> loginResponseCall = apiInterface.residentLoginWithSubDomain(subDomain, AppSessionData.getInstance().getSite_id(), request);

        loginResponseCall.enqueue(new Callback<LoginResponse>() {

            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    callback.onFailure(error.message());
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

            }
        });
    }

    public void logoutRequestWithSubDomain(final ApiCallback<CommonResponse> callback) {

        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);

        Call<CommonResponse> loginResponseCall = apiInterface.residentLogout(
                AppSessionData.getInstance().getSubdomain());

        loginResponseCall.enqueue(new Callback<CommonResponse>() {

            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {

                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    callback.onFailure(error.message());
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {

            }
        });
    }

    public void generateOtpRequest(String userId, String subDomain, final ApiCallback<CommonResponse> callback) {

        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);

        Call<CommonResponse> loginResponseCall = apiInterface.generateOTP(
                userId,
                subDomain);

        loginResponseCall.enqueue(new Callback<CommonResponse>() {

            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {

                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    callback.onFailure(error.message());
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {

            }
        });
    }

    public void verifyOtpRequest(String userId, String subDomain, OtpRequest request, final ApiCallback<LoginResponse> callback) {

        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<LoginResponse> responseCall = apiInterface.verifyOTP(
                userId,
                subDomain,
                request);

        responseCall.enqueue(new Callback<LoginResponse>() {

            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    callback.onFailure(error.message());
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

            }
        });
    }

    public void changePassword(String residentId, ChangePasswordRequest request, final ApiCallback<CommonResponse> callback) {

        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<CommonResponse> responseCall = apiInterface.changePassword(
                residentId,
                AppSessionData.getInstance().getSubdomain(),
                AppSessionData.getInstance().getSite_id(),
                request);

        responseCall.enqueue(new Callback<CommonResponse>() {

            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {

                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    callback.onFailure(error.message());
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {

            }
        });
    }

    public void resetPassword(String username, final ApiCallback<LoginResponse> callback) {

        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<LoginResponse> responseCall = apiInterface.resetPassword(username);

        responseCall.enqueue(new Callback<LoginResponse>() {

            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    callback.onFailure(error.message());
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

            }
        });
    }

    public void fetchCategories(final ApiCallback<CategoriesResponse> callback) {

        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<CategoriesResponse> responseCall = apiInterface.fetchCategories(
                AppSessionData.getInstance().getSubdomain(),
                AppSessionData.getInstance().getSite_id());

        responseCall.enqueue(new Callback<CategoriesResponse>() {

            @Override
            public void onResponse(Call<CategoriesResponse> call, Response<CategoriesResponse> response) {

                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    callback.onFailure(error.message());
                }
            }

            @Override
            public void onFailure(Call<CategoriesResponse> call, Throwable t) {

            }
        });
    }

    public void fetchCredential(final ApiCallback<CloudinaryCredential> callback) {

        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<CloudinaryCredential> responseCall = apiInterface.fetchCredential(
                AppSessionData.getInstance().getSubdomain(),
                AppSessionData.getInstance().getSite_id());

        responseCall.enqueue(new Callback<CloudinaryCredential>() {

            @Override
            public void onResponse(Call<CloudinaryCredential> call, Response<CloudinaryCredential> response) {

                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    callback.onFailure(error.message());
                }
            }

            @Override
            public void onFailure(Call<CloudinaryCredential> call, Throwable t) {

            }
        });
    }

    public void getNotificationList(String filter, String residentId, final ApiCallback<NotificationResponse> callback) {

        ApiInterface apiInterface = ServiceGeneratorV2.createService(ApiInterface.class);
        Call<NotificationResponse> responseCall = apiInterface.getNotificationList(
                residentId, AppSessionData.getInstance().getSubdomain(),
                AppSessionData.getInstance().getSite_id(),
                filter);

        responseCall.enqueue(new Callback<NotificationResponse>() {

            @Override
            public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {

                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    callback.onFailure(error.message());
                }
            }

            @Override
            public void onFailure(Call<NotificationResponse> call, Throwable t) {

            }
        });
    }

    public void updateNotificationStatus(String residentId, String notificationId, String readAt, final ApiCallback<NotificationResponse> callback) {

        NotificationRequest putRequest = new NotificationRequest();
        NotificationRequest.Data data = new NotificationRequest.Data();
        data.setRead_at(readAt);
        putRequest.setNotification(data);

        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<NotificationResponse> responseCall = apiInterface.updateNotificationStatus(
                residentId,
                notificationId,
                AppSessionData.getInstance().getSubdomain(),
                AppSessionData.getInstance().getSite_id(),
                putRequest);

        responseCall.enqueue(new Callback<NotificationResponse>() {

            @Override
            public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {

                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    callback.onFailure(error.message());
                }
            }

            @Override
            public void onFailure(Call<NotificationResponse> call, Throwable t) {

            }
        });
    }

    public void createNewComplaint(NewComplaint complaint, final ApiCallback<CommonResponse> callback) {

        ComplaintRequest complaintRequest = new ComplaintRequest();
        complaintRequest.setComplaint(complaint);

        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<CommonResponse> responseCall = apiInterface.createNewResponse(
                AppSessionData.getInstance().getSubdomain(),
                AppSessionData.getInstance().getSite_id(),
                complaintRequest);

        responseCall.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    callback.onFailure(error.message());
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {

            }
        });
    }

    public void getComplaintList(String state, int page, final ApiCallback<HelpDeskResponse> callback) {

        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<HelpDeskResponse> responseCall = apiInterface.getComplaintList(
                AppSessionData.getInstance().getSubdomain(),
                AppSessionData.getInstance().getSite_id(),
                state,
                page);

        responseCall.enqueue(new Callback<HelpDeskResponse>() {

            @Override
            public void onResponse(Call<HelpDeskResponse> call, Response<HelpDeskResponse> response) {

                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    callback.onFailure(error.message());
                }
            }

            @Override
            public void onFailure(Call<HelpDeskResponse> call, Throwable t) {

            }
        });
    }

    public void getComplaintDetails(String complaintId, final ApiCallback<ComplaintDetailResponse> callback) {

        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<ComplaintDetailResponse> responseCall = apiInterface.getComplaintDetails(complaintId,
                AppSessionData.getInstance().getSubdomain(),
                AppSessionData.getInstance().getSite_id());

        responseCall.enqueue(new Callback<ComplaintDetailResponse>() {

            @Override
            public void onResponse(Call<ComplaintDetailResponse> call, Response<ComplaintDetailResponse> response) {

                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    callback.onFailure(error.message());
                }
            }

            @Override
            public void onFailure(Call<ComplaintDetailResponse> call, Throwable t) {

            }
        });
    }

    public void postComment(String complaintId, String comment, String image_url, final ApiCallback<ComplaintChatResponse> callback) {

        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);

        ComplaintChatRequest request = new ComplaintChatRequest();
        ComplaintChatRequest.Activity activity = new ComplaintChatRequest.Activity();
        activity.setText(comment);
        activity.setImage_url(image_url);
        request.setActivity(activity);

        Call<ComplaintChatResponse> responseCall = apiInterface.postComment(complaintId,
                AppSessionData.getInstance().getSubdomain(),
                AppSessionData.getInstance().getSite_id(),
                request);

        responseCall.enqueue(new Callback<ComplaintChatResponse>() {

            @Override
            public void onResponse(Call<ComplaintChatResponse> call, Response<ComplaintChatResponse> response) {

                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    callback.onFailure(error.message());
                }
            }

            @Override
            public void onFailure(Call<ComplaintChatResponse> call, Throwable t) {

            }
        });
    }

    public void getPlannerDetails(String unitId, final ApiCallback<PlannerResponse> callback) {

        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<PlannerResponse> responseCall = apiInterface.showPlannerDetails(unitId,
                AppSessionData.getInstance().getSubdomain(),
                AppSessionData.getInstance().getSite_id());

        responseCall.enqueue(new Callback<PlannerResponse>() {

            @Override
            public void onResponse(Call<PlannerResponse> call, Response<PlannerResponse> response) {

                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    callback.onFailure(error.message());
                }
            }

            @Override
            public void onFailure(Call<PlannerResponse> call, Throwable t) {

            }
        });
    }

    public void updatePlannerDetails(String unitId, boolean amc, int duration, int water, int electricity, final ApiCallback<PlannerResponse> callback) {

        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);

        PlannerRequest request = new PlannerRequest();
        Planner planner = new Planner();
        planner.setAmc(amc);
        planner.setDuration(duration);
        planner.setWater(water);
        planner.setElectricity(electricity);
        request.setPlanner(planner);

        Call<PlannerResponse> responseCall = apiInterface.updatePlannerDetails(unitId,
                AppSessionData.getInstance().getSubdomain(),
                AppSessionData.getInstance().getSite_id(),
                request);

        responseCall.enqueue(new Callback<PlannerResponse>() {

            @Override
            public void onResponse(Call<PlannerResponse> call, Response<PlannerResponse> response) {

                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    callback.onFailure(error.message());
                }
            }

            @Override
            public void onFailure(Call<PlannerResponse> call, Throwable t) {

            }
        });
    }

    public void getBotDetails(String unitId, String botType, final ApiCallback<BotResponse> callback) {

        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<BotResponse> responseCall = apiInterface.getBotDetails(unitId,
                botType,
                AppSessionData.getInstance().getSubdomain(),
                AppSessionData.getInstance().getSite_id());

        responseCall.enqueue(new Callback<BotResponse>() {

            @Override
            public void onResponse(Call<BotResponse> call, Response<BotResponse> response) {

                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    callback.onFailure(error.message());
                }
            }

            @Override
            public void onFailure(Call<BotResponse> call, Throwable t) {

            }
        });
    }

    public void showTrends(String unitId, String botType, int duration, final ApiCallback<BotResponse> callback) {

        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<BotResponse> responseCall = apiInterface.showTrends(unitId,
                botType,
                AppSessionData.getInstance().getSubdomain(),
                AppSessionData.getInstance().getSite_id(),
                duration);

        responseCall.enqueue(new Callback<BotResponse>() {

            @Override
            public void onResponse(Call<BotResponse> call, Response<BotResponse> response) {

                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    callback.onFailure(error.message());
                }
            }

            @Override
            public void onFailure(Call<BotResponse> call, Throwable t) {

            }
        });
    }

    public void giveFeedback(String complaint_id, ComplaintFeedbackRequest request, final ApiCallback<ComplaintFeedbackResponse> callback) {

        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<ComplaintFeedbackResponse> responseCall = apiInterface.doFeedbackComplaint(complaint_id,
                AppSessionData.getInstance().getSubdomain(),
                AppSessionData.getInstance().getSite_id(),
                request);

        responseCall.enqueue(new Callback<ComplaintFeedbackResponse>() {

            @Override
            public void onResponse(Call<ComplaintFeedbackResponse> call, Response<ComplaintFeedbackResponse> response) {

                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    callback.onFailure(error.message());
                }
            }

            @Override
            public void onFailure(Call<ComplaintFeedbackResponse> call, Throwable t) {

            }
        });
    }

    public void getSensorsReadings(String unitId, final ApiCallback<SensorList> callback) {

        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<SensorList> responseCall = apiInterface.getSensorsReadings(AppSessionData.getInstance().getSubdomain(), unitId);

        responseCall.enqueue(new Callback<SensorList>() {

            @Override
            public void onResponse(Call<SensorList> call, Response<SensorList> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    callback.onFailure(error.message());
                }
            }

            @Override
            public void onFailure(Call<SensorList> call, Throwable t) {

            }
        });
    }

    public void getSensorsReadingGraph(String sensorKey, String fromDate, String toDate, final ApiCallback<SensorReadingGraphResponse> callback) {

        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<SensorReadingGraphResponse> responseCall = apiInterface.getReadingsGraph(AppSessionData.getInstance().getSubdomain(),
                AppSessionData.getInstance().getSite_id(),
                sensorKey,
                fromDate,
                toDate);

        responseCall.enqueue(new Callback<SensorReadingGraphResponse>() {

            @Override
            public void onResponse(Call<SensorReadingGraphResponse> call, Response<SensorReadingGraphResponse> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    callback.onFailure(error.message());
                }
            }

            @Override
            public void onFailure(Call<SensorReadingGraphResponse> call, Throwable t) {

            }
        });
    }

    public void getTransactionCredentials(WalletCredentialRequest request, final ApiCallback<WalletCredentialResponse> callback) {

        WalletApiInterface apiInterface = ServiceGenerator.createService(WalletApiInterface.class);
        Call<WalletCredentialResponse> responseCall = apiInterface.getTransactionCredentials(
                AppSessionData.getInstance().getSubdomain(),
                AppSessionData.getInstance().getSite_id(),
                request);

        responseCall.enqueue(new Callback<WalletCredentialResponse>() {

            @Override
            public void onResponse(Call<WalletCredentialResponse> call, Response<WalletCredentialResponse> response) {

                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    callback.onFailure(error.message());
                }
            }

            @Override
            public void onFailure(Call<WalletCredentialResponse> call, Throwable t) {

            }
        });
    }

    public void getTransactions(String residentId, final ApiCallback<TransactionResponse> callback) {

        WalletApiInterface apiInterface = ServiceGenerator.createService(WalletApiInterface.class);
        Call<TransactionResponse> responseCall = apiInterface.getTransactions(residentId,
                AppSessionData.getInstance().getSubdomain(),
                AppSessionData.getInstance().getSite_id());

        responseCall.enqueue(new Callback<TransactionResponse>() {

            @Override
            public void onResponse(Call<TransactionResponse> call, Response<TransactionResponse> response) {

                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    callback.onFailure(error.message());
                }
            }

            @Override
            public void onFailure(Call<TransactionResponse> call, Throwable t) {

            }
        });
    }

    public void updateTransaction(String transactionId, WalletCredentialRequest request, final ApiCallback<TransactionResponse> callback) {

        WalletApiInterface apiInterface = ServiceGenerator.createService(WalletApiInterface.class);
        Call<TransactionResponse> responseCall = apiInterface.updateTransaction(transactionId,
                AppSessionData.getInstance().getSubdomain(),
                AppSessionData.getInstance().getSite_id(),
                request);

        responseCall.enqueue(new Callback<TransactionResponse>() {

            @Override
            public void onResponse(Call<TransactionResponse> call, Response<TransactionResponse> response) {

                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    callback.onFailure(error.message());
                }
            }

            @Override
            public void onFailure(Call<TransactionResponse> call, Throwable t) {

            }
        });
    }

    public void openDoor(String unitId, final ApiCallback<CommonResponse> callback) {

        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<CommonResponse> responseCall = apiInterface.openDoor(unitId,
                AppSessionData.getInstance().getSubdomain(),
                AppSessionData.getInstance().getSite_id());

        responseCall.enqueue(new Callback<CommonResponse>() {

            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {

                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    callback.onFailure(error.message());
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {

            }
        });
    }

    public void fetchCategoriesV2(final ApiCallback<CategoryResponse> callback) {

        ApiInterface apiInterface = ServiceGeneratorV2.createService(ApiInterface.class);
        Call<CategoryResponse> responseCall = apiInterface.fetchCategoriesV2(
                AppSessionData.getInstance().getSubdomain(),
                AppSessionData.getInstance().getSite_id());

        responseCall.enqueue(new Callback<CategoryResponse>() {

            @Override
            public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {

                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    callback.onFailure(error.message());
                }
            }

            @Override
            public void onFailure(Call<CategoryResponse> call, Throwable t) {

            }
        });
    }

    public void getBillDetails(String billId, final ApiCallback<BillDetailResponse> callback) {

        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<BillDetailResponse> responseCall = apiInterface.getBillDetails(
                billId,
                AppSessionData.getInstance().getSubdomain(),
                AppSessionData.getInstance().getSite_id());

        responseCall.enqueue(new Callback<BillDetailResponse>() {

            @Override
            public void onResponse(Call<BillDetailResponse> call, Response<BillDetailResponse> response) {

                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    callback.onFailure(error.message());
                }
            }

            @Override
            public void onFailure(Call<BillDetailResponse> call, Throwable t) {

            }
        });
    }

    public void updateComplaintStatus(String complaintId, ComplaintUpdateRequest request, final ApiCallback<MessageResponseClass> callback) {

        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<MessageResponseClass> responseCall = apiInterface.updateComplaintStatus(
                complaintId,
                AppSessionData.getInstance().getSubdomain(),
                request);

        responseCall.enqueue(new Callback<MessageResponseClass>() {

            @Override
            public void onResponse(Call<MessageResponseClass> call, Response<MessageResponseClass> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    callback.onFailure(error.message());
                }
            }

            @Override
            public void onFailure(Call<MessageResponseClass> call, Throwable t) {
            }
        });
    }
}
