package com.chinatelecom.xjdh.linphone;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class ContactManager
{
    public final int MAX_LINES = 10;
    private static ContactManager mInstance;
    private static Object locker = new Object();
    List<Contact> contacts = new ArrayList<>();

    public static ContactManager Instance()
    {
        if (mInstance == null)
        {
            synchronized (locker)
            {
                if (mInstance == null)
                {
                    mInstance = new ContactManager();
                }
            }
        }

        return mInstance;
    }

    private ContactManager()
    {
    }
    public List<Contact> getContacts()
    {
        return contacts;
    }
    public void AddContact(Contact contact)
    {
        contacts.add(contact);
    }

    public void RemoveContact(Contact contact)
    {
        contacts.remove(contact);
    }
    public void RemoveContactAt(int index)
    {
        contacts.remove(index);
    }
    public void RemoveAll()
    {
        contacts.clear();
    }

    public Contact FindContactBySipAddr(String sipAddr)
    {
        if (TextUtils.isEmpty(sipAddr))
            return null;
        for(Contact contact:contacts)
        {
            sipAddr = sipAddr.replaceFirst("sip:","");
            String contactAddr = contact.SipAddr.replaceFirst("sip:","");
            if (sipAddr.equals(contactAddr))
                return contact;
        }
        return null;
    }
}


