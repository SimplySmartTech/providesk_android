package com.simplysmart.lib.endpint;

import com.simplysmart.lib.model.bill.BillDetailResponse;
import com.simplysmart.lib.model.bots.BotResponse;
import com.simplysmart.lib.model.categories.CategoriesResponse;
import com.simplysmart.lib.model.categories.v2.CategoryResponse;
import com.simplysmart.lib.model.common.CloudinaryCredential;
import com.simplysmart.lib.model.common.CommonResponse;
import com.simplysmart.lib.model.helpdesk.ComplaintChatRequest;
import com.simplysmart.lib.model.helpdesk.ComplaintChatResponse;
import com.simplysmart.lib.model.helpdesk.ComplaintDetailResponse;
import com.simplysmart.lib.model.helpdesk.ComplaintFeedbackRequest;
import com.simplysmart.lib.model.helpdesk.ComplaintFeedbackResponse;
import com.simplysmart.lib.model.helpdesk.ComplaintRequest;
import com.simplysmart.lib.model.helpdesk.HelpDeskResponse;
import com.simplysmart.lib.model.login.ChangePasswordRequest;
import com.simplysmart.lib.model.login.LoginRequest;
import com.simplysmart.lib.model.login.LoginResponse;
import com.simplysmart.lib.model.notification.NotificationRequest;
import com.simplysmart.lib.model.notification.NotificationResponse;
import com.simplysmart.lib.model.planner.PlannerRequest;
import com.simplysmart.lib.model.planner.PlannerResponse;
import com.simplysmart.lib.model.sensor.SensorList;
import com.simplysmart.lib.model.sensor.SensorReadingGraphResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by shekhar on 13/05/16.
 */
public interface ApiInterface {


    //Resident login
    @POST("/api/sessions/sign_in")
    Call<LoginResponse> residentLogin(@Body LoginRequest request);

    //Resident logout
    @DELETE("/api/sessions/sign_out")
    Call<CommonResponse> residentLogout(@Query("subdomain") String subDomain);

    @POST("/api/sessions/sign_in")
    Call<LoginResponse> residentLoginWithSubDomain(@Query("subdomain") String subDomain,
                                                   @Query("site_id") String site_id,
                                                   @Body LoginRequest request);

    @GET("/api/passwords/reset_resident")
    Call<LoginResponse> resetPassword(@Query("username") String username);

    //Fetch categories
    @GET("/cms/categories")
    Call<CategoriesResponse> fetchCategories(@Query("subdomain") String subDomain,
                                             @Query("site_id") String site_id);

    //Fetch categories
    @GET("/cms/categories")
    Call<CategoryResponse> fetchCategoriesV2(@Query("subdomain") String subDomain,
                                             @Query("site_id") String site_id);

    //Get resident notifications
    @GET("/api/residents/{residentId}/notifications")
    Call<NotificationResponse> getNotificationList(@Path("residentId") String residentId,
                                                   @Query("subdomain") String subDomain,
                                                   @Query("site_id") String site_id,
                                                   @Query("filter") String filter);

    //Update notifications status (Read/Unread)
    @PUT("/api/residents/{residentId}/notifications/{notificationId}")
    Call<NotificationResponse> updateNotificationStatus(@Path("residentId") String residentId,
                                                        @Path("notificationId") String notificationId,
                                                        @Query("subdomain") String subDomain,
                                                        @Query("site_id") String site_id,
                                                        @Body NotificationRequest request);

    //Show planner details
    @GET("/api/units/{unitId}/planner")
    Call<PlannerResponse> showPlannerDetails(@Path("unitId") String unitId,
                                             @Query("subdomain") String subDomain,
                                             @Query("site_id") String site_id);

    //Update planner details
    @PUT("/api/units/{unitId}/planner")
    Call<PlannerResponse> updatePlannerDetails(@Path("unitId") String unitId,
                                               @Query("subdomain") String subDomain,
                                               @Query("site_id") String site_id,
                                               @Body PlannerRequest request);

    //Show Bot Details (Meter details)
    @Headers("Cache-Control: public,max-age=600")
    @GET("/api/units/{unitId}/bots/{botType}")
    Call<BotResponse> getBotDetails(@Path("unitId") String unitId,
                                    @Path("botType") String botType,
                                    @Query("subdomain") String subDomain,
                                    @Query("site_id") String site_id);

    //Show trends (Usage graph)
    @GET("/api/units/{unitId}/bots/{botType}/trends")
    Call<BotResponse> showTrends(@Path("unitId") String unitId,
                                 @Path("botType") String botType,
                                 @Query("subdomain") String subDomain,
                                 @Query("site_id") String site_id,
                                 @Query("duration") int duration);

    //Fetch cloudinary credentials for image upload
    @GET("/api/cloudinary/credentials")
    Call<CloudinaryCredential> fetchCredential(@Query("subdomain") String subDomain,
                                               @Query("site_id") String site_id);

    //Create new complaint
    @POST("/cms/complaints")
    Call<CommonResponse> createNewResponse(@Query("subdomain") String subDomain,
                                           @Query("site_id") String site_id,
                                           @Body ComplaintRequest complaintRequest);

    //Get complaint list
    @GET("cms/complaints")
    Call<HelpDeskResponse> getComplaintList(@Query("subdomain") String subDomain,
                                            @Query("site_id") String site_id,
                                            @Query("state") String state,
                                            @Query("page") int page);

    //Get complaint details
    @GET("/cms/complaints/{complaintId}")
    Call<ComplaintDetailResponse> getComplaintDetails(@Path("complaintId") String complaintId,
                                                      @Query("subdomain") String subDomain,
                                                      @Query("site_id") String site_id);

    //Post comment on specific complaint
    @POST("/cms/complaints/{complaintId}/activity")
    Call<ComplaintChatResponse> postComment(@Path("complaintId") String complaintId,
                                            @Query("subdomain") String subDomain,
                                            @Query("site_id") String site_id,
                                            @Body ComplaintChatRequest request);

    //feedback for complaint (close/restart)
    @POST("/cms/complaints/{complaint_id}/close_complaint")
    Call<ComplaintFeedbackResponse> doFeedbackComplaint(@Path("complaint_id") String complaint_id,
                                                        @Query("subdomain") String subDomain,
                                                        @Query("site_id") String site_id,
                                                        @Body ComplaintFeedbackRequest request);

    // get readings for all sensors
    @GET("api/admin/sensors")
    Call<SensorList> getSensorsReadings(@Query("subdomain") String subDomain,
                                        @Query("site_id") String state);

    // get cunsumption readings for all sensors
    @GET("api/sensor_readings")
    Call<SensorReadingGraphResponse> getReadingsGraph(@Query("subdomain") String subDomain,
                                                      @Query("site_id") String site_id,
                                                      @Query("sensor_key") String sensor_key,
                                                      @Query("from_date") String from_date,
                                                      @Query("to_date") String to_date);


    @PUT("api/residents/{resident_id}/change_password")
    Call<CommonResponse> changePassword(@Path("resident_id") String resident_id,
                                        @Query("subdomain") String subDomain,
                                        @Query("site_id") String site_id,
                                        @Body ChangePasswordRequest request);


    //Access control
    @GET("/api/units/{unitId}/open_door")
    Call<CommonResponse> openDoor(@Path("unitId") String unitId,
                                  @Query("subdomain") String subDomain,
                                  @Query("site_id") String site_id);



    @GET("/api/bills/{billId}")
    Call<BillDetailResponse> getBillDetails(@Path("billId") String billId,
                                            @Query("subdomain") String subDomain,
                                            @Query("site_id") String site_id);



}
