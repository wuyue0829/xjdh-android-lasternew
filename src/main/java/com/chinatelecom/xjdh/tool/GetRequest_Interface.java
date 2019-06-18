package com.chinatelecom.xjdh.tool;

import com.chinatelecom.xjdh.bean.CityBean;
import com.chinatelecom.xjdh.bean.ConversationBean;
import com.chinatelecom.xjdh.bean.GetCallRecord;
import com.chinatelecom.xjdh.bean.LoginRequest;
import com.chinatelecom.xjdh.bean.PostCallRecord;
import com.chinatelecom.xjdh.bean.PostCreateConference;
import com.chinatelecom.xjdh.bean.RoomBean;
import com.chinatelecom.xjdh.bean.SubstationBean;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface GetRequest_Interface {

    @GET("Api/Get_City_List")
    Call<CityBean> getCall(@Query("access_token") String userToken);

    @GET
    Call<SubstationBean> getSubstationCall(@Url String Url, @Query("access_token") String userToken);

    @GET
    Call<RoomBean> getRoomCall(@Url String Url, @Query("access_token") String userToken);

    @GET("Api/Get_User_List")
    Call<ConversationBean> getConversationCall(@Query("access_token") String userToken);

    @POST("Api/Save_Call_Records")
    Call<LoginRequest> postCallRecords(@Body PostCallRecord callRecord);

    @GET("Api/Get_call_records")
    Call<GetCallRecord> getCallRecords(@QueryMap Map<String, String> params);

    @POST("Api/Create_Conference")
    Call<LoginRequest> postCreateConference(@Body PostCreateConference postCreateConference);

    @POST("Api/Create_Conference")
    Call<String> postCreateConference1(@Body PostCreateConference postCreateConference);

}