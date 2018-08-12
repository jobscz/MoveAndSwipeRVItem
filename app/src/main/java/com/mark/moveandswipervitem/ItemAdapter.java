package com.mark.moveandswipervitem;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

/**
 * Created by mark on 18-8-11.
 */
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    static final String TAG = "ItemAdapter";
    Context mContext;
    List<ImageModel> mModels;

    int bound ;
    public ItemAdapter(){}

    public ItemAdapter(Context context, List<ImageModel> models){
        this.mContext = context;
        this.mModels = models;
        bound = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,80,
                context.getResources().getDisplayMetrics());
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.recycler_view_item,null));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ImageModel model = mModels.get(position);
        // 使用 LruCache 发现 从顶部滑动到底部时 在划过底部时有卡顿
        // 使用 LruCache 发现 从底部滑动顶部到时 在划过顶部时有卡顿
        // 这应该是符合 最少最近使用原则的吧
        // 显示图片
        //ImageLoader.displayRoundImage(model.getImagePath(),holder.imageView,bound,bound);

        // 显示圆角图片
        ImageLoader.displayRoundImage(model.getImagePath(),holder.imageView,bound,bound);
        //holder.imageView.setImageBitmap(BitmapUtil.decodeFile(model.getImagePath(),bound,bound));
        holder.tVPath.setText(model.getImagePath());
        holder.tVName.setText(model.getImageName());
        holder.tVTitle.setText(model.getImageTitle());

        Log.e(TAG,"PATH : " + model.getImagePath());
    }

    @Override
    public int getItemCount() {
        return mModels != null ? mModels.size() : 0;
    }


    static class ViewHolder  extends RecyclerView.ViewHolder{

        public ImageView imageView;
        public TextView tVPath,tVName,tVTitle;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.thumb);
            tVPath = itemView.findViewById(R.id.tv_path);
            tVName = itemView.findViewById(R.id.tv_name);
            tVTitle = itemView.findViewById(R.id.tv_title);
        }
    }
}
