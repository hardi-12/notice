package com.kjsieit.noticeboard.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.kjsieit.noticeboard.R;
import com.kjsieit.noticeboard.models.resource;
import com.kjsieit.noticeboard.web_view;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.util.ArrayList;

public class ResourceAdapter extends RecyclerView.Adapter<ResourceAdapter.viewholder> {

    ArrayList<resource> resourceList;
    Context context;
    DatabaseReference databaseReference;

    public ResourceAdapter(ArrayList<resource> resourceList, Context context) {
        this.resourceList = resourceList;
        this.context = context;
    }

    @NonNull
    @Override
    public ResourceAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.resource_row, parent, false);
        return new ResourceAdapter.viewholder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ResourceAdapter.viewholder holder, final int position) {
        holder.tvPrintSubject.setText(resourceList.get(position).getSubject());
        holder.tvPrintTitle.setText(resourceList.get(position).getTitle());
        holder.tvPrintAuthor.setText(resourceList.get(position).getAuthor());
        holder.tvPrintPublication.setText(resourceList.get(position).getPublication());
        holder.tvPrintDescription.setText(resourceList.get(position).getDescription());
        holder.tvPrintsemDept.setText(resourceList.get(position).getSemDept());

        databaseReference = FirebaseDatabase.getInstance().getReference().child("References");

        if(resourceList.get(position).getLink().equals("")) {
            holder.tvFile.setText("Click here to view File");
        }
        else {
            holder.tvFile.setText("Click here to open Link");
        }

        if(resourceList.get(position).getAuthor().equals("")) {
            holder.authorLayout.setVisibility(View.GONE);
        }

        if(resourceList.get(position).getPublication().equals("")) {
            holder.publicationLayout.setVisibility(View.GONE);
        }

        if(resourceList.get(position).getDescription().equals("")) {
            holder.descriptionLayout.setVisibility(View.GONE);
        }

        holder.tvFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if(resourceList.get(position).getLink().equals("")) {
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                if (snapshot1.child("title").getValue(String.class).equals(resourceList.get(position).getTitle())) {
                                    snapshot1.getRef().child("files").child("File 0").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            context.startActivity(new Intent(context, web_view.class)
                                                    .putExtra("link",snapshot.getValue(String.class)));
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            //
                        }
                    });
                }
                else {
                    context.startActivity(new Intent(context, web_view.class)
                            .putExtra("link",resourceList.get(position).getLink()));
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.hiddenView.getVisibility() == View.VISIBLE) {
                    TransitionManager.beginDelayedTransition(holder.cardView,
                            new AutoTransition());
                    holder.hiddenView.setVisibility(View.GONE);
                }

                else {
                    TransitionManager.beginDelayedTransition(holder.cardView,
                            new AutoTransition());
                    holder.hiddenView.setVisibility(View.VISIBLE);
                }
            }
        });

        holder.ibQRScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                View view = LayoutInflater.from(v.getContext()).inflate(R.layout.barcode_display, null);
                builder.setView(view);
                ImageView ivQRCode;
                ivQRCode = view.findViewById(R.id.ivQRCode);

                builder.setCancelable(false).setTitle("QR code");

                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                try {
                    BitMatrix bitMatrix = multiFormatWriter.encode(resourceList.get(position).getLink(), BarcodeFormat.QR_CODE, 250, 250);
                    Bitmap bitmap = Bitmap.createBitmap(250, 250, Bitmap.Config.RGB_565);
                    for (int i = 0; i < 250; i++){
                        for (int j = 0; j < 250; j++){
                            bitmap.setPixel(i,j,bitMatrix.get(i,j)? Color.BLACK:Color.WHITE);
                        }
                    }
                    ivQRCode.setImageBitmap(bitmap);
                } catch (WriterException e) {
                    e.printStackTrace();
                }

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
            }
        });

    }


    @Override
    public int getItemCount() {
        return resourceList.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        TextView tvPrintTitle, tvPrintSubject, tvPrintAuthor, tvPrintPublication,
                tvPrintDescription, tvFile, tvPrintsemDept;
        LinearLayout hiddenView, authorLayout, publicationLayout, descriptionLayout ;
        CardView cardView;
        ImageButton ibQRScan;
        public viewholder(View view) {
            super(view);
            cardView = view.findViewById(R.id.cardView);
            hiddenView = view.findViewById(R.id.hiddenView);
            tvPrintTitle = view.findViewById(R.id.tvPrintTitle);
            tvPrintSubject = view.findViewById(R.id.tvPrintSubject);
            tvPrintAuthor = view.findViewById(R.id.tvPrintAuthor);
            tvPrintPublication = view.findViewById(R.id.tvPrintPublication);
            tvPrintDescription = view.findViewById(R.id.tvPrintDescription);
            tvFile = view.findViewById(R.id.tvFile);
            authorLayout = view.findViewById(R.id.authorLayout);
            publicationLayout = view.findViewById(R.id.publicationLayout);
            descriptionLayout = view.findViewById(R.id.descriptionLayout);
            tvPrintsemDept = view.findViewById(R.id.tvPrintsemDept);
            ibQRScan = view.findViewById(R.id.ibQRScan);
        }
    }
}
