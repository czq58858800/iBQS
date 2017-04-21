package com.bq.shuo.core.helper;

/**
 * Created by Administrator on 2017/4/13 0013.
 */
public final class CounterHelper {

    public static final String COUNTER_KEY = "counter:";

    public static final class  Subject {

        public static final String  SUBJECT_COUNTER_KEY = COUNTER_KEY+"subject:";

        /**浏览数*/
        public static final String  VIEW = "view_num";
        /**喜欢数*/
        public static final String  LIKED = "liked_num";
        /**转发数*/
        public static final String  FORWARD = "forward_num";
        /**评论数*/
        public static final String  COMMENTS = "comments_num";
        /**作品喜欢数*/
        public static final String  WORKS_LIKE = "works_like_num";
        /**我喜欢的作品数*/
        public static final String  MY_LIKE_WORKS = "my_like_works_num";

    }


    public static class User {
        public static final String USER_COUNTER_KEY = COUNTER_KEY+"user:";
        /**作品被喜欢总数*/
        public static final String WORKS_LIKE = "works_like_num";
        /**关注数量*/
        public static final String FOLLOW = "follow_num";
        /**粉丝数*/
        public static final String FANS = "fans_num";
        /**素材被收藏数量*/
        public static final String STUFF_COLL = "stuff_coll_num";
        /**我收藏的素材数*/
        public static final String MY_COLL_STUFF = "my_coll_stuff_num";
        /**转发数量*/
        public static final String FORWARD = "forward_num";
        /**我喜欢的作品总数*/
        public static final String MY_LIKE_WORKS = "my_like_works_num";
        /**作品数量*/
        public static final String WORKS = "works_num";
    }

    public static final class Category {

        public static final String CATEGORY_COUNTER_KEY = COUNTER_KEY+"category:";

        /**浏览数*/
        public static final String  VIEW = "view_num";

        /**收藏数*/
        public static final String  COLLECTION = "collection_num";
    }

    public static final class Comments {
        public static final String COMMENTS_COUNTER_KEY = COUNTER_KEY+"comments:";
        /**评论点赞数*/
        public static final String PRAISE = "praise_num";

    }
}
