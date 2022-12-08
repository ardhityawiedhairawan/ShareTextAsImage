package web.id.aiueo.sharetextasimage;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import web.id.aiueo.sharetextasimage.databinding.FragmentFirstBinding;

public class Fragment extends androidx.fragment.app.Fragment {

    private FragmentFirstBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveShareImage();
            }
        });
    }

    private Bitmap getContent(View view){
        /* get screenshot content */
        view.setDrawingCacheEnabled(true);
        Bitmap bm = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        return bm;
    }
    private void saveShareImage(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat;
        dateFormat = new SimpleDateFormat("ddMMyyyy");
        String dToday = dateFormat.format(calendar.getTime());
        String fileName = "share_" +dToday + ".png";
        View content = getView().findViewById(R.id.frame);
        Bitmap bm = getContent(content);

        String dirPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString();

        File dir = new File(dirPath);
        if(!dir.exists()){
            dir.mkdir();
        }
        File file = new File(dirPath, fileName);
        Log.d("apppp",file.toString());
        try{
            FileOutputStream fis = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 100, fis);
            fis.flush();
            fis.close();
            Toast.makeText(getActivity(), "Saved", Toast.LENGTH_LONG).show();
            /* share intent */
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("image/jpeg");
            share.putExtra(Intent.EXTRA_STREAM, Uri.parse(file.toString()));
            startActivity(Intent.createChooser(share, "Share Image"));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}