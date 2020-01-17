package com.example.fulltaskc4i;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.fulltaskc4i.Adapters.PersonRecyclerAdapter;
import com.example.fulltaskc4i.ModelsPackage.PersonModel;
import com.example.fulltaskc4i.SQLiteDBPackage.PersonDBManager;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;


public class InsertOrUpdatePersonFragment extends DialogFragment {

    private PersonDBManager personDBManager;

    private EditText edtPersonName, edtPersonSaying, edtPersonInfo;
    private ImageView personImage;
    private Button btnAddOrUpdatePerson;

    private int personId;
    private String personName, personSaying, personInfo;
    private String whatToDo;
    private String UPDATE_PERSON = "Update Person";
    private String ADD_PERSON = "Add Person";
    private ArrayList<PersonModel> personList;
    private int position;
    private byte[] personImageBytes;

    private int PICK_IMAGE_REQUEST = 1;
    private boolean isDBAltered;

    private View.OnClickListener addOrUpdateClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            getViewsData();
            if (!isViewsEmpty()) {
                if (whatToDo.equals(UPDATE_PERSON)) {
                    updatePerson();
                } else if (whatToDo.equals(ADD_PERSON)) {
                    insertPerson();
                }
            }
            dismiss();
        }
    };

    private View.OnClickListener showFileChooser = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
        }
    };

    private boolean isViewsEmpty() {
        return personName.isEmpty()&& personSaying.isEmpty()&& personInfo.isEmpty();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_person_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        personDBManager = new PersonDBManager(getContext()).open();
        bundleData();
        isDBAltered = false;
    }

    private void bundleData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            whatToDo = bundle.getString("whatToDo");
            if (whatToDo != null && whatToDo.equals(UPDATE_PERSON)) {
                btnAddOrUpdatePerson.setText(UPDATE_PERSON);
                personId = bundle.getInt("personId");
                position = bundle.getInt("position");
                personList = (ArrayList<PersonModel>) bundle.getSerializable("personList");
                PersonModel person = personDBManager.getPerson(personId);
                setViewsData(person);
            } else if (whatToDo != null && whatToDo.equals(ADD_PERSON)) {
                btnAddOrUpdatePerson.setText(ADD_PERSON);
                personList = (ArrayList<PersonModel>) bundle.getSerializable("personList");
            }
        }
    }

    private void initViews(View view) {
        edtPersonName = view.findViewById(R.id.edtPersonName);
        edtPersonSaying = view.findViewById(R.id.edtPersonSaying);
        edtPersonInfo = view.findViewById(R.id.edtPersonInfo);
        personImage = view.findViewById(R.id.personImage);
        personImage.setOnClickListener(showFileChooser);
        btnAddOrUpdatePerson = view.findViewById(R.id.btnAddOrUpdatePerson);
        btnAddOrUpdatePerson.setOnClickListener(addOrUpdateClickListener);
    }

    private void setViewsData(PersonModel person) {
        edtPersonName.setText(person.getPersonName());
        edtPersonSaying.setText(person.getPersonSaying());
        edtPersonInfo.setText(person.getPersonInfo());
        if (person.getPersonImage()!=null){
            personImage.setImageBitmap(bytesToImage(person.getPersonImage()));
        }
    }

    private void getViewsData() {
        personName = edtPersonName.getText().toString();
        personSaying = edtPersonSaying.getText().toString();
        personInfo = edtPersonInfo.getText().toString();
        personImageBytes = imageToBytes(personImage);

    }

    private Bitmap bytesToImage(byte[] imageBytes) {
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    }

    private void insertPerson() {
        PersonModel personModel = new PersonModel(0, personName, personSaying, personInfo, personImageBytes);
        personDBManager.addPerson(personModel);
        isDBAltered = true;
        Log.i("diss", "in insert");
    }

    private void updatePerson() {
        personDBManager.updatePerson(new PersonModel(personId, personName, personSaying, personInfo, personImageBytes));
        isDBAltered = true;
    }

    private byte[] imageToBytes(ImageView personImage) {
        if (personImage.getDrawable()==null) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) personImage.getDrawable();
            Bitmap bitmap = bitmapDrawable.getBitmap();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            return outputStream.toByteArray();
        } else {
            return null;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            if (data != null) {
                Uri postImgPath = data.getData();
                personImage.setImageURI(postImgPath);
            }
        }
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if (isDBAltered) {
            if (whatToDo != null && whatToDo.equals(UPDATE_PERSON)) {
                personList.set(position, personDBManager.getPerson(personId));
                PersonRecyclerAdapter.adapter.notifyItemChanged(position);
            } else if (whatToDo != null && whatToDo.equals(ADD_PERSON)) {
                PersonModel lastPerson=personDBManager.getLastPerson();
                if (lastPerson!=null){
                    personList.add(lastPerson);
                    PersonRecyclerAdapter.adapter.notifyItemInserted(personList.size() - 1);
                }

            }
        }
    }

}
