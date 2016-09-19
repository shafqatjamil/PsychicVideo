package avreye.mytarotadvisor.helper;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import com.github.siyamed.shapeimageview.BubbleImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import avreye.mytarotadvisor.R;
import avreye.mytarotadvisor.utils.Util;

/**
 * Created by shafqat on 8/26/16.
 */

public class ImageLoadinginList {


    Context mContext;
    List<String> taskList;
    public ImageLoadinginList(Context context)
    {
        this.mContext = context;
        taskList = new ArrayList<>();
    }
    public String saveImage(Context context, Bitmap b, String name){
        ContextWrapper cw = new ContextWrapper(context.getApplicationContext());
        File directory = cw.getDir("profile", Context.MODE_PRIVATE);
        if (!directory.exists()) {
            directory.mkdir();
        }
        File mypath = new File(directory, name);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            b.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();

            Log.e("SAVE_IMAGE", mypath.getPath());
        } catch (Exception e) {
            Log.e("SAVE_IMAGE", e.getMessage(), e);
        }
        return mypath.getPath();
    }


    public Bitmap getImageBitmap(Context context,String name,String extension){
        name=name+"."+extension;
        try{
            FileInputStream fis = context.openFileInput(name);
            Bitmap b = BitmapFactory.decodeStream(fis);
            fis.close();
            return b;
        }
        catch(Exception e){
        }
        return null;
    }
    class ImageDownloaderTask extends AsyncTask<String, Void, Bitmap> {
        private final WeakReference<com.github.siyamed.shapeimageview.mask.PorterShapeImageView> imageViewReference;
        String filename;
        public ImageDownloaderTask(com.github.siyamed.shapeimageview.mask.PorterShapeImageView imageView) {
            imageViewReference = new WeakReference<com.github.siyamed.shapeimageview.mask.PorterShapeImageView>(imageView);

        }
        @Override
        protected Bitmap doInBackground(String... params) {
            String str = params[0].replace("https://s3.amazonaws.com/studiosbucket/","");
            String str2  = str.replace(".mp4","");
            filename = str2 + ".png";
            try {
                return Util.getThumbnailfromVideoURL(params[0]);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                return null;
            }
        }
        @Override
        protected void onPostExecute(Bitmap bitmap) {

            if (isCancelled()) {
                bitmap = null;
            }
            if (imageViewReference != null) {
                ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    if (bitmap != null) {
                        String url = "";
                        imageView.setImageBitmap(bitmap);
                        //   saveImage(mContext, bitmap,filename,"png");
                       // String path = MediaStore.Images.Media.insertImage(mContext.getContentResolver(),bitmap, filename, null);

                       // saveImage(mContext, bitmap, filename);


                        UserSession.getInstance(mContext).setThumbnailUri(filename,"file:" + saveImage(mContext, bitmap, filename));


                       // Log.e("imagepath",path);
                    } else {
                        Drawable placeholder = imageView.getContext().getResources().getDrawable(R.drawable.placeholder);
                        imageView.setImageDrawable(placeholder);
                    }
                }
            }
        }
    }
    class ImageLoadTask extends AsyncTask<String, Void, Bitmap> {
        private final WeakReference<com.github.siyamed.shapeimageview.mask.PorterShapeImageView> imageViewReference;
        private  final com.github.siyamed.shapeimageview.mask.PorterShapeImageView imageViewReference1;
        String filename;
        String str;
        String str3;
        public ImageLoadTask(com.github.siyamed.shapeimageview.mask.PorterShapeImageView imageView) {
            imageViewReference = new WeakReference<com.github.siyamed.shapeimageview.mask.PorterShapeImageView>(imageView);
            imageViewReference1 = imageView;

        }
        @Override
        protected Bitmap doInBackground(String... params) {
            str = params[0].replace("https://s3.amazonaws.com/studiosbucket/","");
            str3 = params[0];
            String str2  = str.replace(".mp4","");
            filename = str2;
            return getImageBitmap(mContext,filename,"png");
        }
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (isCancelled()) {
                bitmap = null;
            }

            if (imageViewReference != null) {
                ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    if (bitmap != null) {
                        imageView.setImageBitmap(bitmap);
                        Log.e("ImageLoading", "in loading task");
                    } else {
                        new ImageDownloaderTask(imageViewReference1).execute(str3 );
                    }
                }
            }
        }
    }
    public void SetThumbnail(com.github.siyamed.shapeimageview.mask.PorterShapeImageView imageView_video, String Url)
    {
        String str = Url.replace("https://s3.amazonaws.com/studiosbucket/","");
        String str2  = str.replace(".mp4","");
        for(int i = 0; i < taskList.size(); i++)
        {
            if(str2.contains(taskList.get(i)))
            {
                return;
            }
        }
        taskList.add(str2);
        new ImageLoadTask(imageView_video).execute(Url);
    }




}
