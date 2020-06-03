package kg.buhara.view.detail_restaurant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.viewpager.widget.PagerAdapter;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import kg.buhara.R;

public class PagerGalaryAdapter extends PagerAdapter {

    private ArrayList<String> images;
    private LayoutInflater inflater;
    private Context context;
    boolean isFull = false;

    private OnClickListener listener;

    public interface OnClickListener {
        void onClick(int pos);
    }

    public void setOnClickListener(OnClickListener listener) {
        this.listener = listener;
    }

    public PagerGalaryAdapter(Context context, ArrayList<String> images, boolean isFull) {
        this.context = context;
        this.images = images;
        this.isFull = isFull;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View myImageLayout;
        if (isFull){
            myImageLayout = inflater.inflate(R.layout.slide, view, false);
        }else {
            myImageLayout = inflater.inflate(R.layout.slide_image, view, false);
        }
        ImageView myImage = myImageLayout.findViewById(R.id.image);
        Glide.with(context).load(images.get(position)).into(myImage);
        myImage.setOnClickListener(v -> {
            if (listener != null) {
                listener.onClick(position);
            }
        });
        view.addView(myImageLayout, 0);
        return myImageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}