package ekyc.itsutra.ekyc;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.R.attr.data;

public class Act_Form extends AppCompatActivity {
    private static final String SERVER_URL="http://192.168.1.205/das/Restserver/api/Sharing/newImage";
    private Spinner msp_occupation;
    private Spinner msp_education_level;
    private Spinner msp_districts;
    private Spinner msp_permanent_address_districts;
    private Spinner msp_vdc_municipality;
    private Spinner msp_ward;
    private DatePicker mcal_date_of_issue;
    private CalendarView mcal_date_of_birth;
    private TextView mtxt_date_of_issue_val;
    private TextView mtxt_date_of_birth_val;
   private Button img_save;
    private ImageButton event_pa;
    private Bitmap bmp;
    private Uri fileUri;
private String imageFileName;
    int year_x,month_x,day_x;
    static final int DIALOG_DOB = 0;
    static final int DIALOG_DATE_OF_ISSUE = 1;
private ProgressDialog dialog;
private ImageView img_photo,img_agrement,img_citizen;
String photopath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        final Calendar cal = Calendar.getInstance();
        year_x = cal.get(Calendar.YEAR);
        month_x = cal.get(Calendar.DAY_OF_MONTH);
        day_x = cal.get(Calendar.DAY_OF_MONTH);

        mtxt_date_of_birth_val = (TextView) findViewById(R.id.txt_date_of_birth_val);
        mtxt_date_of_issue_val = (TextView) findViewById(R.id.txt_date_of_issue_val);
        //Button

        event_pa = (ImageButton) findViewById(R.id.evt_pa_image);
        event_pa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                personal_agree();
            }
        });

                    img_save = (Button) findViewById(R.id.btn_Send_Photo );
                        img_save.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                     upload_image();

                            }
                        });


      img_photo = (ImageView) findViewById(R.id.iv_photograph);
        img_agrement = (ImageView) findViewById(R.id.iv_pa_agreement);
        img_citizen = (ImageView) findViewById(R.id.iv_citizenship);
        /* Populating Spinner for the
        occupation in the EYKC form
         */
        final HashMap<Integer, String> occupation = new HashMap<Integer, String>();
        occupation.put(1, "Student");
        occupation.put(2, "Doctor");
        occupation.put(3, "Nurse");
        occupation.put(4, "Engineer");
        occupation.put(5, "Housewife");
        occupation.put(6, "Teacher");

        msp_occupation = (Spinner) findViewById(R.id.sp_occupation);

        //Create ArrayList collection using StringWithTag instead of String
        List<Class_StringWithTag> occupation_list = new ArrayList<Class_StringWithTag>();

        //Iterate through the original collection
        for (Map.Entry<Integer, String> entry : occupation.entrySet()) {
            Integer key = entry.getKey();
            String value = entry.getValue();

            occupation_list.add(new Class_StringWithTag(value, key));
        }

        ArrayAdapter<Class_StringWithTag> occupation_adapter = new ArrayAdapter<Class_StringWithTag>(this, android.R.layout.simple_spinner_dropdown_item, occupation_list);
        occupation_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        msp_occupation.setAdapter(occupation_adapter);

        msp_occupation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Class_StringWithTag swt = (Class_StringWithTag) parent.getItemAtPosition(position);
                Integer key = (Integer) swt.tag;
                Toast.makeText(Act_Form.this, String.valueOf(occupation.get(key)), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /* Populating spinner for
        education level in the EKYC Form
         */

        final HashMap<Integer, String> education_level = new HashMap<Integer, String>();
        education_level.put(1, "SLC");
        education_level.put(2, "+2");
        education_level.put(3, "Bachelor");
        education_level.put(4, "Masters");
        education_level.put(5, "Ph.d");


        msp_education_level = (Spinner) findViewById(R.id.sp_education_level);

        //Create ArrayList collection using StringWithTag instead of String
        List<Class_StringWithTag> education_level_list = new ArrayList<Class_StringWithTag>();

        //Iterate through the original collection
        for (Map.Entry<Integer, String> entry : education_level.entrySet()) {
            Integer key = entry.getKey();
            String value = entry.getValue();
            education_level_list.add(new Class_StringWithTag(value, key));
        }

        ArrayAdapter<Class_StringWithTag> education_level_adapter = new ArrayAdapter<Class_StringWithTag>(this, android.R.layout.simple_spinner_dropdown_item, education_level_list);
        education_level_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        msp_education_level.setAdapter(education_level_adapter);

        msp_education_level.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Class_StringWithTag swt = (Class_StringWithTag) parent.getItemAtPosition(position);
                Integer key = (Integer) swt.tag;
                Toast.makeText(Act_Form.this, String.valueOf(education_level.get(key)), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        /* Populating spinner sp_district
        in EKYC form for district
         */


        final HashMap<Integer, String> hmDistricts = new HashMap<Integer, String>();
        hmDistricts.put(1, "Kathmandu");
        hmDistricts.put(2, "Lalitpur");
        hmDistricts.put(3, "Bhaktapur");
        hmDistricts.put(4, "Rasuwa");
        hmDistricts.put(5, "Dhading");


        msp_districts = (Spinner) findViewById(R.id.sp_district);
        msp_permanent_address_districts = (Spinner) findViewById(R.id.sp_permanent_district);

        //Create ArrayList collection using StringWithTag instead of String
        List<Class_StringWithTag> district_list = new ArrayList<Class_StringWithTag>();

        //Iterate through the original collection
        for (Map.Entry<Integer, String> entry : hmDistricts.entrySet()) {
            Integer key = entry.getKey();
            String value = entry.getValue();
            district_list.add(new Class_StringWithTag(value, key));
        }

        ArrayAdapter<Class_StringWithTag> district_adapter = new ArrayAdapter<Class_StringWithTag>(this, android.R.layout.simple_spinner_dropdown_item, district_list);
        district_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        msp_districts.setAdapter(district_adapter);
        msp_permanent_address_districts.setAdapter(district_adapter);

        msp_districts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Class_StringWithTag swt = (Class_StringWithTag) parent.getItemAtPosition(position);
                Integer key = (Integer) swt.tag;
                Toast.makeText(Act_Form.this, String.valueOf(hmDistricts.get(key)), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        msp_permanent_address_districts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Class_StringWithTag swt = (Class_StringWithTag) parent.getItemAtPosition(position);
                Integer key = (Integer) swt.tag;
                Toast.makeText(Act_Form.this, String.valueOf(hmDistricts.get(key)), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /* Populating Spinner
        for vdc/municipality
         */

        msp_vdc_municipality = (Spinner) findViewById(R.id.sp_permanent_vdc_municipality);
        final HashMap<Integer, String> hmVdcMunicipality = new HashMap<Integer, String>();
        hmVdcMunicipality.put(1, "VDC 1");
        hmVdcMunicipality.put(2, "VDC 2");
        hmVdcMunicipality.put(3, "Municipality 1");
        hmVdcMunicipality.put(4, "Municipality 2");
        hmVdcMunicipality.put(5, "Municipality 3");

        //Create ArrayList collection using StringWithTag instead of String
        List<Class_StringWithTag> vdc_municipality_list = new ArrayList<Class_StringWithTag>();

        //Iterate through the original collection
        for (Map.Entry<Integer, String> entry : hmVdcMunicipality.entrySet()) {
            Integer key = entry.getKey();
            String value = entry.getValue();
            vdc_municipality_list.add(new Class_StringWithTag(value, key));
        }

        ArrayAdapter<Class_StringWithTag> vdc_municipality_adapter = new ArrayAdapter<Class_StringWithTag>(this, android.R.layout.simple_spinner_dropdown_item, vdc_municipality_list);
        vdc_municipality_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        msp_vdc_municipality.setAdapter(district_adapter);


        msp_vdc_municipality.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Class_StringWithTag swt = (Class_StringWithTag) parent.getItemAtPosition(position);
                Integer key = (Integer) swt.tag;
                Toast.makeText(Act_Form.this, String.valueOf(hmVdcMunicipality.get(key)), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


         /* Populating Spinner
        for ward
         */

        msp_ward = (Spinner) findViewById(R.id.sp_permanent_ward_no);
        final HashMap<Integer, String> hmWard = new HashMap<Integer, String>();
        hmWard.put(1, " 1");
        hmWard.put(2, " 2");
        hmWard.put(3, " 3");
        hmWard.put(4, " 4");
        hmWard.put(5, " 5");

        //Create ArrayList collection using StringWithTag instead of String
        List<Class_StringWithTag> ward_list = new ArrayList<Class_StringWithTag>();

        //Iterate through the original collection
        for (Map.Entry<Integer, String> entry : hmWard.entrySet()) {
            Integer key = entry.getKey();
            String value = entry.getValue();
            ward_list.add(new Class_StringWithTag(value, key));
        }

        ArrayAdapter<Class_StringWithTag> ward_adapter = new ArrayAdapter<Class_StringWithTag>(this, android.R.layout.simple_spinner_dropdown_item, ward_list);
        district_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        msp_ward.setAdapter(ward_adapter);


        msp_ward.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Class_StringWithTag swt = (Class_StringWithTag) parent.getItemAtPosition(position);
                Integer key = (Integer) swt.tag;
                Toast.makeText(Act_Form.this, String.valueOf(hmWard.get(key)), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }




    @SuppressWarnings("deprecation")
    public void evt_show_birth_date_calendar(View view) {
        showDialog(DIALOG_DOB);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if(id == DIALOG_DOB)
            return new DatePickerDialog(this, dpDOBListner,year_x,month_x,day_x);
        else if(id == DIALOG_DATE_OF_ISSUE)
            return new DatePickerDialog(this,dpPlaceOfIssue,year_x,month_x,day_x);
        return  null;
    }

    private DatePickerDialog.OnDateSetListener dpDOBListner = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            year_x = year;
            month_x = monthOfYear;
            day_x = dayOfMonth;
            Toast.makeText(Act_Form.this, String.valueOf(year_x), Toast.LENGTH_SHORT).show();
            mtxt_date_of_birth_val.setText(String.valueOf(year_x)+ "/"+String.valueOf(month_x+"/"+String.valueOf(day_x)));

        }
    };

    private DatePickerDialog.OnDateSetListener dpPlaceOfIssue = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            year_x = year;
            month_x = monthOfYear;
            day_x = dayOfMonth;
            Toast.makeText(Act_Form.this, String.valueOf(year_x), Toast.LENGTH_SHORT).show();
            mtxt_date_of_issue_val.setText(String.valueOf(year_x)+ "/"+String.valueOf(month_x+"/"+String.valueOf(day_x)));
        }
    };

//    public void evt_pa_image(View view) {
//        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if(i.resolveActivity(getPackageManager())!=null) {
//            startActivityForResult(i, 2);
//
//        }
//
//    }

    public String getStringImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        Log.d("++++++++++",encodedImage.trim());
        return encodedImage;

    }





    public void evt_photograph(View view) {
        if (getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
     img_agrement.setImageBitmap(null);
            Intent i = new Intent((MediaStore.ACTION_IMAGE_CAPTURE));
            // Ensure that there's a camera activity to handle the intent
            if (i.resolveActivity(getPackageManager()) != null) {
                File photo = null;
                try {
                    // Create the File where the photo should go
                    photo = CreateImageFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // Continue only if the File was successfully created
                if (photo != null) {
                    i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
                    startActivityForResult(i, 3);
                }

            }
    }}


    public void evt_citizenship(View view) {
        if (getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
img_citizen.setImageBitmap(null);
            Intent i = new Intent((MediaStore.ACTION_IMAGE_CAPTURE));
            // Ensure that there's a camera activity to handle the intent
            if (i.resolveActivity(getPackageManager()) != null) {
                File photo = null;
                try {
                    // Create the File where the photo should go
                    photo = CreateImageFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // Continue only if the File was successfully created
                if (photo != null) {
                    i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
                    startActivityForResult(i, 4);
                }

            }
        }
    }

    @SuppressWarnings("deprecation")
    public void evt_show_date_of_issue(View view) {
        showDialog(DIALOG_DATE_OF_ISSUE);
    }

private void personal_agree() {

    if (getApplicationContext().getPackageManager().hasSystemFeature(
            PackageManager.FEATURE_CAMERA)) {
img_photo.setImageBitmap(null);
        Intent i = new Intent((MediaStore.ACTION_IMAGE_CAPTURE));
        // Ensure that there's a camera activity to handle the intent
        if (i.resolveActivity(getPackageManager()) != null) {
            File photo = null;
            try {
                // Create the File where the photo should go
                photo = CreateImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photo != null) {
                i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
                startActivityForResult(i, 2);
            }

        }
    }
}
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode==RESULT_OK) {
//            bmp = (Bitmap) data.getExtras().get("data");
////                //Setting the Bitmap to ImageView
//                img_photo.setImageBitmap(bmp);
            setPic();
        }
    if(requestCode==3&& resultCode==RESULT_OK){
        setPic1();
    }
        if(requestCode==4 && resultCode==RESULT_OK){
            setPic2();
        }

//            b = data.getExtras();
//            bmp = (Bitmap) b.get("data");
//            ImageView img = (ImageView) findViewById(R.id.iv_photograph);


//            } catch (IOException e) {
//                e.printStackTrace();

//
//            if (requestCode == 3 && resultCode == RESULT_OK) {
////            b = data.getExtras();
////
////            bmp = (Bitmap) b.get("data");
////
////            img_agrement.setImageBitmap(bmp);
//            }
//
//            if (requestCode == 4 && resultCode == RESULT_OK) {
//            b = data.getExtras();
//
//            bmp = (Bitmap) b.get("data");
//
//            img_citizen.setImageBitmap(bmp);

    }





    private void upload_image() {
        dialog.setMessage("Uploading Image..");
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,SERVER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
                img_agrement.setImageBitmap(null);
                img_citizen.setImageBitmap(null);
                img_photo.setImageBitmap(null);
                dialog.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

               Toast.makeText(getApplicationContext(), error.getMessage().toString(), Toast.LENGTH_LONG).show();
                Log.d("++", String.valueOf(error));

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String image = getStringImage(bmp);
                Map<String, String> params = new HashMap<>();
                params.put("image", image);
                params.put("name",imageFileName);

                return params;
            }
        };
//        AppController.getInstance().addToRequestQueue(stringRequest);
        RequestQueue rq = Volley.newRequestQueue(Act_Form.this);
        rq.add(stringRequest);
    }
    private void setPic() {
        int targetW = img_photo.getWidth();
        int targetH = img_photo.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photopath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;
//
        bmp = BitmapFactory.decodeFile(photopath, bmOptions);
        img_photo.setImageBitmap(bmp);
    }

    private File CreateImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        photopath = image.getAbsolutePath();
        Log.e("Getpath", "Cool" + photopath);
        return image;

    }
    private void setPic1() {
        int targetW = img_agrement.getWidth();
        int targetH = img_agrement.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photopath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;
//
        bmp = BitmapFactory.decodeFile(photopath, bmOptions);
        img_agrement.setImageBitmap(bmp);
    }

    private void setPic2() {
        int targetW = img_citizen.getWidth();
        int targetH = img_citizen.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photopath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;
//
        bmp = BitmapFactory.decodeFile(photopath, bmOptions);
        img_citizen.setImageBitmap(bmp);
    }

}