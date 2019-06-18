package com.chinatelecom.xjdh.linphone;


public class Session
{
	public static int INVALID_SESSION_ID = -1;
	public String Remote;
	public String DisplayName;
	public boolean HasVideo;
	public long SessionID;
	public boolean Hold;

	public boolean Mute;
	public CALL_STATE_FLAG state;

	public boolean IsIdle()
	{
		return state == CALL_STATE_FLAG.FAILED || state == CALL_STATE_FLAG.CLOSED;
	}
	public Session()
	{
		Remote = null;
		DisplayName = null;
		HasVideo = false;
		SessionID = INVALID_SESSION_ID;
		state = CALL_STATE_FLAG.CLOSED;
	}

	public void Reset()
	{
		Remote = null;
		DisplayName = null;
		HasVideo = false;
		SessionID = INVALID_SESSION_ID;
		state = CALL_STATE_FLAG.CLOSED;
	}

	public enum CALL_STATE_FLAG
	{
		INCOMING,
		TRYING ,
		CONNECTED,
		FAILED,
		CLOSED,
	}
}


