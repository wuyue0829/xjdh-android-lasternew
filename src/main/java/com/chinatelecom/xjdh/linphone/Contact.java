package com.chinatelecom.xjdh.linphone;

public class Contact
{
    public String SubRequestDescription;
    public enum SUBSCRIBE_STATE_FLAG
    {
        UNSETTLLED,
        ACCEPTED ,
        REJECTED,
        UNSUBSCRIBE,
    }
    public String SipAddr;
    public String SubDescription;
    public boolean SubscribRemote;

    public long SubId;//if SubId >0 means received remote subscribe
    public SUBSCRIBE_STATE_FLAG state; // weigher accepte remote subscribe

    public String currentStatusToString()
    {
        String status = "";

        status += "Subscribe："+ SubscribRemote;
        status += "  Remote presence is：" + SubDescription;


        status += " Subscription received:("+ SubRequestDescription+")";
        switch (state)
        {
            case ACCEPTED:
                status += "Accepted";
                break;
            case REJECTED:
                status += "Rejected";
                break;
            case UNSETTLLED:
                status += "Pending";
                break;
            case UNSUBSCRIBE:
                status += "Not subscripted";
                break;
        }

        return status;
    }
    public Contact()
    {
        state = SUBSCRIBE_STATE_FLAG.UNSUBSCRIBE;//Not being subscripted
        SubscribRemote = false;//Not subscripted
        SubId = 0;
    }

}


