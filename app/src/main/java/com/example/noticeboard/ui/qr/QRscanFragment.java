package com.example.noticeboard.ui.qr;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.noticeboard.R;
import com.example.noticeboard.web_view;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class QRscanFragment extends Fragment {

    public QRscanFragment() {
        // Required empty public constructor
    }

    Button btnScanQR, btnVisit;
    TextView tvScannedData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_q_rscan, container, false);
        btnScanQR = view.findViewById(R.id.btnScanQR);
        tvScannedData = view.findViewById(R.id.tvScannedData);
        btnVisit = view.findViewById(R.id.btnVisit);

        btnScanQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                IntentIntegrator integrator = IntentIntegrator.forSupportFragment(QRscanFragment.this);
//                //integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
//                integrator.setBeepEnabled(false);
//                integrator.initiateScan();

                Intent pickIntent = new Intent(Intent.ACTION_PICK);
                pickIntent.setDataAndType( android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");

                startActivityForResult(pickIntent, 111);
            }
        });

        btnVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent m = new Intent(getContext(), web_view.class);
                m.putExtra("link",tvScannedData.getText().toString());
                startActivity(m);
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111) {
            if (data == null || data.getData() == null) {
                tvScannedData.setText("Scan cancelled");
                return;
            }
            Uri uri = data.getData();
            try {
                InputStream inputStream = getActivity().getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                if (bitmap == null) {
                    tvScannedData.setText("uri is not a bitmap," + uri.toString());
                    return;
                }
                int width = bitmap.getWidth(), height = bitmap.getHeight();
                int[] pixels = new int[width * height];
                bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
                bitmap.recycle();
                bitmap = null;
                RGBLuminanceSource source = new RGBLuminanceSource(width, height, pixels);
                BinaryBitmap bBitmap = new BinaryBitmap(new HybridBinarizer(source));
                MultiFormatReader reader = new MultiFormatReader();
                try {
                    Result result = reader.decode(bBitmap);
                    tvScannedData.setText(result.getText());
                    btnVisit.setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(), "The content of the QR image is: " + result.getText(), Toast.LENGTH_SHORT).show();
                } catch (NotFoundException e) {
                    tvScannedData.setText("decode exception\n"+e.getLocalizedMessage());
                }
            } catch (FileNotFoundException e) {
                tvScannedData.setText("can not open file" + uri.toString()+"\n"+e.getLocalizedMessage());
            }
        }
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
//        if (result != null) {
//            if (result.getContents() == null) {
//                tvScannedData.setText("Scan cancelled");
//                Toast.makeText(getContext(), "Scan cancelled", Toast.LENGTH_LONG).show();
//            }
//            else {
//                btnVisit.setVisibility(View.VISIBLE);
//                tvScannedData.setText(result.getContents());
//            }
//        }
//        else {
//            super.onActivityResult(requestCode, resultCode, data);
//        }
//    }
}