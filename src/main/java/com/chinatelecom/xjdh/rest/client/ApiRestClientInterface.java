package com.chinatelecom.xjdh.rest.client;

import com.chinatelecom.xjdh.bean.ApiResponse;
import com.chinatelecom.xjdh.bean.ApiResponseImage;
import com.chinatelecom.xjdh.bean.ApiResponseUpLoad;
import com.chinatelecom.xjdh.bean.ApiResponseUrl;
import com.chinatelecom.xjdh.bean.DoorOperation;
import com.chinatelecom.xjdh.bean.JsonResponse;
import com.chinatelecom.xjdh.bean.SPDevResponse;
import com.chinatelecom.xjdh.rest.interceptor.HttpBasicAuthenticatorInterceptor;
import com.chinatelecom.xjdh.utils.URLs;
import com.chinatelecom.xjdh.utils.Update;

import org.androidannotations.annotations.rest.Get;
import org.androidannotations.annotations.rest.Post;
import org.androidannotations.annotations.rest.RequiresHeader;
import org.androidannotations.annotations.rest.Rest;
import org.androidannotations.api.rest.RestClientHeaders;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;

import java.util.LinkedHashMap;

/**
 * @author peter
 * 
 */
@Rest(rootUrl = URLs.URL_API_HOST + "/api/" + URLs.API_VERSION, converters = { MappingJacksonHttpMessageConverter.class,
		StringHttpMessageConverter.class, MyFormHttpMessageConverter.class, MyStringHttpMessageConverter.class,
		ResourceHttpMessageConverter.class, FormHttpMessageConverter.class,
		ByteArrayHttpMessageConverter.class }, interceptors = { HttpBasicAuthenticatorInterceptor.class })
@RequiresHeader("Authorization")
public interface ApiRestClientInterface extends RestClientHeaders {
	@Get("/getuserinfo")
	ApiResponse getUserInfo() throws RestClientException;
	
	@Get("/getMobileAuth") 
	ApiResponse getMobileAuth() throws RestClientException;
	
	@Get("/GetCameraUrl?data_id={data_id}") 
	ApiResponseUrl GetCameraUrl(String data_id) throws RestClientException;
	
//	@Post("/get_video_url") 
//	ApiResponseUpLoad get_video_url(MultiValueMap<String, Object> multiValueMap) throws RestClientException;
	
	@Get("/getAlarmList?citycode={citycode}&countycode={countycode}&substationId={substationId}&roomId={roomId}&level={level}&model={model}&signalName={signalName}&startdatetime={startdatetime}&enddatetime={enddatetime}&offset={offset}&count={count}&lastId={lastId}")
	ApiResponse getAlarmList(String citycode, String countycode, String substationId, String roomId, String level,
                             String model, String signalName, String startdatetime, String enddatetime, String offset, String count, String lastId)
					throws RestClientException;

	@Get("/getSubstationList?name={name}&offset={offset}")
	ApiResponse getSubstationList(String name, int offset) throws RestClientException;

	@Get("/getPreAlarmList?citycode={citycode}&countycode={countycode}&substationId={substationId}&roomId={roomId}&level={level}&model={model}&signalName={signalName}&startdatetime={startdatetime}&enddatetime={enddatetime}&offset={offset}&count={count}&lastId={lastId}")
	ApiResponse getPreAlarmList(String citycode, String countycode, String substationId, String roomId, String level,
                                String model, String signalName, String startdatetime, String enddatetime, String offset, String count, String lastId)
					throws RestClientException;

	@Get("/getAreaData?cityName={cityName}&stationName={stationName}&stationNames={stationNames}")
	ApiResponse getAreaData(String cityName, String stationName, String stationNames) throws RestClientException;

	@Get("/GetActiveLocas?locationType={locationType}&locationID={locationID}&type={type}")
	ApiResponse GetActiveLocas(int locationType, int locationID, int type) throws RestClientException;

	@Get("/GetCheckLocation?type={type}&id={id}")
	ApiResponse GetCheckLocation(int type, int id) throws RestClientException;

	@Get("/getEditQuestions?type={type}&id={id}")
	ApiResponse getEditQuestions(int type, int id) throws RestClientException;

	@Get("/GetCheckLocation?type={type}&id={id}&apply={apply}")
	ApiResponse GetCheckLocation(int type, int id, int apply) throws RestClientException;

	@Get("/GetAppliedLocation?checkType={checkType}&applyType={applyType}&userID={userID}&subID={subID}&roomID={roomID}")
	ApiResponse GetAppliedLocation(int checkType, int applyType, int userID, int subID, int roomID)
			throws RestClientException;

	//
	@Get("/GetAppliedLocation?checkType={checkType}&applyType={applyType}&userID={userID}")
	ApiResponse GetAppliedLocation(int checkType, int applyType, int userID) throws RestClientException;

	@Get("/getEditSubs?userID={userID}&type={type}&locationType={locationType}")
	ApiResponse getEditSubs(int userID, int type, int locationType) throws RestClientException;

	@Get("/GetLocation?type={type}&id={id}&locationType={locationType}")
	ApiResponse GetLocation(int type, int id, int locationType) throws RestClientException;

	
	@Get("/getMemos?userID={userID}&subID={subID}")
	ApiResponse getMemos(int userID, int subID) throws RestClientException;
	
	@Get("/getMemoSubs?userID={userID}")
	ApiResponse getMemoSubs(int userID) throws RestClientException;
	
	@Get("/getLocations?type={type}")
	ApiResponse getLocations(int type) throws RestClientException;

	@Get("/getLocations?type={type}&id={id}")
	ApiResponse getLocations(int type, int id) throws RestClientException;

	@Get("/GetApplyInfo?type={type}&locID={locID}")
	ApiResponse GetApplyInfo(int type, int locID) throws RestClientException;

	@Get("/getArrangeSubs?userID={userID}")
	ApiResponse getArrangeSubs(int userID) throws RestClientException;

	@Get("/GetQuestion?type={type}&topicID={topicID}")
	ApiResponse GetQuestion(int type, int topicID) throws RestClientException;

	@Get("/getDevModelData")
	ApiResponse getDevModelData() throws RestClientException;
	
	@Get("/getSignalNamelData")
	ApiResponse getSignalNamelData() throws RestClientException;
	
	@Get("/getDevCategoryData")
	ApiResponse getDevCategoryData() throws RestClientException;
	
	@Get("/getLatestAlarmId")
	String getLatestAlarmId();

	// 修改密码
	@Get("/ChangePasswd?txtPasswdold={txtPasswdold}&txtPasswdnew={txtPasswdnew}&txtPasswdagain={txtPasswdagain}")
	JsonResponse changepasswd(String txtPasswdold, String txtPasswdnew, String txtPasswdagain)
			throws RestClientException;

	@Get("/getRoomDeviceList?roomcode={roomcode}&devtype={devtype}")
	ApiResponse getRoomDeviceList(String roomcode, String devtype);

	@Post("/addfeedback")
	ApiResponse addFeedback(LinkedHashMap<String, String> items) throws RestClientException;

	@Get("/VideoRecord?data_id={data_id}&start_date={start_date}&end_start={end_start}")
	ApiResponse VideoRecord(String data_id, String start_date, String end_start) throws RestClientException;
	
	@Get("/getAlarmChartsData")
	ApiResponse getAlarmChartsData() throws RestClientException;

	@Post("/modifyuserinfo")
	ApiResponse modifyUserInfo(LinkedHashMap<String, String> items) throws RestClientException;

	@Post("/modifyuserimage")
	@RequiresHeader({ "Authorization", "Content-Type" })
	ApiResponse modifyuserimage(MultiValueMap<String, Object> multiValueMap) throws RestClientException;

	@Get(URLs.UPDATE_VERSION)
	@RequiresHeader(value = {})
	Update getUpdateInfo() throws RestClientException;

	@Get("/getmessage?msgtype={msgType}")
	ApiResponse getMessage(String msgType) throws RestClientException;

	// 预告警处理
	@Post("/postPreAlarmList")
	ApiResponse postPreAlarmList(LinkedHashMap<String, String> lastd) throws RestClientException;

	// 上传图片
	@Post("/checkTeamImgs")
	@RequiresHeader({ "Authorization", "Content-Type" })
	ApiResponseUpLoad checkTeamImgs(MultiValueMap<String, Object> multiValueMap);

	// 上传图片
	@Post("/StationImage")
	@RequiresHeader({ "Authorization", "Content-Type" })
	ApiResponseUpLoad StationImage(MultiValueMap<String, Object> multiValueMap);

	// 上传图片
	@Post("/CheckUpload")
	@RequiresHeader({ "Authorization", "Content-Type" })
	ApiResponseUpLoad CheckUpload(MultiValueMap<String, Object> multiValueMap);

	@Post("/SaveUserWork")
	@RequiresHeader({ "Authorization", "Content-Type" })
	ApiResponseUpLoad SaveUserWork(MultiValueMap<String, Object> multiValueMap) throws RestClientException;
	// 上传图片
	@Post("/teamUpload")
	@RequiresHeader({ "Authorization", "Content-Type" })
	ApiResponseUpLoad teamUpload(MultiValueMap<String, Object> multiValueMap);
	
	@Post("/editQuestion")
	@RequiresHeader({ "Authorization", "Content-Type" })
	ApiResponseUpLoad editQuestion(MultiValueMap<String, Object> multiValueMap);
	
	@Post("/uploadMemo")
	@RequiresHeader({ "Authorization", "Content-Type" })
	ApiResponseUpLoad uploadMemo(MultiValueMap<String, Object> multiValueMap);
	
	
	@Post("/updateMemo")
	@RequiresHeader({ "Authorization", "Content-Type" })
	ApiResponseUpLoad updateMemo(MultiValueMap<String, Object> multiValueMap);
	
	// 局站列表
	@Get("/stationList")
	ApiResponse stationList() throws RestClientException;

	// 局站列表
	@Get("/stationList")
	ApiResponseImage stationListImage() throws RestClientException;

	@Post("/creationUser")
	ApiResponse creationUser(LinkedHashMap<String, String> items) throws RestClientException;

	@Get("/newGrouping?GroupingName={GroupingName}&substation_id={substation_id}")
	ApiResponse newGrouping(String GroupingName, String substation_id);

	// 分组
	@Get("/Group?substation_id={substation_id}")
	ApiResponse Group(String substation_id);

	// 删除数据
	@Get("/deleteStation?station_id={station_id}")
	ApiResponse deleteStation(String station_id);

	@Get("/get_spdev_list")
	SPDevResponse get_spdev_list();

	@Get("/get_door_status?data_id={data_id}")
	ApiResponse GetDoorStatus(String data_id);

	@Post("/open_door")
	ApiResponse OpenDoor(DoorOperation op);


}
