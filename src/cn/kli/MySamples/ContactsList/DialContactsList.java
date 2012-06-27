package cn.kli.MySamples.ContactsList;

import android.content.Context;
import android.content.Intent;
import android.database.CharArrayBuffer;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.QuickContactBadge;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;
import cn.kli.MySamples.R;

public class DialContactsList extends LinearLayout {
	
	private static final String FILTERMIDDLE_PARAMETER = "filtermiddle";
	
    protected static class PhoneQuery {
        private static final String[] PROJECTION_PRIMARY = new String[] {
            Phone._ID,                          // 0
            Phone.TYPE,                         // 1
            Phone.LABEL,                        // 2
            Phone.NUMBER,                       // 3
            Phone.CONTACT_ID,                   // 4
            Phone.LOOKUP_KEY,                   // 5
            Phone.PHOTO_ID,                     // 6
            Phone.DISPLAY_NAME_PRIMARY,         // 7
        };

        private static final String[] PROJECTION_ALTERNATIVE = new String[] {
            Phone._ID,                          // 0
            Phone.TYPE,                         // 1
            Phone.LABEL,                        // 2
            Phone.NUMBER,                       // 3
            Phone.CONTACT_ID,                   // 4
            Phone.LOOKUP_KEY,                   // 5
            Phone.PHOTO_ID,                     // 6
            Phone.DISPLAY_NAME_ALTERNATIVE,     // 7
        };

        public static final int PHONE_ID           = 0;
        public static final int PHONE_TYPE         = 1;
        public static final int PHONE_LABEL        = 2;
        public static final int PHONE_NUMBER       = 3;
        public static final int PHONE_CONTACT_ID   = 4;
        public static final int PHONE_LOOKUP_KEY   = 5;
        public static final int PHONE_PHOTO_ID     = 6;
        public static final int PHONE_DISPLAY_NAME = 7;
    }
    
	private Context mContext;
	private ListView mListView;
	private TextView mNoDataTextView;
	private ContactPhotoManager mContactPhotoManager;
	private DialContactsListAdapter mAdapter;
    
    public abstract class ContactsOnClickListener implements OnItemClickListener{
    	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3){
			Cursor c = (Cursor)mAdapter.getItem(position);
			String number = c.getString(PhoneQuery.PHONE_NUMBER);
			onContactSelect(number);
    	}
    	//help client to get number;
    	public abstract void onContactSelect(String number);
    }
    
    private ContactsOnClickListener mListener = new ContactsOnClickListener(){

		@Override
		public void onContactSelect(String number) {
			Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel://"+ number));
			mContext.startActivity(intent);
		}
    	
    };
    

	public DialContactsList(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		LayoutInflater inflater = LayoutInflater.from(mContext);
		View view = inflater.inflate(R.layout.contacts_query_list, this,true);
		mListView = (ListView)view.findViewById(R.id.contactsListview);
		mNoDataTextView = (TextView)view.findViewById(R.id.noDataView);
		mContactPhotoManager = ContactPhotoManager.getInstance(mContext);
		updateData(null);
		mListView.setOnItemClickListener(mListener);
	}
	
	public void updateQueryString(String query){
		updateData(removeNotNumber(query));
	} 
	
	private void updateData(String selection) {
		Uri.Builder builder;
		if (TextUtils.isEmpty(selection)) {
			builder = Phone.CONTENT_URI.buildUpon();
		} else {
			builder = Phone.CONTENT_FILTER_URI.buildUpon();
			builder.appendPath(selection);
			builder.appendQueryParameter(FILTERMIDDLE_PARAMETER, "true");
		}
		Cursor c = mContext.getContentResolver().query(builder.build(),
				PhoneQuery.PROJECTION_PRIMARY, selection, null,
				Phone.DISPLAY_NAME + " COLLATE LOCALIZED ASC");
		if (c.getCount() == 0) {
			mNoDataTextView.setVisibility(View.VISIBLE);
			mListView.setVisibility(View.GONE);
		} else if (c.getCount() != 0) {
			mNoDataTextView.setVisibility(View.GONE);
			mListView.setVisibility(View.VISIBLE);
			mAdapter = new DialContactsListAdapter(mContext,
					R.layout.contacts_list_item, c);
			mListView.setAdapter(mAdapter);
		}
	}
	
	//only use numberic char
	private static String removeNotNumber(String query){
		final char[] NUMBER = {'1','2','3','4','5','6','7','8','9','0'};
		StringBuilder sb = new StringBuilder();
		char[] ch = query.toCharArray();
		for(char c: ch){
			for(int i =0;i < NUMBER.length; i++){
				System.out.println("c = "+c+" : "+NUMBER[NUMBER.length - 1]);
				if(c == NUMBER[i]){
					sb.append(c);
					break;
				}
			}
		}
		return sb.toString();
	}
	
	
	
	//Contact list adapter
	private class DialContactsListAdapter extends ResourceCursorAdapter{

		public DialContactsListAdapter(Context context, int layout, Cursor c) {
			super(context, layout, c);
		}

		
		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			View view = super.newView(context, cursor, parent);
			ContactListItemCache cache = new ContactListItemCache();
			cache.photoView = (QuickContactBadge)(view.findViewById(R.id.contact_photo));
			cache.nameView = (TextView)(view.findViewById(R.id.contact_name));
			cache.numberView = (TextView)(view.findViewById(R.id.contact_number));
			cache.tabelView = (TextView)(view.findViewById(R.id.contact_type));
			view.setTag(cache);
			return view;
		}


		@Override
		public void bindView(View view, Context context, Cursor cursor) {

            final ContactListItemCache cache = (ContactListItemCache) view.getTag();
            final String phoneNumber = cursor.getString(PhoneQuery.PHONE_NUMBER);
            final long contactId = cursor.getLong(PhoneQuery.PHONE_CONTACT_ID);
            final String lookupKey = cursor.getString(PhoneQuery.PHONE_LOOKUP_KEY);
            final int photoId = cursor.getInt(PhoneQuery.PHONE_PHOTO_ID);
            final String label = cursor.getString(PhoneQuery.PHONE_TYPE);
            final int type = cursor.getInt(PhoneQuery.PHONE_TYPE);
            
            // Set the name
            cursor.copyStringToBuffer(PhoneQuery.PHONE_DISPLAY_NAME, cache.nameBuffer);
            int size = cache.nameBuffer.sizeCopied;
            cache.nameView.setText(cache.nameBuffer.data, 0, size);
            
            //set photo
            cache.photoView.assignContactFromPhone(phoneNumber, true);
            mContactPhotoManager.loadPhoto(cache.photoView, photoId, false, true);
            
            //set phone number
            cache.numberView.setText(phoneNumber);
            
            //set phone tab
            CharSequence labelDisplay = Phone.getTypeLabel(mContext.getResources(),type, label);
            cache.tabelView.setText(labelDisplay);
		}
		
		final class ContactListItemCache {
	        public TextView nameView, numberView, tabelView;
	        public QuickContactBadge photoView;
	        public CharArrayBuffer nameBuffer = new CharArrayBuffer(128);
	    }
		
		
	}

}
