package com.firebaseauth;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnSave;
    private Button logout;
    private EditText textName;
    private EditText textNumber;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private ListView listViewContacts;
    private List<UserContact> contactList;


    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser user = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child(user.getUid());

        btnSave = (Button)findViewById(R.id.btnSave);
        logout = (Button)findViewById(R.id.logout);
        textName = (EditText)findViewById(R.id.textName);
        textNumber = (EditText)findViewById(R.id.textNumber);
        listViewContacts = (ListView)findViewById(R.id.listViewContacts);
        contactList = new ArrayList<>();
        btnSave.setOnClickListener(this);

        listViewContacts.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                UserContact user = contactList.get(i);

                showUpdateDialog (user.getContact(), user.getName());
                return false;
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                contactList.clear();
                for(DataSnapshot contactSnapshot : dataSnapshot.getChildren()){
                    UserContact user = contactSnapshot.getValue(UserContact.class);

                    contactList.add(user);
                }

                ContactList adapter = new ContactList(ProfileActivity.this, contactList);
                listViewContacts.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void showUpdateDialog(String ContactNumber, String ContactName){

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.update_dialog, null);

        dialogBuilder.setView(dialogView);

        final TextView txtName = (TextView) dialogView.findViewById(R.id.txtName);
        final EditText editName = (EditText)dialogView.findViewById(R.id.editName);
        final TextView txtCnt = (TextView) dialogView.findViewById(R.id.txtCnt);
        final EditText editCnt = (EditText)dialogView.findViewById(R.id.editCnt);
        final Button btnUpdate = (Button)dialogView.findViewById(R.id.btnUpdate);
        dialogBuilder.setTitle("Updating Contact "+ContactName);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();


        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editName.getText().toString().trim();
                String contact = editCnt.getText().toString().trim();

                if(TextUtils.isEmpty(name)){
                    editName.setError("Name Required");
                    return;
                }

                if(TextUtils.isEmpty(contact)){
                    editCnt.setError("Contact Name Required");
                    return;
                }
                updateArtist(name, contact);
                alertDialog.dismiss();
            }
        });



    }



    private void updateArtist(String name, String contact){

        FirebaseUser user = firebaseAuth.getCurrentUser();
        String id = databaseReference.push().getKey();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(user.getUid()).child(id);
        UserContact userContact = new UserContact(name, contact);
        databaseReference.setValue(userContact);
    }

    private void saveContact(){
        String name = textName.getText().toString().trim();
        String contact = textNumber.getText().toString().trim();


        UserContact userContact = new UserContact(name, contact);
        String id = databaseReference.push().getKey();
        databaseReference.child(id).setValue(userContact).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ProfileActivity.this, "Contact Saved Successfully...", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(ProfileActivity.this, "Some Error occurred while savng the contact!", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public void onClick(View view) {
        if(view == btnSave){
            saveContact();
        }

        if(view == logout){
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

    }


}
