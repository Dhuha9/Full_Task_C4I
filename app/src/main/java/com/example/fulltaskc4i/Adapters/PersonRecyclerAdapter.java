package com.example.fulltaskc4i.Adapters;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fulltaskc4i.PersonDetailsActivity;
import com.example.fulltaskc4i.InsertOrUpdatePersonFragment;
import com.example.fulltaskc4i.ModelsPackage.PersonModel;
import com.example.fulltaskc4i.R;
import com.example.fulltaskc4i.SQLiteDBPackage.PersonDBManager;

import java.util.ArrayList;


public class PersonRecyclerAdapter extends RecyclerView.Adapter<PersonRecyclerAdapter.PersonViewHolder> {

    public static PersonRecyclerAdapter adapter;
    private Context context;
    private ArrayList<PersonModel> personList;
    private FragmentManager fragmentManager;
    private ArrayList<PersonModel> baseResource;

    public PersonRecyclerAdapter(Context context, ArrayList<PersonModel> personList, FragmentManager fragmentManager) {
        this.context = context;
        this.personList = personList;
        this.fragmentManager = fragmentManager;
        baseResource = personList;
    }

    @NonNull
    @Override
    public PersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View personView = LayoutInflater.from(parent.getContext()).inflate(R.layout.person_recycler_item, parent, false);
        adapter = this;
        return new PersonViewHolder(personView, context);
    }

    @Override
    public void onBindViewHolder(@NonNull final PersonViewHolder holder, final int position) {
        final PersonModel currentItem = personList.get(position);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Pair[] pairs = new Pair[2];
                pairs[0] = new Pair<View, String>(holder.txtPersonName, "sharedPersonName");
                pairs[1] = new Pair<View, String>(holder.personImage, "sharedPersonImage");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) context, pairs);
                Intent intent = new Intent(context, PersonDetailsActivity.class);
                intent.putExtra("name", currentItem.getPersonName());
                intent.putExtra("saying", currentItem.getPersonSaying());
                intent.putExtra("image", currentItem.getPersonImage());

                view.getContext().startActivity(intent, options.toBundle());
            }
        });


        holder.txtPersonName.setText(currentItem.getPersonName());
        holder.txtPersonSaying.setText(currentItem.getPersonSaying());
        if (currentItem.getPersonImage()!=null){
            holder.personImage.setImageBitmap(bytesToImage(currentItem.getPersonImage()));
        }

        holder.icEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                InsertOrUpdatePersonFragment fragment = new InsertOrUpdatePersonFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("personId", currentItem.getId());
                bundle.putString("whatToDo", "Update Person");
                bundle.putInt("position", position);
                bundle.putSerializable("personList", personList);

                fragment.setArguments(bundle);
                fragment.show(fragmentManager, null);
            }
        });

        holder.icDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showDeleteAlertDialog(currentItem.getId(), position);
            }
        });
    }


    private Bitmap bytesToImage(byte[] imageBytes) {
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    }

    private void showDeleteAlertDialog(final int personId, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("title");
        builder.setMessage("Are you sure you want to delete this person?");
        builder.setIcon(R.drawable.ic_delete_alert);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                PersonDBManager sqLiteHelper = new PersonDBManager(context).open();
                sqLiteHelper.deletePerson(personId);
                personList.remove(position);
                adapter.notifyItemRemoved(position);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public int getItemCount() {
        return personList.size();
    }

    public void searchPerson(String newText) {
        personList = new ArrayList<>();
        if (newText.isEmpty()) {
            personList = baseResource;
        } else {
            for (PersonModel person : baseResource) {
                if (person.getPersonName().toLowerCase().trim().contains(newText.toLowerCase().trim())) {
                    personList.add(person);
                }
            }
        }
        notifyDataSetChanged();
    }

    static class PersonViewHolder extends RecyclerView.ViewHolder {
        Context context;
        private TextView txtPersonName, txtPersonSaying;
        private ImageView personImage, icDelete, icEdit;


        PersonViewHolder(@NonNull final View itemView, Context context) {
            super(itemView);
            this.context = context;
            txtPersonName = itemView.findViewById(R.id.txtPersonName);
            txtPersonSaying = itemView.findViewById(R.id.txtPersonSaying);
            personImage = itemView.findViewById(R.id.personImage);
            icEdit = itemView.findViewById(R.id.icEdit);
            icDelete = itemView.findViewById(R.id.icDelete);
        }
    }
}

