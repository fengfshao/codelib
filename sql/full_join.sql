select
    coalesce(t_binge.uin,t_binge2.uin,t_subscribe.uin,t_subscribe2.uin,t_like.uin,t_like2.uin,t_comment.uin,t_comment2.uin,t_share.uin,t_share2.uin,t_bullet.uin,t_bullet2.uin) uin
    , coalesce(t_binge.seqnum,t_binge2.seqnum,t_subscribe.seqnum,t_subscribe2.seqnum,t_like.seqnum,t_like2.seqnum,t_comment.seqnum,t_comment2.seqnum,t_share.seqnum,t_share2.seqnum,t_bullet.seqnum,t_bullet2.seqnum) seqnum
    , coalesce(t_binge.sourcekey,t_binge2.sourcekey,t_subscribe.sourcekey,t_subscribe2.sourcekey,t_like.sourcekey,t_like2.sourcekey,t_comment.sourcekey,t_comment2.sourcekey,t_share.sourcekey,t_share2.sourcekey,t_bullet.sourcekey,t_bullet2.sourcekey) sourcekey
    , coalesce(t_binge.tag_id,t_binge2.tag_id,t_subscribe.tag_id,t_subscribe2.tag_id,t_like.tag_id,t_like2.tag_id,t_comment.tag_id,t_comment2.tag_id,t_share.tag_id,t_share2.tag_id,t_bullet.tag_id,t_bullet2.tag_id) tag_id
    , coalesce(t_binge.cid,t_binge2.cid,t_subscribe.cid,t_subscribe2.cid,t_like.cid,t_like2.cid,t_comment.cid,t_comment2.cid,t_share.cid,t_share2.cid,t_bullet.cid,t_bullet2.cid) cid
    , coalesce(t_binge.mod_idx,t_binge2.mod_idx,t_subscribe.mod_idx,t_subscribe2.mod_idx,t_like.mod_idx,t_like2.mod_idx,t_comment.mod_idx,t_comment2.mod_idx,t_share.mod_idx,t_share2.mod_idx,t_bullet.mod_idx,t_bullet2.mod_idx) mod_idx
    , nvl(t_binge.binge,0) binge, nvl(t_binge2.binge,0) binge_vid
    , nvl(t_subscribe.subscribe,0) subscribe, nvl(t_subscribe2.subscribe,0) subscribe_vid
    , nvl(t_like.`like`,0) `like`, nvl(t_like2.`like`,0) like_vid
    , nvl(t_comment.`comment`,0) `comment`, nvl(t_comment2.`comment`,0) comment_vid
    , nvl(t_share.share,0) share, nvl(t_share2.share,0) share_vid
    , nvl(t_share.download,0) download, nvl(t_share2.download,0) download_vid
    , nvl(t_bullet.bullet,0) bullet, nvl(t_bullet2.bullet,0) bullet_vid
from
(
    select
        uin
        , seqnum
        , sourcekey
        , tag_id
        , cid
        , mod_idx
        , binge
    from
    (
        select
            concat('qimei36=',qimei36) uin
            ,find_source_ele__seqnum seqnum
            ,find_source_ele__cid cid
            ,cur_cid
            ,find_source_ele__src_key sourcekey
            ,find_source_ele__module_id tag_id
            ,find_source_ele__mod_idx mod_idx
            ,if(eid='binge',1,-1) binge
            ,row_number() over (PARTITION BY find_source_ele__seqnum, find_source_ele__src_key, find_source_ele__module_id,find_source_ele__cid ORDER BY ctime desc) as rank_num
            ,ctime
        from
        (
            select
                qimei36
                ,get_json_object(udf_kv,'$.find_source_ele.seqnum') as find_source_ele__seqnum
                ,get_json_object(udf_kv,'$.find_source_ele.cid') as find_source_ele__cid
                ,nvl(get_json_object(udf_kv,'$.cur_pg.pg_cid'), el_source_cid) as cur_cid
                ,get_json_object(udf_kv,'$.find_source_ele.cut_vid') as find_source_ele__cut_vid --强区跳转为null
                ,get_json_object(udf_kv,'$.find_source_ele.src_key') as find_source_ele__src_key
                ,get_json_object(udf_kv,'$.find_source_ele.ztid') as find_source_ele__ztid
                ,get_json_object(udf_kv,'$.find_source_ele.module_id') as find_source_ele__module_id
                ,get_json_object(udf_kv,'$.find_source_ele.mod_idx') as find_source_ele__mod_idx
                ,eid
                ,ctime
            from xxx_video_dwd::dwd_app_back_ia_h_zl
            where
                imp_hour>=${interactionStartTime} and imp_hour<=${interactionEndTime}
                and biz_id in ('1001002001','1001002002','1001002004002','1001002003002','1001002003001','1001002004001')
                and (is_fake <> '1' or is_fake is null)
                and eid in('binge','cancel_binge')   --如果要取消在追的话，eid = 'cancel_binge'
        )
        where length(find_source_ele__seqnum)>40 and length(find_source_ele__cid)=15
        and find_source_ele__cid=cur_cid
    )
    where rank_num=1
) t_binge
FULL OUTER JOIN
(
    select
        uin
        , seqnum
        , sourcekey
        , cid
        , tag_id
        , mod_idx
        , binge
    from
    (
        select
            concat('qimei36=',qimei36) uin
            ,find_source_ele__seqnum seqnum
            ,find_source_ele__cid cid
            ,cur_cid
            ,find_source_ele__src_key sourcekey
            ,find_source_ele__module_id tag_id
            ,find_source_ele__mod_idx mod_idx
            ,if(eid='binge',1,-1) binge
            ,row_number() over (PARTITION BY find_source_ele__seqnum, find_source_ele__src_key, find_source_ele__module_id,find_source_ele__cid ORDER BY ctime desc) as rank_num
            ,ctime
        from
        (
            select
                qimei36
                ,get_json_object(udf_kv,'$.find_source_ele.seqnum') as find_source_ele__seqnum
                ,get_json_object(udf_kv,'$.find_source_ele.cid') as find_source_ele__cid
                ,nvl(get_json_object(udf_kv,'$.cur_pg.pg_cid'), el_source_cid) as cur_cid
                ,get_json_object(udf_kv,'$.find_source_ele.cut_vid') as find_source_ele__cut_vid
                ,get_json_object(udf_kv,'$.cur_pg.pg_vid') as vid
                ,get_json_object(udf_kv,'$.find_source_ele.src_key') as find_source_ele__src_key
                ,get_json_object(udf_kv,'$.find_source_ele.ztid') as find_source_ele__ztid
                ,get_json_object(udf_kv,'$.find_source_ele.module_id') as find_source_ele__module_id
                ,get_json_object(udf_kv,'$.find_source_ele.mod_idx') as find_source_ele__mod_idx
                ,eid
                ,ctime
            from xxx_video_dwd::dwd_app_back_ia_h_zl
            where
                imp_hour>=${interactionStartTime} and imp_hour<=${interactionEndTime}
                and biz_id in ('1001002001','1001002002','1001002004002','1001002003002','1001002003001','1001002004001')
                and (is_fake <> '1' or is_fake is null)
                and eid in('binge','cancel_binge')   --如果要取消在追的话，eid = 'cancel_binge'
        )
        where length(find_source_ele__seqnum)>40 and length(find_source_ele__cid)=15
        and find_source_ele__cid=cur_cid
        and find_source_ele__cut_vid=vid
    )
    where rank_num=1
) t_binge2
ON
    t_binge.seqnum=t_binge2.seqnum
    AND t_binge.uin=t_binge2.uin
    AND t_binge.cid=t_binge2.cid
    AND t_binge.sourcekey=t_binge2.sourcekey
    AND t_binge.tag_id=t_binge2.tag_id
    AND t_binge.mod_idx=t_binge2.mod_idx
FULL OUTER JOIN
(
    select
        uin
        , seqnum
        , sourcekey
        , tag_id
        , cid
        , mod_idx
        , subscribe
    from
    (
        select
            concat('qimei36=',qimei36) uin
            ,find_source_ele__seqnum seqnum
            ,find_source_ele__cid cid
            ,cur_cid
            ,find_source_ele__src_key sourcekey
            ,find_source_ele__module_id tag_id
            ,find_source_ele__mod_idx mod_idx
            ,if(eid='subscribe',1,-1) subscribe
            ,row_number() over (PARTITION BY find_source_ele__seqnum, find_source_ele__src_key, find_source_ele__module_id,find_source_ele__cid ORDER BY ctime desc) as rank_num
            ,ctime
        from
        (
            select
                qimei36
                ,get_json_object(udf_kv,'$.find_source_ele.seqnum') as find_source_ele__seqnum
                ,get_json_object(udf_kv,'$.find_source_ele.cid') as find_source_ele__cid
                ,nvl(get_json_object(udf_kv,'$.cur_pg.pg_cid'), el_source_cid) as cur_cid
                ,get_json_object(udf_kv,'$.find_source_ele.cut_vid') as find_source_ele__cut_vid
                ,get_json_object(udf_kv,'$.find_source_ele.src_key') as find_source_ele__src_key
                ,get_json_object(udf_kv,'$.find_source_ele.ztid') as find_source_ele__ztid
                ,get_json_object(udf_kv,'$.find_source_ele.module_id') as find_source_ele__module_id
                ,get_json_object(udf_kv,'$.find_source_ele.mod_idx') as find_source_ele__mod_idx
                ,eid
                ,ctime
            from xxx_video_dwd::dwd_app_back_ia_h_zl
            where
                imp_hour>=${interactionStartTime} and imp_hour<=${interactionEndTime}
                and biz_id in ('1001002001','1001002002','1001002004002','1001002003002','1001002003001','1001002004001')
                and (is_fake <> '1' or is_fake is null)
                and eid in('subscribe','unsubscribe')
        )
        where length(find_source_ele__seqnum)>40 and length(find_source_ele__cid)=15
        and find_source_ele__cid=cur_cid
    )
    where rank_num=1
) t_subscribe
ON
    coalesce(t_binge.seqnum,t_binge2.seqnum)=t_subscribe.seqnum
    AND coalesce(t_binge.uin,t_binge2.uin)=t_subscribe.uin
    AND coalesce(t_binge.cid,t_binge2.cid)=t_subscribe.cid
    AND coalesce(t_binge.sourcekey,t_binge2.sourcekey)=t_subscribe.sourcekey
    AND coalesce(t_binge.tag_id,t_binge2.tag_id)=t_subscribe.tag_id
    AND coalesce(t_binge.mod_idx,t_binge2.mod_idx)=t_subscribe.mod_idx