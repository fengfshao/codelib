package com.berry.spark.sql

/**
 * 非等值join，验证join不到的情况
 *
 * @author leonardo
 * @since 2024/9/12
 *
 */


/*
* select
  t_fgin.search_id as fgin_id,t_fea.search_id as fea_id
  , t_fgin.USER_COMMON_user_semi_c_clk_30d as left,t_fea.USER_COMMON_user_semi_c_clk_30d as right
from t_fgin
left join t_fea
on (
  t_fgin.search_id=t_fea.search_id
    and nvl(t_fgin.USER_COMMON_user_semi_c_clk_30d,'')!=nvl(t_fea.USER_COMMON_user_semi_c_clk_30d,'')
)
* */

/*
* -RECORD 0-----------------------------------------------------------------------
 fgin_id | --1-81M6OsCxdaPwvTUC2Lr3gHjzE0RV3LHbkUhcHbw=:3
 fea_id  | null
 left    | -965378730
 right   | null
-RECORD 1-----------------------------------------------------------------------
 fgin_id | --DXxQfxF4hZH1keXFuSuS35dhY9X7Guz4eaeWhRemwdoxxHd2Xd6d-jPgpPZ3Ig:11
 fea_id  | null
 left    | -796360903
 right   | null
-RECORD 2-----------------------------------------------------------------------
 fgin_id | --EX3fmlUydir3-jZEWr3sVPZPilw0RvNIXYLDCMYMMSU63zInm4UX-fomNUv_a_:115
 fea_id  | null
 left    | -965378730
 right   | null
-RECORD 3-----------------------------------------------------------------------
 fgin_id | --HHW63tiG_LOyzH95cAYaDQXcJh_x09XongQg1TmVkls6qXj3DoBhb5iSpVGLah:2
 fea_id  | null
 left    | 1054378557
 right   | null
 *
 * */
object JoinNotEqDemo {

}
