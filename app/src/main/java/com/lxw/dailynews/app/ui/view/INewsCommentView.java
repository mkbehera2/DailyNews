package com.lxw.dailynews.app.ui.view;

import com.lxw.dailynews.app.bean.NewsCommentBean;
import com.lxw.dailynews.framework.base.BaseMvpView;

/**
 * Created by lxw9047 on 2016/12/8.
 */

public interface INewsCommentView  extends BaseMvpView{
    void getNewsComments(String newsId, String commentsType);
    void setNewsCommentBean(NewsCommentBean newsCommentBean);
}