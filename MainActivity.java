package com.example.phone;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts.People.Phones;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.R.string;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {

	ArrayList<Map<String, String>>mList;
	Map<String, String> contacts;
	ListView mListView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initview();	
		getContacts();
		initdata();
		setlistner();
	}
	private void setlistner() {
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long arg3) {
				final Map<String, String>mcontacter;
				mcontacter=mList.get(position);
				AlertDialog.Builder builder=new Builder(MainActivity.this);
				builder.setTitle("当前联系人："+mcontacter.get("name")).setMessage("号码："+mcontacter.get("phoneNumber")).setNegativeButton("取消", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
					}
				}).
				setPositiveButton("拨打电话", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						String phoneNumber=mcontacter.get("phoneNumber");
						Intent intent=new Intent(Intent.ACTION_CALL);
						intent.setData(Uri.parse("tel:"+mcontacter.get("phoneNumber")));
						startActivity(intent);
					}
				});
			     builder.create().show();
				
			}
		});
		
	}
	private void initdata() {
		contactAdapter cAdapter=new contactAdapter(MainActivity.this, mList);
		mListView.setAdapter(cAdapter);
		
	}
	private void initview() {
		mListView=(ListView) findViewById(R.id.listView1);
		mList=new ArrayList<Map<String,String>>();
		contacts=new HashMap<String, String>();
	}
	public void getContacts() {
		ContentResolver resolver = getContentResolver();
		
		final String[]PROJECTION = new String[] {  
		       Phone.DISPLAY_NAME, Phone.NUMBER, Photo.PHOTO_ID,Phone.CONTACT_ID }; 
		Cursor c = resolver.query(Phone.CONTENT_URI, PROJECTION, null, null, null);
		System.out.println(c==null);
		if(c!=null)
		{
				while(c.moveToNext())
		{
			contacts=new HashMap<String, String>();
			String phoneNumber=c.getString(c.getColumnIndex((ContactsContract.CommonDataKinds.Phone.NUMBER)));
			if (TextUtils.isEmpty(phoneNumber))  
		        continue; 
			String Name=c.getString(c.getColumnIndex(Phone.DISPLAY_NAME));
			String pId=c.getLong(c.getColumnIndex(Phone.PHOTO_ID))+"";
			String cId=c.getLong(c.getColumnIndex(Phone.CONTACT_ID))+"";
			contacts.put("name", Name);
		//	System.out.println(contacts.get("name"));
			
			contacts.put("phoneNumber", phoneNumber);
			contacts.put("pId",pId);
			contacts.put("cId",cId);
			mList.add(contacts);
			
			
		}System.out.println(mList.get(0).get("name")+"23232323232323");
				c.close();
		}
	}
	class contactAdapter extends BaseAdapter{

		Context context;
		ArrayList<Map<String, String>>theList;
		
		public contactAdapter(Context context,
				ArrayList<Map<String, String>> theList) {
			super();
			this.context = context;
			this.theList = theList;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return theList.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View view, ViewGroup arg2) {
			if(theList.size()!=0)
			{
				view=View.inflate(MainActivity.this, R.layout.item, null);
				Map<String, String>theContacer;
				theContacer=theList.get(position);
				String name=(String) theContacer.get("name");
				System.out.println(name);
				String phoneNumber=theContacer.get("phoneNumber");
				long pid=Long.parseLong(theContacer.get("pId"));
				long cid=Long.parseLong(theContacer.get("cId"));
				Bitmap pBitmap=null;
				if(pid>0)
				{/*
				为存在联系人图片的位置增加图片
				*/
					Uri uri=ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, cid);
					InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(context.getContentResolver(), uri);
					pBitmap=BitmapFactory.decodeStream(input);
				}else {
					pBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.lxrtx); 
				}
				ImageView photo;
				photo=(ImageView) view.findViewById(R.id.imageView1);
				photo.setImageBitmap(pBitmap);
				//StringBuilder sb=new StringBuilder();
				//sb.append("姓名：     ").append(name).append("\n").append("电话号码：").append(phoneNumber).append("\n").append(""+cid);
				TextView mTextView=(TextView) view.findViewById(R.id.textView2);
				mTextView.setText("姓名："+name);
				TextView sTextView=(TextView) view.findViewById(R.id.textView3);
				sTextView.setText("电话号码："+phoneNumber);
				TextView tTextView=(TextView) view.findViewById(R.id.textView4);
				tTextView.setText("id:"+""+cid);
				return view;
			}
		  return null;
		}
		
	}

}
