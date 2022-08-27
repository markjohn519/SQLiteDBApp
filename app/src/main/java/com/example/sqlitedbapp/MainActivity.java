package com.example.sqlitedbapp;


import android.app.AlertDialog.Builder;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.os.Bundle;
import android.app.Activity;

public class MainActivity extends  Activity implements OnClickListener{

    private EditText etStdntID, etStdntName, etStdntProg;
    private Button btAdd, btDelete, btSearch, btView;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etStdntID=findViewById(R.id.tfid);
        etStdntName=findViewById(R.id.tfname);
        etStdntProg=findViewById(R.id.tfprogram);

        btAdd=findViewById(R.id.btnadd);
        btDelete=findViewById(R.id.btndelete);
        btSearch=findViewById(R.id.btnsearch);
        btView=findViewById(R.id.btnviewall);

        btAdd.setOnClickListener(this);
        btDelete.setOnClickListener(this);
        btSearch.setOnClickListener(this);
        btView.setOnClickListener(this);

        db = openOrCreateDatabase("StudentDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS student(stdnt_id VARCHAR, stdnt_name VARCHAR, stdnt_prog VARCHAR);");
    }

    public void ClearText(){
        etStdntID.getText().clear();
        etStdntProg.getText().clear();
        etStdntName.getText().clear();
        etStdntID.requestFocus();
    }

    public void showMessage(String Title, String Message){
        Builder builder = new Builder(this);
        builder.setCancelable(true);
        builder.setTitle(Title);
        builder.setMessage(Message);
        builder.show();
    }

    public void onClick(View view){
        if(view == btAdd){
            db.execSQL("INSERT INTO student VALUES('" +etStdntID.getText()+ "','" +etStdntName.getText()+ "'," +
                    "'" +etStdntProg.getText()+ "')");
            showMessage("Success", "Record added.");
            ClearText();
        }
        else if (view == btDelete){
            Cursor c = db.rawQuery("SELECT * FROM student WHERE stdnt_ID='"+ etStdntID.getText()+ "'",
                    null);

            if(c.moveToFirst()){
                db.execSQL("DELETE FROM student WHERE stdnt_ID = '" + etStdntID.getText() + "'");
                showMessage("Success","Record Deleted");
            }
            ClearText();
        }
        else if(view == btSearch){
            Cursor c = db.rawQuery("SELECT * FROM student WHERE stdnt_ID = '" + etStdntID.getText() + "'",
                    null );
            StringBuffer buffer = new StringBuffer();

            if(c.moveToFirst()){
                buffer.append("Name: " + c.getString(1) + "\n");
                buffer.append("Program" + c.getString(2) + "\n");
            }
            //Displaying all records
            showMessage("Student Details", buffer.toString());
        }
        else if(view == btView){
            Cursor c = db.rawQuery("SELECT * FROM student",null);

            if(c.getCount() == 0){
                showMessage("Error","No Records Found");
                return;
            }

            StringBuffer buffer = new StringBuffer();
            while(c.moveToNext()){
                buffer.append("ID: "+ c.getString(0)+ "\n");
                buffer.append("Name: "+ c.getString(1)+ "\n");
                buffer.append("Program: "+ c.getString(2)+ "\n");
            }
            showMessage("Student Details", buffer.toString());
        }
    }


}