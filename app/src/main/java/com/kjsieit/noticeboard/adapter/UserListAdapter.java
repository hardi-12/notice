package com.kjsieit.noticeboard.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kjsieit.noticeboard.R;
import com.kjsieit.noticeboard.models.user;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.viewHolder> {

    ArrayList<user> userArrayList;

    public UserListAdapter(ArrayList<user> userArrayList) {
        this.userArrayList = userArrayList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.userlist, parent, false);
        return new UserListAdapter.viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final viewHolder holder, final int position) {
        holder.tvUserName.setText(userArrayList.get(position).getName());
        holder.tvDepartment.setText(userArrayList.get(position).getDepartment());
        holder.tvEmail.setText(userArrayList.get(position).getEmail());
        if(userArrayList.get(position).getDesignation().equals(""))
            holder.tvSemDeg.setText(userArrayList.get(position).getSemester());
        else
            holder.tvSemDeg.setText(userArrayList.get(position).getDesignation());
        holder.tvContact.setVisibility(View.INVISIBLE);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String mail = user.getEmail().replace(".", "_dot_");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("user");
        reference.child(mail).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String typ = dataSnapshot.child("type").getValue().toString();
                if (typ.equals("student")) {
                    holder.tvContact.setVisibility(View.GONE);
                }
                if (typ.equals("admin")) {
                    holder.tvContact.setVisibility(View.VISIBLE);
                    holder.tvContact.setText(userArrayList.get(position).getPhone());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String mail = user.getEmail().replace(".", "_dot_");
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("user");
                reference.child(mail).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String typ = dataSnapshot.child("type").getValue().toString();
                        if (typ.equals("student")) {
                            holder.tvContact.setVisibility(View.GONE);
                            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                            builder.setMessage("Do you want to E-mail : "+userArrayList.get(position).getEmail());
                            builder.setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    v.getContext().startActivity(new Intent(Intent.ACTION_SENDTO).setData(Uri.parse("mailto:"+userArrayList.get(position).getEmail())));
                                }
                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            builder.create().show();
                        }
                        if (typ.equals("admin")) {
                            holder.tvContact.setVisibility(View.VISIBLE);
                            holder.tvContact.setText(userArrayList.get(position).getPhone());
                            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                            builder.setTitle(userArrayList.get(position).getName());
                            builder.setMessage("Do you want to contact this user : "+userArrayList.get(position).getEmail());
                            builder.setCancelable(false).setPositiveButton("Email", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    v.getContext().startActivity(new Intent(Intent.ACTION_SENDTO).setData(Uri.parse("mailto:"+userArrayList.get(position).getEmail())));
                                }
                            }).setNegativeButton("Call", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    v.getContext().startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + userArrayList.get(position).getPhone())));
                                }
                            }).setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            builder.create().show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toasty.error(v.getContext(), "Error : "+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    class viewHolder extends RecyclerView.ViewHolder {
        TextView tvUserName,tvEmail,tvDepartment,tvSemDeg,tvContact;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvDepartment = itemView.findViewById(R.id.tvDepartment);
            tvSemDeg = itemView.findViewById(R.id.tvSemDeg);
            tvContact = itemView.findViewById(R.id.tvContact);
        }
    }
}
