package com.meiji.toutiao.binder.media;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.meiji.toutiao.ErrorAction;
import com.meiji.toutiao.R;
import com.meiji.toutiao.bean.media.MultiMediaArticleBean;
import com.meiji.toutiao.bean.news.MultiNewsArticleDataBean;
import com.meiji.toutiao.module.news.content.NewsContentActivity;
import com.meiji.toutiao.util.ImageLoader;
import com.meiji.toutiao.util.NetWorkUtil;
import com.meiji.toutiao.util.TimeUtil;

import java.util.List;

import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by Meiji on 2017/6/8.
 * 带图片的 item
 */

public class MediaArticleImgViewBinder extends ItemViewBinder<MultiMediaArticleBean.DataBean, MediaArticleImgViewBinder.ViewHolder> {

    @NonNull
    @Override
    protected MediaArticleImgViewBinder.ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_media_article_img, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull MediaArticleImgViewBinder.ViewHolder holder, @NonNull final MultiMediaArticleBean.DataBean item) {

        try {
            String imgUrl = "";
            List<MultiMediaArticleBean.DataBean.ImageListBean> imageList = item.getImage_list();
            if (imageList != null && imageList.size() > 0) {
                String url = imageList.get(0).getUrl();
                if (!TextUtils.isEmpty(url)) {
                    if (NetWorkUtil.isWifiConnected(holder.itemView.getContext())) {
                        // 加载高清图
                        url = url.replace("list", "large");
                    }
                    ImageLoader.loadCenterCrop(holder.itemView.getContext(), url, holder.iv_image, R.color.viewBackground);
                    imgUrl = imageList.get(0).getUrl().replace("list", "large");
                }
            }

            String title = item.getTitle();
            String abstractX = item.getAbstractX();
            String readCount = item.getTotal_read_count() + "阅读量";
            String countmmentCount = item.getComment_count() + "评论";
            String time = item.getBehot_time() + "";
            if (!TextUtils.isEmpty(time)) {
                time = TimeUtil.getTimeStampAgo(time);
            }

            holder.tv_title.setText(title);
            holder.tv_abstract.setText(abstractX);
            holder.tv_extra.setText(readCount + " - " + countmmentCount + " - " + time);
            final String finalImgUrl = imgUrl;
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MultiNewsArticleDataBean bean = new MultiNewsArticleDataBean();
                    bean.setTitle(item.getTitle());
                    bean.setDisplay_url(item.getDisplay_url());
                    bean.setMedia_name(item.getSource());
                    bean.setGroup_id(item.getGroup_id());
                    bean.setItem_id(item.getItem_id());
                    MultiNewsArticleDataBean.MediaInfoBean mediaInfo = new MultiNewsArticleDataBean.MediaInfoBean();
                    mediaInfo.setMedia_id(item.getMedia_id() + "");
                    bean.setMedia_info(mediaInfo);
                    NewsContentActivity.launch(bean, finalImgUrl);
                }
            });
        } catch (Exception e) {
            ErrorAction.print(e);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv_image;
        private TextView tv_title;
        private TextView tv_abstract;
        private TextView tv_extra;

        ViewHolder(View itemView) {
            super(itemView);
            this.iv_image = itemView.findViewById(R.id.iv_image);
            this.tv_title = itemView.findViewById(R.id.tv_title);
            this.tv_abstract = itemView.findViewById(R.id.tv_abstract);
            this.tv_extra = itemView.findViewById(R.id.tv_extra);
        }
    }
}
