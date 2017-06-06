package com.example.aprivate.test_contact;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MAIN>>>>>>>: ";
    private Button myButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myButton = (Button) findViewById(R.id.btn);
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                test();
            }
        });
    }

    void test() {
        Contact contact;
        Cursor cursor=getApplicationContext().getContentResolver().query(
                ContactsContract.Contacts.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        if(cursor.getCount() > 0)
        {
            while(cursor.moveToNext())
            {
                contact = new Contact();
                final String id=cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                contact.setId(id);
                String name=cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                contact.setContactName(name);
                if(Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0)
                {
                    Log.i(TAG, "Contact name=" + name + ", Id=" + id);
                    // get the phone number
                    Cursor pCur=getApplicationContext().getContentResolver().query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id},
                            null);
                    while(pCur.moveToNext())
                    {
                        String phone=pCur.getString(
                                pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        contact.setPhoneNumber(phone);
                        Log.i(TAG, "phone=" + phone);
                    }
                    pCur.close();
                }
            }
        }
    }
}
